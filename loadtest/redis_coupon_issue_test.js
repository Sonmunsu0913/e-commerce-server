import http from 'k6/http';
import { check } from 'k6';
import { Counter } from 'k6/metrics';

// 진짜 성공 카운터
const successCounter = new Counter('coupon_real_success');
const errorCounter = new Counter('request_error');

export const options = {
  vus: 500,
  duration: '5s',
  thresholds: {
    'coupon_real_success': ['count == 50'],  // 재고 수만큼
    'http_req_duration': ['p(50)<150', 'p(99)<300'],
  },
};

const COUPON_ID = 1;

export default function () {
  const userId = __VU * 1000000 + __ITER;
  const url = `http://localhost:8080/api/coupon/v2/${userId}/coupon/${COUPON_ID}`;

  let res;
  try {
    res = http.post(url, null, {
      headers: { 'Content-Type': 'application/json' },
      timeout: '3s',
    });
  } catch (e) {
    errorCounter.add(1);
    console.error(`❌ 요청 예외: ${e.message}`);
    return;
  }

  if (!res || typeof res.status !== 'number') {
    errorCounter.add(1);
    console.error('❌ 응답이 null이거나 잘못됨');
    return;
  }

  check(res, {
    'status is 200': (r) => r.status === 200,
  });

  // ✅ 응답 본문이 '쿠폰 발급 성공'일 때만 성공 카운트
  if (res.status === 200 && res.body?.trim() === '쿠폰 발급 성공') {
    successCounter.add(1);
  }
}
