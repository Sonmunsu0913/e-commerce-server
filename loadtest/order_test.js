import http from 'k6/http';
import { check, fail } from 'k6';

export let options = {
    vus: 100,
    duration: '10s',
    summaryTrendStats: ['avg', 'min', 'max', 'p(50)', 'p(90)', 'p(95)', 'p(99)'],
};

export default function () {
    const payload = JSON.stringify({
        userId: 1,
        items: [
            { productId: 1, quantity: 1 }
        ],
        couponId: null,
    });

    const headers = { 'Content-Type': 'application/json' };

    const res = http.post('http://localhost:8080/api/order', payload, { headers });

    const success = check(res, {
        'status is 200': (r) => r.status === 200,
    });

    if (!success) {
        console.error(`❌ 실패: ${res.status} - ${res.body}`);
        // fail('요청 실패'); // 이걸 추가하면 실행 중단도 가능
    }
}
