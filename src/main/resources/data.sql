-- 초기화
DELETE FROM user_coupon;
DELETE FROM coupon;
DELETE FROM user_point;
DELETE FROM product;

-- 유저 포인트 초기화
INSERT INTO user_point (id, point, created_at, updated_at, version)
SELECT seq.id, 100000, NOW(), NOW(), 0
FROM (
 SELECT 1 AS id UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5
 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10
 UNION SELECT 11 UNION SELECT 12 UNION SELECT 13 UNION SELECT 14 UNION SELECT 15
 UNION SELECT 16 UNION SELECT 17 UNION SELECT 18 UNION SELECT 19 UNION SELECT 20
 UNION SELECT 21 UNION SELECT 22 UNION SELECT 23 UNION SELECT 24 UNION SELECT 25
 UNION SELECT 26 UNION SELECT 27 UNION SELECT 28 UNION SELECT 29 UNION SELECT 30
 UNION SELECT 31 UNION SELECT 32 UNION SELECT 33 UNION SELECT 34 UNION SELECT 35
 UNION SELECT 36 UNION SELECT 37 UNION SELECT 38 UNION SELECT 39 UNION SELECT 40
 UNION SELECT 41 UNION SELECT 42 UNION SELECT 43 UNION SELECT 44 UNION SELECT 45
 UNION SELECT 46 UNION SELECT 47 UNION SELECT 48 UNION SELECT 49 UNION SELECT 50
 UNION SELECT 51 UNION SELECT 52 UNION SELECT 53 UNION SELECT 54 UNION SELECT 55
 UNION SELECT 56 UNION SELECT 57 UNION SELECT 58 UNION SELECT 59 UNION SELECT 60
 UNION SELECT 61 UNION SELECT 62 UNION SELECT 63 UNION SELECT 64 UNION SELECT 65
 UNION SELECT 66 UNION SELECT 67 UNION SELECT 68 UNION SELECT 69 UNION SELECT 70
 UNION SELECT 71 UNION SELECT 72 UNION SELECT 73 UNION SELECT 74 UNION SELECT 75
 UNION SELECT 76 UNION SELECT 77 UNION SELECT 78 UNION SELECT 79 UNION SELECT 80
 UNION SELECT 81 UNION SELECT 82 UNION SELECT 83 UNION SELECT 84 UNION SELECT 85
 UNION SELECT 86 UNION SELECT 87 UNION SELECT 88 UNION SELECT 89 UNION SELECT 90
 UNION SELECT 91 UNION SELECT 92 UNION SELECT 93 UNION SELECT 94 UNION SELECT 95
 UNION SELECT 96 UNION SELECT 97 UNION SELECT 98 UNION SELECT 99 UNION SELECT 100
) AS seq;

-- 쿠폰
INSERT INTO coupon (id, name, discount_amount, issued_count, total_quantity) VALUES
 (1, '테스트쿠폰1', 1000, 0, 50);
