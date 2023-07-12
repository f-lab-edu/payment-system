## 💰 payment-system

> 개인 프로젝트입니다.
> 스프링부트를 이용한 백엔드 개발, 로그 수집 및 테스트 코드 작성을 했습니다.

참고사항: 보안상 비밀번호는 암호화돼서 저장됩니다.

<br>

### 📌 프로젝트 설명

> 평소 돈의 흐름에 관심이 많아 결제 시스템을 구현해보고 싶다는 생각을 했고, 카카오와 토스 결제 api를 연동한 결제 시스템을 구현했습니다.
> 단건 결제 프로세스는 아래와 같습니다.

<center><img src="https://github.com/f-lab-edu/payment-system/assets/98700133/d9dfa411-632c-4b3f-99c2-5e527ee62e34"  width="60%" height="60%"/></center>

<br>


### 📌 폴더 구조

<br>


<details>
    <summary> 🧷 프로젝트 구조 펼쳐보기</summary>

```bash
main
├── java
│   ├── flab.payment_system
│   │   ├──  config
│   │   │   ├──  AppConfig
│   │   │   ├──  QueryDslConfig
│   │   │   ├──  RedisConfig
│   │   │   └──  WebConfig
│   │   ├──  core # 공통
│   │   │   ├──  data
│   │   │   │  └──  BaseEntity
│   │   │   ├──  enums
│   │   │   │  └──  Constant
│   │   │   ├──  exception # 각 domain 의 예외들은 아래 예외들을 상속받음
│   │   │   │  ├──  BadRequestException
│   │   │   │  ├──  BaseException
│   │   │   │  ├──  ConflictException
│   │   │   │  ├──  CustomExceptionHandler
│   │   │   │  ├──  ExceptionMessage
│   │   │   │  ├──  ForbiddenException
│   │   │   │  ├──  OkException
│   │   │   │  ├──  ServiceUnavailableException
│   │   │   │  └──  UnauthorizedException
│   │   │   ├──  filter
│   │   │   │  ├──  ExceptionHandlerFilter
│   │   │   │  └──  SignInCheckFilter
│   │   │   ├──  interceptor
│   │   │   │  └──  LoggingInterceptor
│   │   │   ├──  response
│   │   │   │  └──  ResponseMessage
│   │   │   └──  utils
│   │   │   │  ├──  CookieUtil
│   │   │   │  └──  IpUtil
│   │   ├──  domain
│   │   │   ├──  log
│   │   │   │  ├──  domain
│   │   │   │  │  └──  AppLogs
│   │   │   ├──  mail
│   │   │   │  ├──  service # 유저에게 이메일 보낼 때(회원가입을 위한 유저 인증번호 발송)
│   │   │   │  │  └──  MailService
│   │   │   ├──  order
│   │   │   │  ├──  controller
│   │   │   │  │  └──  OrderController
│   │   │   │  ├──  domain
│   │   │   │  │  └──  Order
│   │   │   │  ├──  dto
│   │   │   │  │  ├──  OrderCancelDto
│   │   │   │  │  ├──  OrderDetailDto
│   │   │   │  │  └──  OrderProductDto
│   │   │   │  ├── exception
│   │   │   │  │  └──  OrderNotExistBadRequestException
│   │   │   │  ├── repository
│   │   │   │  │  ├──  OrderCustomRepository
│   │   │   │  │  ├──  OrderCustomRepositoryImpl
│   │   │   │  │  └──  OrderRepository
│   │   │   │  ├── service
│   │   │   │  │  └──  OrderService
│   │   │   ├──  payment
│   │   │   │  ├──  client # pg사와 통신
│   │   │   │  │  ├──  kakao
│   │   │   │  │  │  └──  PaymentKakaoClient
│   │   │   │  │  ├──  toss
│   │   │   │  │  │  └──  PaymentTossClient
│   │   │   │  ├──  controller
│   │   │   │  │  └──  PaymentController
│   │   │   │  ├── domain
│   │   │   │  ├── Payment
│   │   │   │  │  ├──  kakao 
│   │   │   │  │  │  ├── Amount
│   │   │   │  │  │  ├── ApprovedCancelAmount
│   │   │   │  │  │  ├── CancelAvailableAmount
│   │   │   │  │  │  ├── CanceledAmount
│   │   │   │  │  │  ├── CardInfo
│   │   │   │  │  │  ├── KakaoPayment
│   │   │   │  │  │  └── PaymentActionDetails
│   │   │   │  │  ├──  toss
│   │   │   │  │  │  ├── Cacnels
│   │   │   │  │  │  ├── Card
│   │   │   │  │  │  ├── CashReceipt
│   │   │   │  │  │  ├── Checkout
│   │   │   │  │  │  ├── Discount
│   │   │   │  │  │  ├── EasyPay
│   │   │   │  │  │  ├── Failure
│   │   │   │  │  │  ├── GiftCertificate
│   │   │   │  │  │  ├── MobilePhone
│   │   │   │  │  │  ├── Receipt
│   │   │   │  │  │  ├── RefundReceiveAccount
│   │   │   │  │  │  ├── TossPayment
│   │   │   │  │  │  ├── Transfer
│   │   │   │  │  │  └── VirtualAccount
│   │   │   │  ├──  enums
│   │   │   │  │  ├── PaymentPgCompany
│   │   │   │  │  ├── PaymentPgCompanyStringToEnumConverter
│   │   │   │  │  └── PaymentStateConstant
│   │   │   │  ├──  exception
│   │   │   │  │  ├── PaymentFailBadRequestException
│   │   │   │  │  ├── PaymentKakaoServiceUnavailableException
│   │   │   │  │  ├── PaymentNotExistBadRequestException
│   │   │   │  │  └── PaymentTossServiceUnavailableException
│   │   │   │  ├──  repository
│   │   │   │  │  ├──  PaymentCustomRepository
│   │   │   │  │  ├──  PaymentCustomRepositoryImpl
│   │   │   │  │  ├──  PaymentRepository
│   │   │   │  │  ├──  kakao
│   │   │   │  │  │  ├──  KakaoPaymentCustomRepository
│   │   │   │  │  │  ├──  KakaoPaymentCustomRepositoryImpl
│   │   │   │  │  │  └──  KakaoPaymentRepository
│   │   │   │  │  ├──  toss
│   │   │   │  │  │  ├──  TossPaymentCustomRepository
│   │   │   │  │  │  ├──  TossPaymentCustomRepositoryImpl
│   │   │   │  │  │  └──  TossPaymentRepository
│   │   │   │  ├──  request
│   │   │   │  │  ├──  kakao
│   │   │   │  │  │  └──  PaymentKakaoRequestBodyFactory
│   │   │   │  │  ├──  toss
│   │   │   │  │  │  └──  PaymentTossRequestBodyFactory
│   │   │   │  ├──  response
│   │   │   │  │  ├──  PaymentApprovalDto
│   │   │   │  │  ├──  PaymentCancelDto
│   │   │   │  │  ├──  PaymentOrderDetailDto
│   │   │   │  │  ├──  PaymentReadyDto
│   │   │   │  │  ├──  kakao
│   │   │   │  │  │  ├──  PaymentKakao
│   │   │   │  │  │  ├──  PaymentKakaoApprovalDtoImpl
│   │   │   │  │  │  ├──  PaymentKakaoCancelDtoImpl
│   │   │   │  │  │  ├──  PaymentKakaoOrderDetailDtoImpl
│   │   │   │  │  │  └──  PaymentKakaoReadyDtoImpl
│   │   │   │  │  ├──  toss
│   │   │   │  │  │  ├──  PaymentToss
│   │   │   │  │  │  └──  PaymentTossDtoImpl
│   │   │   │  ├──  service
│   │   │   │  │  ├──  PaymentService
│   │   │   │  │  ├──  PaymentStrategy # enums - PaymentPgCompany 값에 따라서 해당 인터페이스를 구현한 전략 서비스 호출
│   │   │   │  │  ├──  kakao
│   │   │   │  │  │  └──  PaymentStrategyKakaoService
│   │   │   │  │  ├──  toss
│   │   │   │  │  │  └──  PaymentStrategyTossService
│   │   │   │  ├──  product
│   │   │   │  │  ├──  controller
│   │   │   │  │  │  └──  ProductController
│   │   │   │  │  ├──  domain
│   │   │   │  │  │  └──  Product
│   │   │   │  │  ├──  dto
│   │   │   │  │  │  └──  ProductDto
│   │   │   │  │  ├──  exception
│   │   │   │  │  │  ├──  ProductNotExistBadRequestException
│   │   │   │  │  │  └──  ProductSoldOutException
│   │   │   │  │  ├──  repository
│   │   │   │  │  │  ├──  ProductCustomRepository
│   │   │   │  │  │  ├──  ProductCustomRepositoryImpl
│   │   │   │  │  │  └──  ProductRepository
│   │   │   │  │  ├──  service
│   │   │   │  │  │  └──  ProductService
│   │   │   │  ├──  redisson
│   │   │   │  │  ├──  service # 재고 동시성 이슈 처리를 위한 redisson lock
│   │   │   │  │  │  └──  RedissonLockService
│   │   │   │  ├──  session
│   │   │   │  │  ├──  service
│   │   │   │  │  │  └──  SessionService
│   │   │   │  ├──  user
│   │   │   │  │  ├──  controller
│   │   │   │  │  │  └──  UserController
│   │   │   │  │  ├──  domain
│   │   │   │  │  │  ├──  User
│   │   │   │  │  │  └──  UserVerification
│   │   │   │  │  ├──  dto
│   │   │   │  │  │  ├──  UserConfirmVerificationNumberDto
│   │   │   │  │  │  ├──  UserDto
│   │   │   │  │  │  ├──  UserSignUpDto
│   │   │   │  │  │  ├──  UserVerificationDto
│   │   │   │  │  │  └──  UserVerifiyEmailDto
│   │   │   │  │  ├──  exception
│   │   │   │  │  │  ├──  UserAlreadySignInConflictException
│   │   │   │  │  │  ├──  UserEmailAlreadyExistConflictException
│   │   │   │  │  │  ├──  UserEmailNotExistBadReqeuestException
│   │   │   │  │  │  ├──  UserNotSignInedConflictException
│   │   │   │  │  │  ├──  UserPasswordFailBadRequestException
│   │   │   │  │  │  ├──  UserSignUpBadRequestException
│   │   │   │  │  │  ├──  UserUnauthorizedException
│   │   │   │  │  │  ├──  UserVerificationEmailBadReqeust
│   │   │   │  │  │  ├──  UserVerificationIdBadRequestException
│   │   │   │  │  │  ├──  UserVerificationNumberBadRequestException
│   │   │   │  │  │  ├──  UserVerificationUnautorizedExcpetion
│   │   │   │  │  │  └──  UserVerifyUserEmailException
│   │   │   │  │  ├──  repository
│   │   │   │  │  │  ├──  UserCustomRepository
│   │   │   │  │  │  ├──  UserCustomRepositoryImpl
│   │   │   │  │  │  ├──  UserRepository
│   │   │   │  │  │  └──  UserVerificationRepository
│   │   │   │  │  ├──  service
│   │   │   │  │  │  └──  UserService
│   │   │   │  └──  PaymentSystemApplication
├── resources
│   ├── templates
│   │  └──  mail.html
│   ├── application.yml
│   ├── log4j2-local.xml
└   └── log4j2-prod.xml
```


<!-- summary -->

</details>




<br>


### 📌 api & db 설계

:clipboard:[api설계](https://closed-glade-095.notion.site/flab-d83ee2e4bd5d4f0cb3645f597ec53f2f)

- db 설계

![image](https://github.com/f-lab-edu/payment-system/assets/98700133/772ffa43-9b4b-4cb7-89c2-76b15b6fc616)

![image](https://github.com/f-lab-edu/payment-system/assets/98700133/497b2954-082b-4fc4-a7e0-8253b75d3b00)

결제에 대한 공통적인 정보는 payment 테이블에 저장하고, pg사 별로 달라지는 정보들은 따로 테이블을 만들어줬습니다.

<br>


### 📌 개발 스택
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
  
3) 단건 결제 / 결제 취소 / 단건 주문 조회
- 카카오, 토스 pg사 연동
- enum 값으로 pg사 식별
- 전략패턴을 이용해서 하나의 인터페이스 및 로직으로 여러 pg사 api 사용할 수 있도록 함 
- 테스트 키 사용해서 실제 결제는 일어나지 않도록 함

<br>

4) 주문
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

### 📌 개선하고 싶은점

<br>


### 📌 문제사항과 해결방안


<br>

### 📌 회고

