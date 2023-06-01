## 💰 payment-system
여러 pg사들과 연동한 결제 시스템

<br>

### 📌 기술 스택
* for Application : `Java 17` `Spring boot` `MySQL` `JPA` `QueryDSL` `Junit5` `redis`

* for CI&CD : `Docker` `Jenkins`

* for log : `ELK STACK`

<br>

### 📌 주요 기능

1) 회원가입
  - 서버에서 유저 정보 관리
  
  <br>
  
  
2) 로그인
  - 세션 관리를 통한 로그인 방식 제공
  
  <br>
  
3) 단건 결제 (유저 포인트 충전)
- 카카오, 토스 등 여러 pg사 연동, 이를 위해 인터페이스를 통한 확장성 갖추기
- enum 값으로 pg사 식별
- 테스트 키/개발 api 서버 등을 사용해서 실제 결제는 일어나지 않도록 하기
- 샘플 사진

  ![image](https://github.com/f-lab-edu/payment-system/assets/98700133/6ba17c2d-7342-4fb0-bf5c-fc7f03970c32)

<br>

4) 일반 결제
- 토스페이와 연동해서 여러 결제수단 중 선택할 수 있도록 하기
- 샘플 사진

   ![image](https://github.com/f-lab-edu/payment-system/assets/98700133/a770cce3-8b43-4c98-8d60-422a79e2a8ba)

<br>

5) 결제 취소 
- 테스트 키/개발 api 서버 등을 사용

<br>

6) 단건 주문 조회
- 테스트 키/개발 api 서버 등을 사용

<br>

7) 주문 목록 조회
- 서버에서 직접 저장한 로그 기반으로 목록 응답

<br>

8) 물품 구매
- 물품 구매를 위한 유저 포인트 지불
- 동시성 이슈 처리 및 프로젝트 확장 가능성을 고려해 설계한 api 로 다양한 메뉴 관리는 따로 하지 않음
- 현재 고민 중인 동시성 이슈 처리 방안
* 하나의 redis 서버 사용해서 lock 여부 체크
* userId와 orderId를 sha 알고리즘(256이나 512)으로 해싱해서 식별자로 사용
    

<br>

### 📌 프로젝트를 통해 얻고 싶은 기술 역량


- 신뢰성 있는 결제 시스템을 위한 동시성 이슈 고려, 성능과 신뢰성 적절한 범위 내에서 trade-off 관계 고민하기
- 로드밸런싱을 통한 분산 처리 시스템에서 적절한 로그인 세션 처리 방법 고민
- OAuth 를 통한 로그인 방식 사용 시 따로 토큰 탈취에 대한 대비책이 필요한지 검토 
- redis 를 자유자재로 필요한 곳에 사용하기 : 캐싱, lock 여부 확인, 로그 저장 등
