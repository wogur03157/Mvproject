package com.mever.api.domain.payment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subscription")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq", nullable = false, unique = true)
    private Long seq;
    @Column(nullable = true)
    String customerKey;             // : 고객 아이디
    @Column(nullable = true)
    String billingKey;              // : "5zJ4xY7m0kODnyRpQWGrN2xqGlNvLrKwv1M9ENjbeoPaZdL6",
    @Column(nullable = true)
    String orderId;                 // : 주문아이디,
    @Column(nullable = true)
    String amount;                  // : 정기 결제 가격
    @Column(nullable = true)
    String status;                  // : ACTIVE=구독 활성화 , INACTIVE=구독 비 활성화
    @Column(nullable = true)
    String period;                  // : day(일간결제),week(주간결제),month(월간결제),year(연간결제)
    @Column(nullable = true)
    String startDate;               // : 구독 시작 날짜
    @Column(nullable = true)
    String endDate;                 // : 구독 취소 날짜
    @Column(nullable = true)
    String createdAt;               // : "2021-01-01T10:05:40+09:00",
    @Column(nullable = true)
    String updatedAt;                // : "2021-01-01T10:05:40+09:00",
    @Column(nullable = true)
    LocalDate paymentDate;                // : "2021-01-01T10:05:40+09:00",

}