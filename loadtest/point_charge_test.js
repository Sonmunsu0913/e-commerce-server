import http from 'k6/http';
import { check } from 'k6';

export const options = {
    vus: 100,
    duration: '20s',
    thresholds: {
        http_req_duration: [
            'p(50)<150',
            'p(99)<300',
        ],
    },
};

export default function () {
    const userId = __VU; // 가상 유저 번호 (1~100)
    const url = 'http://localhost:8080/api/point/charge';

    const payload = JSON.stringify({
        userId: userId,
        amount: Math.floor(Math.random() * 100) + 1
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const res = http.post(url, payload, params);

    check(res, {
        'status is 200': (r) => r.status === 200,
    });

    if (res.status !== 200) {
        console.error(`❌ ${res.status}: ${res.body}`);
    }
}

