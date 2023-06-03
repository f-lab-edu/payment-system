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
  - 가입 시 유저 아이디 대신 이메일로 가입하도록 하고, 이메일로 인증번호를 보내서 인증하도록 하는 방식 
  
  <br>
  
  
2) 로그인
  - 세션 관리 or 토큰 발행을 통한 로그인 유지 방식 제공 (고민 중)
  
  <br>
  
3) 단건 결제
- 카카오, 토스 등 여러 pg사 연동, 이를 위해 인터페이스를 통한 확장성 갖추기
- enum 값으로 pg사 식별
- 테스트 키/개발 api 서버 등을 사용해서 실제 결제는 일어나지 않도록 하기
- 샘플 사진

  ![image](https://github.com/f-lab-edu/payment-system/assets/98700133/6ba17c2d-7342-4fb0-bf5c-fc7f03970c32)

<br>

4) 일반 결제 (보류)
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

7) 포인트 충전 & 물품 구매 (보류)
- 물품 구매를 위한 유저 포인트 지불
- 동시성 이슈 처리 및 프로젝트 확장 가능성을 고려해 설계한 api 로 다양한 메뉴 관리는 따로 하지 않음

- 현재 고민 중인 동시성 이슈 처리 방안
* 하나의 redis 서버 사용해서 lock 여부 체크
* userId와 orderId를 sha 알고리즘(256이나 512)으로 해싱해서 식별자로 사용


<br>

8) 주문 목록 조회 (보류)
- 서버에서 직접 저장한 로그 기반으로 목록 응답

<br>


### 📌 프로젝트를 통해 얻고 싶은 기술 역량

- 회원가입 시 이메일로 인증메일을 보낸 뒤 인증번호를 확인하도록 하는데, 이 때 인증메일을 보낸 유저와 인증번호를 확인하는 유저가 어떻게 같은 유저인지 검증하는 로직 고민하기 -> ip 나 useragent 를 통해서 같은 유저인지 확인하도록 하는 방법이 있으나, 와이파이 등을 사용해서 ip가 계속 바뀌는 유저이면 어떡할 것인지 의문
- 신뢰성 있는 결제 시스템을 위한 동시성 이슈 고려, 성능과 신뢰성 적절한 범위 내에서 trade-off 관계 고민하기
- 세션관리를 통한 로그인 유지 방법 채택 시 : 로드밸런싱을 통한 분산 처리 시스템에서 적절한 로그인 세션 처리 -> 세션을 redis에 관리
- 토큰발행을 통한 로그인 유지 방법 채택 시: 토큰 탈취 보안 문제에 대한 대비책 고민 
- redis 를 자유자재로 필요한 곳에 사용하기 : 캐싱, lock 여부 확인, 로그 저장 등

<br>

### 📌 api & db 설계

:clipboard:[api설계](https://closed-glade-095.notion.site/flab-d83ee2e4bd5d4f0cb3645f597ec53f2f)

- db 설계

![image](https://github.com/f-lab-edu/payment-system/assets/98700133/836a7e58-1478-4efe-b7f5-5693baa1000e)
