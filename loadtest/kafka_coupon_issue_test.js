import http from 'k6/http';
import { check } from 'k6';
import { Counter } from 'k6/metrics';

// 진짜 Kafka 발급 요청 성공 카운터
const successCounter = new Counter('coupon_kafka_request_success');
const errorCounter = new Counter('request_error');

export const options = {
  vus: 500, // 동시 사용자 수
  duration: '5s', // 테스트 시간
  thresholds: {
    'coupon_kafka_request_success': ['count == 50'],  // 예상 발급 요청 수
    'http_req_duration': ['p(50)<150', 'p(99)<300'],
  },
};

const COUPON_ID = 1;

export default function () {
  const userId = __VU * 1000000 + __ITER;
  const url = `http://localhost:8080/api/coupon/v3/${userId}/coupon/${COUPON_ID}`;

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
    'status is 200 or 202': (r) => r.status === 200 || r.status === 202,
  });

  // Kafka 발급 요청은 성공 응답 메시지로 구분
  if (res.status === 200 && res.body?.trim() === '쿠폰 발급 요청이 접수되었습니다.') {
    successCounter.add(1);
  } else {
    errorCounter.add(1);
    console.log(`❌ Failed: userId=${userId}, status=${res.status}, body=${res.body}`);
  }
}
