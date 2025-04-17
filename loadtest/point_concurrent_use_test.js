import http from 'k6/http';
import { check } from 'k6';

export const options = {
    vus: 100, // 가상 유저 100명
    duration: '20s',
    thresholds: {
        http_req_duration: ['p(50)<150', 'p(99)<300'],
    },
};

export function setup() {
    // userId별로 초기화 + 충전
    for (let i = 1; i <= 100; i++) {
        http.del(`http://localhost:8080/api/point/init/${i}`);

        const chargeRes = http.post('http://localhost:8080/api/point/charge', JSON.stringify({
            userId: i,
            amount: 1000000,
        }), {
            headers: { 'Content-Type': 'application/json' }
        });

        if (chargeRes.status !== 200) {
            console.error(`❌ 충전 실패: userId=${i} / ${chargeRes.status} ${chargeRes.body}`);
        }
    }
}

export default function () {
    const userId = __VU; // 각 VU는 서로 다른 userId (1~100)
    const payload = JSON.stringify({
        userId,
        amount: 1000
    });

    const res = http.post('http://localhost:8080/api/point/use', payload, {
        headers: { 'Content-Type': 'application/json' },
    });

    check(res, {
        'status is 200': (r) => r.status === 200,
    });

    if (res.status !== 200) {
        console.error(`❌ ${res.status}: ${res.body}`);
    }
}
