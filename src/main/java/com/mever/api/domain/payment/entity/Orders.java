package com.mever.api.domain.payment.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq", nullable = false, unique = true)
    private Long seq;
    @Column(nullable = true)
    String mId;                     // : "tosspayments", 가맹점 ID
    @Column
    String email;
    @Column
    String name;
    @Column(nullable = true)
    String version;                 // : "1.3", Payment 객체 응답 버전
    @Column(nullable = true)
    String paymentKey;              // : "5zJ4xY7m0kODnyRpQWGrN2xqGlNvLrKwv1M9ENjbeoPaZdL6",
    @Column(nullable = true)
    String orderId;                 // : "IBboL1BJjaYHW6FA4nRjm",
    @Column(nullable = true)
    String orderName;               // : "토스 티셔츠 외 2건",
    @Column(nullable = true)
    String currency;                // : "KRW",
    @Column(nullable = true)
    String method;                  // : "카드", 결제수단
    @Column(nullable = true)
    Long totalAmount;             // : 15000,
    @Column(nullable = true)
    Long balanceAmount;           // : 15000,
    @Column(nullable = true)
    Long suppliedAmount;          // : 13636,
    @Column(nullable = true)
    Long vat;                     // : 1364,
    @Column(nullable = true)
    String status;                  // : "DONE", 결제 처리 상태
    @Column(nullable = true)
    String requestedAt;             // : "2021-01-01T10:01:30+09:00",
    @Column(nullable = true)
    String approvedAt;              // : "2021-01-01T10:05:40+09:00",
    @Column(nullable = true)
    String useEscrow;               // : false,
    @Column(nullable = true)
    String cultureExpense;          // : false,
    @Column(nullable = true)
    String type;                    // : "NORMAL",	결제 타입 정보 (NOMAL, BILLING, CONNECTPAY)
    @Column(nullable = true)
    String number;                      // "433012******1234",
    @Column(nullable = true)
    String company;                     // "현대",
    @Column(nullable = true)
    String billingKey;                     // "자동 결제 billingKey",
    @Column(nullable = true)
    String customerKey;                     // "고객 ID",
    @Column(nullable = true)
    Object receipt;                     // "결제 receipt",
    @Column(nullable = true)
    Object chekout;                     // "결제 chekout",
    @Column(nullable = true)
    Object period;                     // "결제 기간",

}