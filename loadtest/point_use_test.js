import http from 'k6/http';
import { check } from 'k6';

export function setup() {
    for (let userId = 1; userId <= 100; userId++) {
        // 초기화
        http.del(`http://localhost:8080/api/point/init/${userId}`);

        // 충전
        const res = http.post('http://localhost:8080/api/point/charge', JSON.stringify({
            userId,
            amount: 100000,
        }), {
            headers: { 'Content-Type': 'application/json' }
        });

        if (res.status !== 200) {
            console.error(`❌ 유저 ${userId} 충전 실패: ${res.status} ${res.body}`);
        }
    }
}


export const options = {
    vus: 100,
    duration: '20s',
    thresholds: {
        http_req_duration: ['p(50)<150', 'p(99)<300'],
    },
};

export default function () {
    const userId = __VU; // 각 가상 유저마다 다른 ID
    const url = 'http://localhost:8080/api/point/use';
    const payload = JSON.stringify({
        userId: userId,
        amount: 500
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
