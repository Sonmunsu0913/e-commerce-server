import http from 'k6/http';
import { check } from 'k6';

export const options = {
    vus: 100,
    duration: '1s',
    thresholds: {
        http_req_duration: ['p(50)<150', 'p(99)<300'],
    },
};

export default function () {
    const userId = __VU;
    const url = `http://localhost:8080/api/coupon/issue/${userId}`;

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
