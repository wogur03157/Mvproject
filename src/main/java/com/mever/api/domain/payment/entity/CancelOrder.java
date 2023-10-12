package com.mever.api.domain.payment.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cancel_order")
public class CancelOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq", nullable = false, unique = true)
    private Long seq;
    @Column
    String email;
    @Column
    String paymentKey;              // : "5zJ4xY7m0kODnyRpQWGrN2xqGlNvLrKwv1M9ENjbeoPaZdL6",
    @Column
    String orderId;                 // : 주문아이디,
    @Column
    String orderName;               // : "토스 티셔츠 외 2건",
    @Column
    String receiptUrl;
    @Column
    String cancelReason;            // : 취소사유
    @Column
    Long cancelAmount;             // : 15000,
    @Column
    String requestedAt;             // : "2021-01-01T10:01:30+09:00",
    @Column
    String approvedAt;              // : "2021-01-01T10:05:40+09:00",
    @Column
    String number;                      // "433012******1234",
    @Column
    String company;                     // "현대",

    @Column
    String cancelDate;                     // "결제 취소가 일어난 날짜와 시간 정보",

}