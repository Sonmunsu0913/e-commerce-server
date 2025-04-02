# 이커머스 주문 시스템

## 📌 마일스톤
- [프로젝트 보드](https://github.com/users/Sonmunsu0913/projects/1/views/1)

---

## ✅ 사용자 시나리오 (초록: 유저, 노랑: 시스템)

## 👤 사용자 시나리오

> *(🟢 유저 / 🟡 시스템)*

---

### 1. 잔액 확인 및 충전

- 🟢 유저가 서비스에 접속하여 본인의 잔액을 확인한다.
    - 🟢 잔액이 부족하면 충전을 요청한다.
        - 🟡 `잔액 충전을 xxx원 요청하면, xxx원이 충전된다.`
        - 🟡 `충전 후 잔액 조회를 하면 원금 + xxx원이 된다.`
    - 🟢 잔액이 충분하면 바로 구매를 진행한다.

### 2. 상품 리스트 확인

- 🟢 유저가 상품리스트를 확인한다.
    - 🟡 상품 리스트를 조회한다.
        - 🟡 이름, 가격, 남은 재고 등 정보 표시

### 3. 선착순 쿠폰 발급

- 🟢 유저가 쿠폰 발급을 시도한다.
    - 🟡 선착순에 들면 쿠폰을 발급한다.
        - 🟡 쿠폰이 남아 있으면 발급된다.
        - 🟡 쿠폰이 남아 있지 않으면 발급되지 않는다.
    - 🟡 이미 받은 쿠폰이면(같은 이벤트) 다시 발급되지 않는다.
    - 🟢 발급받은 쿠폰은 쿠폰 목록에서 확인한다.

### 4. 상품 주문 및 결제

- 🟢 유저가 상품 리스트를 보고 상품을 선택한다.
- 🟢 주문을 진행한다.
    - 🟡 유저의 잔액이 남았는지 확인한다.
    - 🟡 상품의 재고가 남았는지 확인한다.
    - 🟡 잔액 또는 재고가 없으면 결제를 진행하지 않는다.
    - 🟡 잔액, 재고가 모두 있으면 결제를 진행한다.
        - 🟢 할인 쿠폰 적용 여부를 선택한다.
        - 🟡 쿠폰이 유효하면 할인 금액으로 결제한다.
            - 🟡 할인 쿠폰이 유효한지 검증한다.
        - 🟡 결제가 완료되면 잔액에서 금액이 차감된다.
        - 🟡 상품 재고가 차감된다.
    - 🟡 결제 정보가 외부 시스템으로 전송된다.
- 🟢 결제 완료 후 주문 내역을 확인한다.

### 5. 인기 상품 조회

- 🟢 유저가 인기 상품을 조회한다.
    - 🟡 최근 3일간 가장 많이 팔린 상품 5개를 반환한다.
---

## 🔁 이벤트 스토밍

https://github.com/Sonmunsu0913/e-commerce-server/blob/98854d9ad293c0a6ea4ed61138d10440596485a5/docs/Event_Storming/%EC%9D%B4%EB%B2%A4%ED%8A%B8%20%EC%8A%A4%ED%86%A0%EB%B0%8D.png

---

## ⚙️ 기능 / 비기능 요구사항 정리



### ✅ 기능 (Functional Requirements)

1. **잔액 관련 기능**
    - 사용자 잔액 충전 API *(주요)*
    - 사용자 잔액 조회 API *(주요)*

2. **상품 관련 기능**
    - 상품 목록 조회 API *(기본)*  
      → 항목: 상품 ID, 이름, 가격, 잔여 수량

3. **주문 / 결제 관련 기능**
    - 상품 주문 및 결제 API *(주요)*  
      → 입력: 상품 ID + 수량 목록, 쿠폰(optional)  
      → 동작: 잔액 차감, 재고 감소
    - 주문 성공 시 외부 데이터 플랫폼으로 전송 *(주요)*

4. **쿠폰 관련 기능**
    - 선착순 쿠폰 발급 API *(주요)*  
      → 조건: 남은 수량, 중복 여부
    - 보유 쿠폰 목록 조회 API *(주요)*
    - 쿠폰 적용 결제 로직 포함

5. **인기 상품 기능**
    - 최근 3일간 판매량 기준 상위 5개 상품 조회 *(기본)*

### 🔒 비기능 (Non-Functional Requirements)

#### 🧩 동시성 / 무결성
- 동시에 들어오는 주문 요청에 대해 정확한 처리 보장
- 잔액 차감, 재고 감소가 정확하게 처리됨
- 선착순 쿠폰의 개수 제한 및 중복 방지
- 중복 결제, 과다 차감, 재고 초과 방지

#### 🧮 재고 및 잔액 관리
- 재고 수량은 항상 정확하게 유지되어야 함
- 잔액 차감은 트랜잭션 내에서 안전하게 처리됨

#### ✅ 테스트
- 기능별 단위 테스트 필수
- 핵심 로직에 대해 경계 조건 및 예외 상황 테스트 포함

---

## 🔄 시퀀스 다이어그램

1. 잔액 관련 기능
- 사용자 잔액 충전 API
  - https://github.com/Sonmunsu0913/e-commerce-server/blob/98854d9ad293c0a6ea4ed61138d10440596485a5/docs/Sequence_Diagram/Balance_%EC%82%AC%EC%9A%A9%EC%9E%90%20%EC%9E%94%EC%95%A1%20%EC%B6%A9%EC%A0%84_API%20.png
- 사용자 잔액 조회 API
  - https://github.com/Sonmunsu0913/e-commerce-server/blob/98854d9ad293c0a6ea4ed61138d10440596485a5/docs/Sequence_Diagram/Balance_%EC%82%AC%EC%9A%A9%EC%9E%90%20%EC%9E%94%EC%95%A1%20%EC%A1%B0%ED%9A%8C_API%20.png
2. 상품 관련 기능
- 상품 목록 조회 API
  - https://github.com/Sonmunsu0913/e-commerce-server/blob/98854d9ad293c0a6ea4ed61138d10440596485a5/docs/Sequence_Diagram/User_%EC%83%81%ED%92%88%20%EB%AA%A9%EB%A1%9D%20%EC%A1%B0%ED%9A%8C_API%20.png
3. 주문/결제 관련 기능
- 주문/결제 API
    - https://github.com/Sonmunsu0913/e-commerce-server/blob/98854d9ad293c0a6ea4ed61138d10440596485a5/docs/Sequence_Diagram/Order_%EC%A3%BC%EB%AC%B8:%EA%B2%B0%EC%A0%9C%20API%20.png
4. 쿠폰 관련 기능
- 선착순 쿠폰 발급 API
  - https://github.com/Sonmunsu0913/e-commerce-server/blob/98854d9ad293c0a6ea4ed61138d10440596485a5/docs/Sequence_Diagram/Coupon_%EC%84%A0%EC%B0%A9%EC%88%9C%20%EC%BF%A0%ED%8F%B0%20%EB%B0%9C%EA%B8%89_API%20.png
- 보유 쿠폰 목록 조회 API
  - https://github.com/Sonmunsu0913/e-commerce-server/blob/98854d9ad293c0a6ea4ed61138d10440596485a5/docs/Sequence_Diagram/Coupon_%EB%B3%B4%EC%9C%A0%20%EC%BF%A0%ED%8F%AD%20%EB%AA%A9%EB%A1%9D%20%EC%A1%B0%ED%9A%8C_API%20.png
5. 인기 상품 기능
- 최근 인기 상품 5개 조회 API
  - https://github.com/Sonmunsu0913/e-commerce-server/blob/98854d9ad293c0a6ea4ed61138d10440596485a5/docs/Sequence_Diagram/Product_%EC%9D%B8%EA%B8%B0%20%EC%83%81%ED%92%88%20%EC%A1%B0%ED%9A%8C_API%20.png
---

## 🧱 클래스 다이어그램

https://github.com/Sonmunsu0913/e-commerce-server/blob/654bf2444abe3f99eb68eb1241371de5dd1f66ea/docs/Class_Diagram/%ED%81%B4%EB%9E%98%EC%8A%A4_%EB%8B%A4%EC%9D%B4%EC%96%B4%EA%B7%B8%EB%9E%A8.png

---

## 🗂️ ERD

![ERD](docs/ERD/erd.png)
