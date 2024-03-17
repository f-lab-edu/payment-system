## 💰 payment-system

> 개인 프로젝트로 스프링 부트를 이용해 백엔드 개발 및 테스트 코드 작성, 로그 수집을 했습니다.
> 평소 돈의 흐름에 관심이 많아 금융 거래의 흐름과 결제 시스템 구조를 파악하고자 PG 사(카카오, 토스)의 결제 API를 연동한 결제 시스템을 구현했습니다.
> 기술적으로는 분산 서버 환경에서의 신뢰성 있는 결제 시스템을 제공하는 것을 목적으로 개발했습니다.


참고사항: 보안상 비밀번호는 암호화돼서 저장됩니다.

<br>

### 📌 프로젝트를 통해 얻은 기술 역량

- 신뢰성 있는 결제 시스템
  - 재고 증감 시 동시성 이슈를 처리하기 위해 Redis를 이용한 분산락 사용
  - PG 사와의 결제 정보 데이터 정합성을 보장하기 위해 배치 시스템 도입을 통한 보상 트랜잭션 구현
    
- 로그인 세션 관리: 분산 서버에서 적절한 로그인 세션 처리
  - Redis를 세션 스토리지로 한 로그인 기능 구현
    
- 대규모 트래픽 환경, 대용량 데이터 처리임을 가정
  - Redis를 이용한 캐싱
  - 커서 기반 페이지네이션 사용
    
- 유지보수성과 확장성 고려
  - 여러 PG 사의 결제 API를 전략 패턴을 활용해 하나의 로직으로 통합 처리
  - 예외 처리 및 인증 확인 로직을 비즈니스 로직과 분리하는 리팩토링함으로써 유지 보수성 및 코드 가독성 향상

<br>

### 📁 폴더 구조

<details>
    <summary> 🧷 프로젝트 구조 펼쳐보기</summary>

```bash
main
├── java
│   ├── flab.payment_system
│   │   ├──  adapter # 다른 패키지(도메인)의 클래스를 사용할 때
│   │   │   ├──  OrderAdapter
│   │   │   ├──  OrderAdapterImpl
│   │   │   ├──  PaymentAdapter
│   │   │   ├──  PaymentAdapterImpl
│   │   │   ├──  RedissonLockAdapter
│   │   │   ├──  RedissonLockAdapterImpl
│   │   │   ├──  UserAdapter
│   │   │   └──  UserAdapterImpl
│   │   ├──  common # 공통
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
│   │   ├──  config
│   │   │   ├──  AppConfig
│   │   │   ├──  QueryDslConfig
│   │   │   ├──  RedisConfig
│   │   │   └──  WebConfig
│   │   ├──  domain
│   │   │   ├──  batch
│   │   │   │  ├──  compensation # 보상 트랜잭션을 위해 주기적으로 PG 사의 엑셀 파일과 DB 를 비교해 데이터를 일치시켜주는 batch
│   │   │   │  │  ├──  CompensationConfig
│   │   │   │  │  ├──  CompensationItemProcessor
│   │   │   │  │  ├──  CompensationItemReader
│   │   │   │  │  ├──  CompensationItemWrite
│   │   │   │  │  └──  CompensationJobConfig
│   │   │   ├──  log
│   │   │   │  ├──  entity
│   │   │   │  │  └──  AppLogs
│   │   │   ├──  mail
│   │   │   │  ├──  service # 유저에게 이메일 보낼 때(회원가입을 위한 유저 인증번호 발송)
│   │   │   │  │  └──  MailService
│   │   │   ├──  order
│   │   │   │  ├──  controller
│   │   │   │  │  └──  OrderController
│   │   │   │  ├──  entity
│   │   │   │  │  └──  OrderProduct
│   │   │   │  ├──  dto
│   │   │   │  │  ├──  OrderCancelDto
│   │   │   │  │  ├──  OrderDetailDto
│   │   │   │  │  ├──  OrderDto
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
│   │   │   │  ├──  client # PG 사와 통신
│   │   │   │  │  ├──  kakao
│   │   │   │  │  │  └──  PaymentKakaoClient
│   │   │   │  │  ├──  toss
│   │   │   │  │  │  └──  PaymentTossClient
│   │   │   │  ├──  controller
│   │   │   │  │  └──  PaymentController
│   │   │   │  ├── entity
│   │   │   │  │  ├── Payment
│   │   │   │  │  │  ├──  kakao 
│   │   │   │  │  │  │  ├── Amount
│   │   │   │  │  │  │  ├── ApprovedCancelAmount
│   │   │   │  │  │  │  ├── CancelAvailableAmount
│   │   │   │  │  │  │  ├── CanceledAmount
│   │   │   │  │  │  │  ├── CardInfo
│   │   │   │  │  │  │  ├── KakaoPayment
│   │   │   │  │  │  │  └── PaymentActionDetails
│   │   │   │  │  │  ├──  toss
│   │   │   │  │  │  │  ├── Cacnels
│   │   │   │  │  │  │  ├── Card
│   │   │   │  │  │  │  ├── CashReceipt
│   │   │   │  │  │  │  ├── Checkout
│   │   │   │  │  │  │  ├── Discount
│   │   │   │  │  │  │  ├── EasyPay
│   │   │   │  │  │  │  ├── Failure
│   │   │   │  │  │  │  ├── GiftCertificate
│   │   │   │  │  │  │  ├── MobilePhone
│   │   │   │  │  │  │  ├── Receipt
│   │   │   │  │  │  │  ├── RefundReceiveAccount
│   │   │   │  │  │  │  ├── TossPayment
│   │   │   │  │  │  │  ├── Transfer
│   │   │   │  │  │  │  └── VirtualAccount
│   │   │   │  ├──  enums
│   │   │   │  │  ├── PaymentPgCompany
│   │   │   │  │  ├── PaymentPgCompanyStringToEnumConverter
│   │   │   │  │  └── PaymentStateConstant
│   │   │   │  ├──  exception
│   │   │   │  │  ├── PaymentAlreadyApprovedConflictException
│   │   │   │  │  ├── PaymentFailBadRequestException
│   │   │   │  │  ├── PaymentKakaoServiceUnavailableException
│   │   │   │  │  ├── PaymentNotApprovedConflictException
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
│   │   │   │  │  ├──  dto
│   │   │   │  │  │  └──  ProductDto
│   │   │   │  │  ├──  entity
│   │   │   │  │  │  └──  Product
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
│   │   │   │  │  ├──  dto
│   │   │   │  │  │  ├──  UserConfirmVerificationNumberDto
│   │   │   │  │  │  ├──  UserDto
│   │   │   │  │  │  ├──  UserSignUpDto
│   │   │   │  │  │  ├──  UserVerificationDto
│   │   │   │  │  │  └──  UserVerifiyEmailDto
│   │   │   │  │  ├──  entity
│   │   │   │  │  │  ├──  User
│   │   │   │  │  │  └──  UserVerification
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
│   ├── application-test.yml
│   ├── log4j2-local.xml
│   ├── log4j2-prod.xml
│   ├── payment_kakao.csv # 배치 시스템에 필요한 예시 엑셀 파일, 주기적으로 PG 사와 통신해 엑셀 파일을 가져와 DB의 데이터와 비교할 것이라 가정
└   └── payment_toss.csv
```


