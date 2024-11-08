# java-convenience-store-precourse

## 설명
구매자의 할인 혜택과 재고 상황을 고려하여 최종 결제 금액을 계산하고 안내하는 결제 시스템을 구현한다.

## 구현할 기능 목록

- [x] 사전 정리
  - [x] 요구사항 읽기 (5분)
  - [x] README 정리 (7분)

- [x] 재고 관리
  - [x] 각 상품의 재고 수량을 고려하여 결제 가능 여부를 확인한다. (10분)
  - [x] 고객이 상품을 구매할 때마다, 결제된 수량만큼 해당 상품의 재고에서 차감하여 수량을 관리한다. (5분)
  
- [x] 프로모션 할인
  - [x] 오늘 날짜가 프로모션 기간 내에 포함된 경우에만 할인을 적용한다. (10분)
  - [x] 프로모션은 N개 구매 시 1개 무료 증정(Buy N Get 1 Free)의 형태로 진행된다. (9분)
  - [x] 프로모션 기간 중이라면 프로모션 재고를 우선적으로 차감한다. (15분)
  - [x] 프로모션 재고가 부족할 경우에는 일반 재고를 사용한다. (7분)
  - [x] 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 필요한 수량을 추가로 가져오면 혜택을 받을 수 있음을 안내한다. (37분)
  - [x] 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제하게 됨을 안내한다. (16분)

- [x] 멤버십 할인
  - [x] 멤버십 회원은 프로모션 미적용 금액의 30%를 할인받는다. (34분)
  - [x] 프로모션 적용 후 남은 금액에 대해 멤버십 할인을 적용한다. (45분)
  - [x] 멤버십 할인의 최대 한도는 8,000원이다. (4분)

- [ ] 기타
  - [x] 출력 로직 작성 (24분)
  - [x] 입력 로직 작성 (8분)
  - [x] 파일 입력 (52분)
  - [ ] controller, service 연결 (41분 +)
  - [ ] 기본적인 예외처리

- [ ] 영수증 출력
  - [ ] 영수증은 고객의 구매 내역과 할인을 요약하여 출력한다.
  - [ ] 영수증 항목은 아래와 같다.
  - [ ] 구매 상품 내역: 구매한 상품명, 수량, 가격
  - [ ] 증정 상품 내역: 프로모션에 따라 무료로 제공된 증정 상품의 목록

- [ ] 금액 정보
  - [ ] 총구매액: 구매한 상품의 총 수량과 총 금액
  - [ ] 행사할인: 프로모션에 의해 할인된 금액
  - [ ] 멤버십할인: 멤버십에 의해 추가로 할인된 금액
  - [ ] 내실돈: 최종 결제 금액
  - [ ] 영수증의 구성 요소를 보기 좋게 정렬하여 고객이 쉽게 금액과 수량을 확인할 수 있게 한다.

- [ ] 정리
  - [ ] 사용자가 입력한 상품의 가격과 수량을 기반으로 최종 결제 금액을 계산한다.
  - [ ] 총구매액은 상품별 가격과 수량을 곱하여 계산하며, 프로모션 및 멤버십 할인 정책을 반영하여 최종 결제 금액을 산출한다.
  - [ ] 구매 내역과 산출한 금액 정보를 영수증으로 출력한다.
  - [ ] 영수증 출력 후 추가 구매를 진행할지 또는 종료할지를 선택할 수 있다.
  - [ ] 사용자가 잘못된 값을 입력할 경우 IllegalArgumentException를 발생시키고, "[ERROR]"로 시작하는 에러 메시지를 출력 후 그 부분부터 입력을 다시 받는다.
  - [ ] Exception이 아닌 IllegalArgumentException, IllegalStateException 등과 같은 명확한 유형을 처리한다.
