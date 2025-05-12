package kr.hhplus.be.server.domain.coupon.service;

import java.util.concurrent.TimeUnit;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.UserCouponRepository;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.infrastructure.redis.RedisLockRepository;
import kr.hhplus.be.server.interfaces.api.coupon.CouponResponse;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 쿠폰 발급 UseCase
 * - 사용자가 쿠폰을 발급받지 않았고, 수량이 남아있는 경우 쿠폰을 발급한다.
 */
@Service
@Transactional
public class IssueCouponService {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final RedisLockRepository redisLockRepository;
    private final RedissonClient redissonClient;
    private final IssueCouponService self;

    public IssueCouponService(
        CouponRepository couponRepository,
        UserCouponRepository userCouponRepository,
        RedisLockRepository redisLockRepository,
        RedissonClient redissonClient,
        @Lazy IssueCouponService self
    ) {
        this.couponRepository = couponRepository;
        this.userCouponRepository = userCouponRepository;
        this.redisLockRepository = redisLockRepository;
        this.redissonClient = redissonClient;
        this.self = self;
    }

    /**
     * 발급 가능한 쿠폰 조회 (잔여 수량 && 미발급)
     */
    public List<CouponResponse> getAvailableCoupons(Long userId) {
        List<Long> alreadyIssued = userCouponRepository.findAllByUserId(userId).stream()
            .map(UserCoupon::couponId)
            .collect(Collectors.toList());

        return couponRepository.findAllCoupon().stream()
            .filter(Coupon::canIssue)
            .filter(coupon -> !alreadyIssued.contains(coupon.getId()))
            .map(coupon -> CouponResponse.from(null, coupon))
            .toList();
    }

    /**
     * 외부에서 호출하는 메서드 - 락 획득 → 내부 트랜잭션 메서드 호출
     */
    public CouponResponse execute(Long userId, Long couponId) {
        String lockKey = "coupon:lock:" + couponId;

        System.out.println("[쿠폰 발급 요청] userId = " + userId);

        // redis simple lock
//        if (!redisLockRepository.lock(lockKey)) {
//            System.out.println("[락 실패] userId = " + userId);
//            throw new IllegalStateException("동시 쿠폰 발급 시도 중입니다. 잠시 후 다시 시도해주세요.");
//        }

        // redis spin lock
        if (!redisLockRepository.tryLockWithRetry(lockKey, 3, 100)) { // 최대 3번, 100ms 간격 재시도
            System.out.println("[락 실패] userId = " + userId);
            throw new IllegalStateException("동시 쿠폰 발급 시도 중입니다. 잠시 후 다시 시도해주세요.");
        }

        System.out.println("[락 획득 성공] userId = " + userId);

        try {
            return self.issueCouponWithTx(userId, couponId); // 프록시 통해 트랜잭션 적용
        } finally {
            redisLockRepository.unlock(lockKey);
            System.out.println("[락 해제] userId = " + userId);
        }

        // 프록시 통해 트랜잭션이 적용된 내부 메서드 호출
//        return self.issueCouponWithTx(userId, couponId);
    }

    //  redisson rlock
//    public CouponResponse execute(Long userId, Long couponId) {
//        String lockKey = "coupon:lock:" + couponId;
//        RLock lock = redissonClient.getLock(lockKey);
//
//        System.out.println("[쿠폰 발급 요청] userId = " + userId);
//
//        try {
//            // 락 시도 (최대 3초 대기, 획득 시 1초 후 자동 만료)
//            if (!lock.tryLock(3, 1, TimeUnit.SECONDS)) {
//                System.out.println("[락 실패] userId = " + userId);
//                throw new IllegalStateException("동시 쿠폰 발급 시도 중입니다. 잠시 후 다시 시도해주세요.");
//            }
//
//            System.out.println("[락 획득 성공] userId = " + userId);
//            // 락을 획득한 경우 트랜잭션 내 발급 로직 수행
//            return self.issueCouponWithTx(userId, couponId);
//
//        } catch (InterruptedException e) {
//            // 락 획득 중 인터럽트 발생 시 처리
//            Thread.currentThread().interrupt();
//            throw new RuntimeException("락 획득 중 인터럽트 발생", e);
//        } finally {
//            // 현재 쓰레드가 락을 보유한 경우에만 해제
//            if (lock.isHeldByCurrentThread()) {
//                lock.unlock();
//                System.out.println("[락 해제] userId = " + userId);
//            }
//        }
//    }

    /**
     * 트랜잭션 내에서 처리되는 실제 발급 로직
     */
    @Transactional
    public CouponResponse issueCouponWithTx(Long userId, Long couponId) {
        // 미사용 쿠폰 존재 시 발급 차단
        if (userCouponRepository.findAllByUserId(userId).stream()
            .anyMatch(userCoupon -> !userCoupon.isUsed())) {
            System.out.println("[미사용 쿠폰 존재] userId = " + userId);
            throw new IllegalStateException("이미 발급받은 미사용 쿠폰이 있습니다.");
        }

        Coupon coupon = couponRepository.findById(couponId)
            .orElseThrow(() -> {
                System.out.println("[쿠폰 없음] couponId = " + couponId);
                return new IllegalArgumentException("존재하지 않는 쿠폰입니다.");
            });

//        Coupon coupon = couponRepository.findWithPessimisticLockById(couponId);
//        if (coupon == null) {
//            System.out.println("[쿠폰 없음] couponId = " + couponId);
//            throw new IllegalArgumentException("존재하지 않는 쿠폰입니다.");
//        }

        if (!coupon.canIssue()) {
            System.out.println("[쿠폰 수량 초과] userId = " + userId);
            throw new IllegalStateException("쿠폰 수량이 모두 소진되었습니다.");
        }

        if (userCouponRepository.existsByUserIdAndCouponId(userId, couponId)) {
            System.out.println("[중복 발급 시도] userId = " + userId);
            throw new IllegalStateException("이미 해당 쿠폰을 발급받았습니다.");
        }

        coupon.issue();
        couponRepository.save(coupon);

        UserCoupon userCoupon = UserCoupon.create(userId, couponId);
        userCouponRepository.save(userCoupon);

        System.out.println("[쿠폰 발급 성공] userId = " + userId + ", couponId = " + couponId);
        System.out.println("현재 수량: " + coupon.getIssuedCount() + " / " + coupon.getTotalQuantity());

        return CouponResponse.from(userCoupon, coupon);
    }
}