<!-- summary -->

</details>




<br>


### 📌 db 설계



<details>
    <summary> 🧷 펼쳐보기 </summary>

![image](https://github.com/f-lab-edu/payment-system/assets/98700133/cb8a9d60-5e2c-45c3-9f0c-b57f574a3f87)

![image](https://github.com/f-lab-edu/payment-system/assets/98700133/2caaa7d0-8863-4145-91dc-e5be1feaad79)



결제에 대한 공통적인 정보는 payment 테이블에 저장하고, pg사 별로 달라지는 정보들은 따로 테이블을 만들어줬습니다.

<!-- summary -->

</details>

<br>


### 📌 개발 스택
* for Application : `Java 17` `Spring boot` `MySQL` `JPA` `QueryDSL` `Junit5` `redis`

<br>


### 📌 주요 기능

1) 회원가입
  - 가입 시 유저 아이디 대신 이메일로 가입하도록 하고, 이메일로 인증번호를 보내서 인증하도록 하는 방식
  - redis에 인증번호 정보 저장해서 ttl 안에 인증하도록 하도록 하고, 시간이 지나면 자동으로 인증번호 정보 삭제
  - 보안을 위해 bcrypt로 암호화해서 비밀번호 저장
  
  <br>
  
  
2) 로그인
  - redis에 세션 저장한 로그인 유지 방식
  
  <br>
  
3) 단건 결제 / 결제 취소 / 단건 주문 조회
- 카카오, 토스 pg사 연동
- enum 값으로 pg사 식별
- 전략 패턴을 이용해서 하나의 인터페이스 및 로직으로 여러 pg사 api 사용할 수 있도록 함 
- 테스트 키 사용해서 실제 결제는 일어나지 않도록 함

-  단건 결제 프로세스는 아래와 같습니다.
<center><img src="https://github.com/f-lab-edu/payment-system/assets/98700133/132aa184-4321-4658-b7b9-31458e175fd3"  width="60%" height="60%"/></center>

