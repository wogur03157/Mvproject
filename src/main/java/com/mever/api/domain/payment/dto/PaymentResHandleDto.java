package com.mever.api.domain.payment.dto;

import com.mever.api.domain.payment.entity.CancelOrder;
import com.mever.api.domain.payment.entity.Orders;
import com.mever.api.domain.payment.entity.Subscription;
import io.swagger.annotations.ApiModelProperty;
import jdk.jfr.SettingDefinition;
import lombok.*;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResHandleDto {
    @ApiModelProperty("가맹점 ID")
    private String mId;
    @ApiModelProperty("객체 응답 버전")
    private String version;                 // : "1.3", Payment
    @ApiModelProperty("키")
    private String paymentKey;              // : "5zJ4xY7m0kODnyRpQWGrN2xqGlNvLrKwv1M9ENjbeoPaZdL6",
    @ApiModelProperty("주문아이디")
    private String orderId;                 // : "IBboL1BJjaYHW6FA4nRjm",
    @ApiModelProperty("상품명")
    private String orderName;               // : "토스 티셔츠 외 2건",
    @ApiModelProperty("KRW")
    private String currency;                // : "",
    @ApiModelProperty("결제수단")
    private String method;                  // : "카드", 결제수단
    @ApiModelProperty("")
    private String totalAmount;             // : 15000,
    @ApiModelProperty("")
    private String balanceAmount;           // : 15000,
    @ApiModelProperty("")
    private String suppliedAmount;          // : 13636,
    @ApiModelProperty("")
    private String vat;                     // : 1364,
    @ApiModelProperty("결제 처리 상태")
    private String status;                  // : "DONE",
    @ApiModelProperty("")
    private String requestedAt;             // : "2021-01-01T10:01:30+09:00",
    @ApiModelProperty("")
    private String approvedAt;              // : "2021-01-01T10:05:40+09:00",
    @ApiModelProperty("")
    private String useEscrow;               // : false,
    @ApiModelProperty("")
    private String cultureExpense;          // : false,
    @ApiModelProperty("")
    private String type;                    // : "NORMAL",	결제 타입 정보 (NOMAL, BILLING, CONNECTPAY)
    private String company;                     // "현대",
    private String number;
    private String email;
    private String phone;
    private String name;
    private String survey;
    private String dcrp;
    private String regdate;
    private String successUrl;		// 성공시 콜백 주소
    private String failUrl;			// 실패시 콜백 주소
    CancelOrderDto[] cancels; // : 결제 취소 이력 관련 객체
    private String receiptUrl;
//    @ApiModelProperty("자동결제 키")
    private String billingKey;
    //    @ApiModelProperty("고객 ID")
    private String customerKey;
    //    @ApiModelProperty("결제 receipt")
    private Object receipt;
    //    @ApiModelProperty("결제 chekout")
    private Object chekout;
    //    @ApiModelProperty("결제 주기")
    private String period;
    private String category;
    public Orders toOrderBuilder() {
        return Orders.builder()
                .orderId(UUID.randomUUID().toString())
                .orderName(orderName)
                .email(email)
                .name(name)
                .totalAmount(Long.valueOf(totalAmount))
                .build();
    }

    public Orders toAutoOrderBuilder() {
        return Orders.builder()
                .orderId(UUID.randomUUID().toString())
                .customerKey(UUID.randomUUID().toString())
                .orderName(orderName)
                .email(email)
                .name(name)
                .totalAmount(Long.valueOf(totalAmount))
                .period(period)
                .build();
    }
    public Subscription subEntity(Subscription subscription){
        subscription.setCustomerKey(customerKey);
        subscription.setBillingKey(billingKey);
        subscription.setOrderId(orderId);
        subscription.setAmount(totalAmount);
        subscription.setStatus(status);
        subscription.setPeriod(period);
        subscription.setStartDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        subscription.setCreatedAt(requestedAt);
        return subscription;
    }
    public Orders autoOrderEntity(Orders orders) {
        orders.setBalanceAmount(Long.valueOf(balanceAmount));
        orders.setCompany(company);
        orders.setCultureExpense(cultureExpense);
        orders.setCurrency(currency);
        orders.setMId(mId);
        orders.setMethod(method);
        orders.setNumber(number);
        orders.setOrderId(orderId);
        orders.setOrderName(orderName);
        orders.setPaymentKey(paymentKey);
        orders.setRequestedAt(requestedAt);
        orders.setApprovedAt(approvedAt);
        orders.setStatus(status);
        orders.setSuppliedAmount(Long.valueOf(suppliedAmount));
        orders.setTotalAmount(Long.valueOf(totalAmount));
        orders.setType(type);
        orders.setUseEscrow(useEscrow);
        orders.setVat(Long.valueOf(vat));
        orders.setVersion(version);
        orders.setBillingKey(billingKey);
        orders.setReceipt(receipt);
        orders.setChekout(chekout);
        return orders;
    }
    public Orders toOrderEntity(Orders orders) {
        orders.setBalanceAmount(Long.valueOf(balanceAmount));
        orders.setCompany(company);
        orders.setCultureExpense(cultureExpense);
        orders.setCurrency(currency);
        orders.setMId(mId);
        orders.setMethod(method);
        orders.setNumber(number);
        orders.setOrderId(orderId);
        orders.setOrderName(orderName);
        orders.setPaymentKey(paymentKey);
        orders.setRequestedAt(requestedAt);
        orders.setApprovedAt(approvedAt);
        orders.setStatus(status);
        orders.setSuppliedAmount(Long.valueOf(suppliedAmount));
        orders.setTotalAmount(Long.valueOf(totalAmount));
        orders.setType(type);
        orders.setUseEscrow(useEscrow);
        orders.setVat(Long.valueOf(vat));
        orders.setVersion(version);

        return orders;
    }

    public CancelOrder toCancelOrder(Orders orders) {
        return CancelOrder.builder()
                .email(email)
                .orderId(orderId)
                .orderName(orderName)
                .paymentKey(paymentKey)
                .requestedAt(requestedAt)
                .approvedAt(approvedAt)
                .company(orders.getCompany())
                .number(orders.getNumber())
                .receiptUrl(receiptUrl)
                .cancelAmount(cancels[0].getCancelAmount())
                .cancelReason(cancels[0].getCancelReason())
                .cancelDate(cancels[0].getCanceledAt())
                .build();
    }
}