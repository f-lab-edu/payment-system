package flab.payment_system.domain.payment.response.toss;

import flab.payment_system.domain.payment.entity.toss.Cancels;
import flab.payment_system.domain.payment.entity.toss.Card;
import flab.payment_system.domain.payment.entity.toss.CashReceipt;
import flab.payment_system.domain.payment.entity.toss.CashReceipts;
import flab.payment_system.domain.payment.entity.toss.Checkout;
import flab.payment_system.domain.payment.entity.toss.Discount;
import flab.payment_system.domain.payment.entity.toss.EasyPay;
import flab.payment_system.domain.payment.entity.toss.Failure;
import flab.payment_system.domain.payment.entity.toss.GiftCertificate;
import flab.payment_system.domain.payment.entity.toss.MobilePhone;
import flab.payment_system.domain.payment.entity.toss.Receipt;
import flab.payment_system.domain.payment.entity.toss.Transfer;
import flab.payment_system.domain.payment.entity.toss.VirtualAccount;
import flab.payment_system.domain.payment.response.PaymentApprovalDto;
import flab.payment_system.domain.payment.response.PaymentCancelDto;
import flab.payment_system.domain.payment.response.PaymentOrderDetailDto;
import flab.payment_system.domain.payment.response.PaymentReadyDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentTossDtoImpl implements PaymentApprovalDto, PaymentReadyDto,
	PaymentOrderDetailDto, PaymentCancelDto, PaymentToss {

	private String version;
	private String paymentKey;
	private String type;
	private String orderId;
	private String orderName;
	private String mId;
	private String currency;
	private String method;
	private Integer totalAmount;
	private Integer balanceAmount;
	private String status;
	private String requestedAt;
	private String approvedAt;
	private boolean useEscrow;
	private String lastTransactionKey;
	private Integer suppliedAmount;
	private Integer vat;
	private boolean cultureExpense;
	private Integer taxFreeAmount;
	private Integer taxExemptionAmount;
	private Cancels[] cancels;
	private boolean isPartialCancelable;
	private Card card;
	private VirtualAccount virtualAccount;
	private String secret;
	private MobilePhone mobilePhone;
	private GiftCertificate giftCertificate;
	private Transfer transfer;
	private Receipt receipt;
	private EasyPay easyPay;
	private String country;
	private Failure failure;
	private CashReceipt cashReceipt;
	private CashReceipts[] cashReceipts;
	private Discount discount;
	private Checkout checkout;
	private Long paymentId;

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}
}
