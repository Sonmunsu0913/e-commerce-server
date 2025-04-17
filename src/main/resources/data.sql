DELETE FROM coupon;

INSERT INTO coupon (id, name, discount_amount, total_quantity, issued_count)
VALUES
    (1, '테스트쿠폰1', 1000, 1000, 0),
    (2, '테스트쿠폰2', 2000, 500, 0);