--  (2, '테스트쿠폰2', 1000, 0, 200),
--  (3, '테스트쿠폰3', 1000, 0, 200),
--  (4, '테스트쿠폰4', 1000, 0, 200),
--  (5, '테스트쿠폰5', 1000, 0, 200),
--  (6, '테스트쿠폰6', 1000, 0, 200),
--  (7, '테스트쿠폰7', 1000, 0, 200),
--  (8, '테스트쿠폰8', 1000, 0, 200),
--  (9, '테스트쿠폰9', 1000, 0, 200),
--  (10, '테스트쿠폰10', 1000, 0, 200),
--  (11, '테스트쿠폰11', 1000, 0, 200),
--  (12, '테스트쿠폰12', 1000, 0, 200),
--  (13, '테스트쿠폰13', 1000, 0, 200),
--  (14, '테스트쿠폰14', 1000, 0, 200),
--  (15, '테스트쿠폰15', 1000, 0, 200),
--  (16, '테스트쿠폰16', 1000, 0, 200),
--  (17, '테스트쿠폰17', 1000, 0, 200),
--  (18, '테스트쿠폰18', 1000, 0, 200),
--  (19, '테스트쿠폰19', 1000, 0, 200),
--  (20, '테스트쿠폰20', 1000, 0, 200),
--  (21, '테스트쿠폰21', 1000, 0, 200),
--  (22, '테스트쿠폰22', 1000, 0, 200),
--  (23, '테스트쿠폰23', 1000, 0, 200),
--  (24, '테스트쿠폰24', 1000, 0, 200),
--  (25, '테스트쿠폰25', 1000, 0, 200),
--  (26, '테스트쿠폰26', 1000, 0, 200),
--  (27, '테스트쿠폰27', 1000, 0, 200),
--  (28, '테스트쿠폰28', 1000, 0, 200),
--  (29, '테스트쿠폰29', 1000, 0, 200),
--  (30, '테스트쿠폰30', 1000, 0, 200),
--  (31, '테스트쿠폰31', 1000, 0, 200),
--  (32, '테스트쿠폰32', 1000, 0, 200),
--  (33, '테스트쿠폰33', 1000, 0, 200),
--  (34, '테스트쿠폰34', 1000, 0, 200),
--  (35, '테스트쿠폰35', 1000, 0, 200),
--  (36, '테스트쿠폰36', 1000, 0, 200),
--  (37, '테스트쿠폰37', 1000, 0, 200),
--  (38, '테스트쿠폰38', 1000, 0, 200),
--  (39, '테스트쿠폰39', 1000, 0, 200),
--  (40, '테스트쿠폰40', 1000, 0, 200),
--  (41, '테스트쿠폰41', 1000, 0, 200),
--  (42, '테스트쿠폰42', 1000, 0, 200),
--  (43, '테스트쿠폰43', 1000, 0, 200),
--  (44, '테스트쿠폰44', 1000, 0, 200),
--  (45, '테스트쿠폰45', 1000, 0, 200),
--  (46, '테스트쿠폰46', 1000, 0, 200),
--  (47, '테스트쿠폰47', 1000, 0, 200),
--  (48, '테스트쿠폰48', 1000, 0, 200),
--  (49, '테스트쿠폰49', 1000, 0, 200),
--  (50, '테스트쿠폰50', 1000, 0, 200),
--  (51, '테스트쿠폰51', 1000, 0, 200),
--  (52, '테스트쿠폰52', 1000, 0, 200),
--  (53, '테스트쿠폰53', 1000, 0, 200),
--  (54, '테스트쿠폰54', 1000, 0, 200),
--  (55, '테스트쿠폰55', 1000, 0, 200),
--  (56, '테스트쿠폰56', 1000, 0, 200),
--  (57, '테스트쿠폰57', 1000, 0, 200),
--  (58, '테스트쿠폰58', 1000, 0, 200),
--  (59, '테스트쿠폰59', 1000, 0, 200),
--  (60, '테스트쿠폰60', 1000, 0, 200),
--  (61, '테스트쿠폰61', 1000, 0, 200),
--  (62, '테스트쿠폰62', 1000, 0, 200),
--  (63, '테스트쿠폰63', 1000, 0, 200),
--  (64, '테스트쿠폰64', 1000, 0, 200),
--  (65, '테스트쿠폰65', 1000, 0, 200),
--  (66, '테스트쿠폰66', 1000, 0, 200),
--  (67, '테스트쿠폰67', 1000, 0, 200),
--  (68, '테스트쿠폰68', 1000, 0, 200),
--  (69, '테스트쿠폰69', 1000, 0, 200),
--  (70, '테스트쿠폰70', 1000, 0, 200),
--  (71, '테스트쿠폰71', 1000, 0, 200),
--  (72, '테스트쿠폰72', 1000, 0, 200),
--  (73, '테스트쿠폰73', 1000, 0, 200),
--  (74, '테스트쿠폰74', 1000, 0, 200),
--  (75, '테스트쿠폰75', 1000, 0, 200),
--  (76, '테스트쿠폰76', 1000, 0, 200),
--  (77, '테스트쿠폰77', 1000, 0, 200),
--  (78, '테스트쿠폰78', 1000, 0, 200),
--  (79, '테스트쿠폰79', 1000, 0, 200),
--  (80, '테스트쿠폰80', 1000, 0, 200),
--  (81, '테스트쿠폰81', 1000, 0, 200),
--  (82, '테스트쿠폰82', 1000, 0, 200),
--  (83, '테스트쿠폰83', 1000, 0, 200),
--  (84, '테스트쿠폰84', 1000, 0, 200),
--  (85, '테스트쿠폰85', 1000, 0, 200),
--  (86, '테스트쿠폰86', 1000, 0, 200),
--  (87, '테스트쿠폰87', 1000, 0, 200),
--  (88, '테스트쿠폰88', 1000, 0, 200),
--  (89, '테스트쿠폰89', 1000, 0, 200),
--  (90, '테스트쿠폰90', 1000, 0, 200),
--  (91, '테스트쿠폰91', 1000, 0, 200),
--  (92, '테스트쿠폰92', 1000, 0, 200),
--  (93, '테스트쿠폰93', 1000, 0, 200),
--  (94, '테스트쿠폰94', 1000, 0, 200),
--  (95, '테스트쿠폰95', 1000, 0, 200),
--  (96, '테스트쿠폰96', 1000, 0, 200),
--  (97, '테스트쿠폰97', 1000, 0, 200),
--  (98, '테스트쿠폰98', 1000, 0, 200),
--  (99, '테스트쿠폰99', 1000, 0, 200),
--  (100, '테스트쿠폰100', 1000, 0, 200);


