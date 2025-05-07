import http from 'k6/http';
import { check, sleep } from 'k6';
import { Trend } from 'k6/metrics';

export let options = {
  vus: 100,
  duration: '10s',
  thresholds: {
    'http_req_duration': [
      'p(50)<150', // p50 (중간값)
      'p(99)<300', // p99 (최악 1% 기준)
    ],
    'checks': ['rate>0.95'], // 성공률 95% 이상
  },
};

const responseTime = new Trend('response_time');

export default function () {
  const res = http.get('http://localhost:8080/api/product/sale/statistics/popular?range=3d');

  // 응답 시간 수집
  responseTime.add(res.timings.duration);

  // 기본 체크
  const success = check(res, {
    'status is 200': (r) => r.status === 200,
    'response time < 500ms': (r) => r.timings.duration < 500,
  });

  if (!success) {
    console.log(`❌ FAIL [${res.status}] - ${res.body}`);
  }

  // 사용자 간 0.3초 sleep (선택)
  sleep(0.3);
}
