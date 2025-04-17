import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    vus: 100, // 가상 사용자 수
    duration: '10s',
    thresholds: {
        http_req_duration: ['p(50)<150', 'p(99)<300'],
    },
};

export default function () {
    const url = 'http://localhost:8080/api/order';

    const payload = JSON.stringify({
        userId: Math.floor(Math.random() * 10) + 1,
        couponId: null,
        items: [
            {
                productId: 1,
                productName: "테스트상품",
                price: 1000,
                quantity: 1
            }
        ]
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const res = http.post(url, payload, params);

    check(res, {
        '✅ status is 200': (r) => r.status === 200,
    });

    sleep(1);
}