INSERT INTO product (id, name, price, stock) VALUES
(1, '테스트상품1', 1000, 1000),
(2, '테스트상품2', 2000, 1000),
(3, '테스트상품3', 3000, 1000),
(4, '테스트상품4', 4000, 1000),
(5, '테스트상품5', 5000, 1000),
(6, '테스트상품6', 6000, 1000),
(7, '테스트상품7', 7000, 1000),
(8, '테스트상품8', 8000, 1000),
(9, '테스트상품9', 9000, 1000),
(10, '테스트상품10', 10000, 1000);

-- 오늘
INSERT INTO product_sale (product_id, quantity, sale_date) VALUES
(1, 3, CURDATE()), (2, 2, CURDATE()), (3, 5, CURDATE()), (4, 1, CURDATE()), (5, 4, CURDATE()),
(6, 3, CURDATE()), (7, 2, CURDATE()), (8, 5, CURDATE()), (9, 1, CURDATE()), (10, 4, CURDATE()),
(1, 2, CURDATE()), (3, 1, CURDATE()), (5, 3, CURDATE()), (7, 2, CURDATE()), (9, 4, CURDATE());

-- D-1
INSERT INTO product_sale (product_id, quantity, sale_date) VALUES
(1, 4, CURDATE() - INTERVAL 1 DAY), (2, 3, CURDATE() - INTERVAL 1 DAY), (3, 2, CURDATE() - INTERVAL 1 DAY),
(4, 5, CURDATE() - INTERVAL 1 DAY), (5, 1, CURDATE() - INTERVAL 1 DAY),
(6, 2, CURDATE() - INTERVAL 1 DAY), (7, 4, CURDATE() - INTERVAL 1 DAY), (8, 3, CURDATE() - INTERVAL 1 DAY),
(9, 2, CURDATE() - INTERVAL 1 DAY), (10, 5, CURDATE() - INTERVAL 1 DAY),
(2, 3, CURDATE() - INTERVAL 1 DAY), (4, 1, CURDATE() - INTERVAL 1 DAY), (6, 2, CURDATE() - INTERVAL 1 DAY);

-- D-2
INSERT INTO product_sale (product_id, quantity, sale_date) VALUES
(1, 5, CURDATE() - INTERVAL 2 DAY), (3, 3, CURDATE() - INTERVAL 2 DAY), (5, 2, CURDATE() - INTERVAL 2 DAY),
(7, 1, CURDATE() - INTERVAL 2 DAY), (9, 4, CURDATE() - INTERVAL 2 DAY),
(2, 4, CURDATE() - INTERVAL 2 DAY), (4, 2, CURDATE() - INTERVAL 2 DAY), (6, 3, CURDATE() - INTERVAL 2 DAY),
(8, 5, CURDATE() - INTERVAL 2 DAY), (10, 1, CURDATE() - INTERVAL 2 DAY),
(3, 2, CURDATE() - INTERVAL 2 DAY), (6, 3, CURDATE() - INTERVAL 2 DAY), (9, 1, CURDATE() - INTERVAL 2 DAY);

-- D-3
INSERT INTO product_sale (product_id, quantity, sale_date) VALUES
(1, 2, CURDATE() - INTERVAL 3 DAY), (2, 5, CURDATE() - INTERVAL 3 DAY), (3, 4, CURDATE() - INTERVAL 3 DAY),
(4, 3, CURDATE() - INTERVAL 3 DAY), (5, 1, CURDATE() - INTERVAL 3 DAY),
(6, 5, CURDATE() - INTERVAL 3 DAY), (7, 2, CURDATE() - INTERVAL 3 DAY), (8, 4, CURDATE() - INTERVAL 3 DAY),
(9, 3, CURDATE() - INTERVAL 3 DAY), (10, 2, CURDATE() - INTERVAL 3 DAY),
(1, 1, CURDATE() - INTERVAL 3 DAY), (4, 4, CURDATE() - INTERVAL 3 DAY), (7, 5, CURDATE() - INTERVAL 3 DAY);
