import http from 'k6/http';
import { check } from 'k6';

export const options = {
    vus: 500,
    duration: '5s',
    thresholds: {
        'http_req_duration': ['p(50)<150', 'p(99)<300']  // 전체 요청 기준
    }
};

const COUPON_ID = 1; // 모든 요청이 이 쿠폰을 발급받으려 시도

export default function () {
    const userId = __VU * 1000000 + __ITER; // 유저 ID 매번 다르게 생성

    const url = `http://localhost:8080/api/coupon/${userId}/coupon/${COUPON_ID}`;

    const res = http.post(url, null, {
        headers: {
            'Content-Type': 'application/json',
        },
    });

    check(res, {
        'status is 200': (r) => r.status === 200,
    });

    if (res.status !== 200) {
        console.error(`❌ ${res.status}: ${res.body}`);
    }
}
