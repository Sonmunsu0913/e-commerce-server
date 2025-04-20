import http from 'k6/http';
import { check } from 'k6';

export let options = {
  vus: 100,
  duration: '10s',
};

export default function () {
  const res = http.get('http://localhost:8080/api/product/sale/statistics/popular?range=3d');

  const success = check(res, {
    '✅ status is 200': (r) => r.status === 200,
    '✅ response time < 500ms': (r) => r.timings.duration < 500,
  });

  if (!success) {
    console.log(`❌ FAIL [${res.status}] - ${res.body}`);
  }
}
