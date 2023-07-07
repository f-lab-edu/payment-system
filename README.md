## 💰 payment-system

<center><img src="https://github.com/f-lab-edu/payment-system/assets/98700133/3aa62a31-a9fb-4043-89b8-7e31e1b1daee"  width="60%" height="60%"/></center>

작성자: 김예지

여러 pg사들과 연동한 결제 시스템

참고사항: 보안상 비밀번호는 암호화돼서 저장됩니다.

<br>

### 📌 기술 스택
* for Application : `Java 17` `Spring boot` `MySQL` `JPA` `QueryDSL` `Junit5` `redis`

* for production : `Docker`


<br>

### 📌 주요 기능

1) 회원가입
  - 서버에서 유저 정보 관리
  - 가입 시 유저 아이디 대신 이메일로 가입하도록 하고, 이메일로 인증번호를 보내서 인증하도록 하는 방식
  - redis에 인증번호 정보 저장해서 ttl안에 인증하도록 하도록하고, 시간이 지나면 자동으로 인증번호
  - bcrypt 로 암호화해서 비밀번호 저장
  
  <br>
  
  
2) 로그인
  - redis 에 세션 저장한 로그인 유지 방식
  
  <br>
  
3) 단건 결제
- 카카오, 토스 pg사 연동
- enum 값으로 pg사 식별
- 전략패턴을 이용해서 하나의 인터페이스 및 로직으로 여러 pg사 api 사용할 수 있도록 함 
- 테스트 키 사용해서 실제 결제는 일어나지 않도록 함

<br>

4) 결제 취소

<br>

5) 단건 주문 조회

<br>

6) 주문
- reddison 분산락을 이용해 재고 처리를 위한 동시성 이슈 처리(productId 식별자로 이용)
- 프로젝트 확장 가능성을 고려해 설계한 api 로 다양한 메뉴 관리는 따로 하지 않음

<br>


### 📌 프로젝트를 통해 얻고 싶은 기술 역량

- 회원가입 시 유저와 사이트의 신뢰성 있는 관계 수립을 위해서 이메일 인증을 통한 가입, ip 대역 확인해서 한국이 아닐 경우 유효하지 않은 요청으로 처리
- 신뢰성 있는 결제 시스템을 위한 동시성 이슈 고려, 성능과 신뢰성 적절한 범위 내에서 trade-off 관계 고민하기
- 로그인
  - 세션관리 : 로드밸런싱을 통한 분산 처리 시스템에서 적절한 로그인 세션 처리를 위해 세션을 redis에 관리
  - 토큰발행 : 토큰 탈취 보안 문제에 대한 대비책 고민 
- redis 를 자유자재로 필요한 곳에 사용하기 : 캐싱, lock 여부 확인

<br>

### 📌 api & db 설계

:clipboard:[api설계](https://closed-glade-095.notion.site/flab-d83ee2e4bd5d4f0cb3645f597ec53f2f)

- db 설계

![image](https://github.com/f-lab-edu/payment-system/assets/98700133/1e42a1be-ac3d-4e32-a860-247f457b30fc)