<br>

4) 주문
- reddison 분산 락을 이용해 재고 처리를 위한 동시성 이슈 처리
- 프로젝트 확장 가능성을 고려해 설계한 api로 다양한 메뉴 관리는 따로 하지 않음

<br>

### 📌 리팩토링

> 기존에 Exception 클래스들을 정의할 때 RuntimeException을 바로 상속받게 하고 일일이 메세지와 코드를 새로 정의해 주었습니다. Exception Handler에서 핸들링할 때도 아래와 같이 각 예외 별로 처리해 주다 보니 예외를 하나 추가할 때마다 반복적인 코드가 늘어났습니다.

- 기존의 예외 핸들러 코드
```
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    // 예외 별로 핸들링 
	@ExceptionHandler(UserVerificationIdBadRequestException.class)
```
>
>
> 이를 해결하기 위해 Exception은 BaseException과 상태 코드 별로 BaseException을 상속받은 예외들을 정의해 줌으로써 hierarchy 구조로 리팩토링했습니다. 리팩토링을 할 때 초기 시간적 비용이 들어가긴 했지만, 장기적으로 봤을 때 예외를 하나 추가할 때마다 드는 반복적인 작업 시간이 줄어들었고, 아래와 같이 핸들러에서 포괄적으로 처리해 줌으로써 예외 처리가 편리해졌습니다.

- 수정된 예외 핸들러 코드

```
public class CustomExceptionHandler {
    // 해당 프로젝트의 모든 예외는 BaseException을 상속받음
	@ExceptionHandler(BaseException.class)
	public ResponseEntity<ExceptionMessage> handleBaseException(BaseException baseException) {

```


### 📌 개선하고 싶은점

<br>

> 프로젝트 기한이 부족해서 적용하고 싶은 부분을 모두 반영하지는 못했습니다. 시간이 더 주어진다면 아래와 같은 항목들을 적용하고 싶습니다.

1. pg 사에서 보관 중인 결제 정보와 해당 서버의 결제 정보가 완벽히 호환되기는 힘들 거라고 생각했습니다. 예를 들면 pg 서버에서는 성공적으로 처리된 결제지만 서버에서는 timeout 등의 문제로 실패된 결제라고 처리되는 경우가 있겠습니다. 이를 호환해 주기 위해서 주기적으로 pg 사에서 결제 정보를 파일로 받아와서 해당 서버의 db 정보와 비교해 결제 데이터를 맞춰주는 배치 시스템이 있을 것이고 이를 반영하고 싶다는 생각을 했습니다.

2. 재고 관리를 하는 데 있어서 rdb 컬럼에 재고 개수를 저장하고 redis를 이용한 분산락을 통해 재고 증감을 처리해 주었습니다. 그런데 이때, 결제 승인 작업이 완료될 때까지 락이 걸려있어서 성능이 떨어질 수 있다는 단점이 있습니다. 토큰 버킷 알고리즘을 사용하면 이를 개선할 수 있을 것입니다. rdb에 관리하는 재고 개수 컬럼을 없애고 redis에 재고 개수만큼 토큰을 채워 넣고 재고에 접근하는 유저마다 토큰을 꺼내면 락을 걸지 않고도 자연스럽게 재고관리 동시성 문제를 해결할 수 있을 것입니다.

3. RestTemplate 사용 시 커넥션 풀을 따로 설정해 주지 않고 디폴트 설정을 사용하고 있습니다. 따로 설정해 주지 않으면 요청할 때마다 커넥션이 생성돼서 소켓이 고갈되는 문제가 발생할 수 있기 때문에 적절한 커넥션 풀을 따로 설정해 주는게 좋겠다는 생각을 했습니다.


### 📌 문제사항과 해결방안


:clipboard: [문제/해결 방안 링크](https://closed-glade-095.notion.site/payment-system-4c871840fd724755a99f684302b170f2?pvs=4)



<br>

### 📝 회고록

외부 api를 사용해서 프로젝트를 진행하는 것은 처음이다 보니 문서를 이해하고 적용하는데 시간이 많이 걸려서 프로젝트를 진행하는데 기한이 부족했습니다. 다음번에는 외부 api를 사용할 일이 있다면 공통 flow를 이해한 뒤 세부적인 문서를 읽는 것이 좋겠다는 생각이 들었습니다.
결제 시스템은 계속 진행해 보고 싶은 프로젝트였는데 소원성취를 하니 뿌듯했습니다. 다음번에도 프로젝트도 즐겁게 하고 싶다는 생각이 듭니다.


