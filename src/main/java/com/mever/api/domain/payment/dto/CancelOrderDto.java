package com.mever.api.domain.payment.dto;

import com.mever.api.domain.payment.entity.CancelOrder;
import com.mever.api.domain.payment.entity.Orders;
import jakarta.persistence.Column;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CancelOrderDto {
    private Long seq;
    private String email;
    private String paymentKey;              // : "5zJ4xY7m0kODnyRpQWGrN2xqGlNvLrKwv1M9ENjbeoPaZdL6",
    private String orderId;                 // : 주문아이디,
    private String orderName;               // : "토스 티셔츠 외 2건",
    private String receiptUrl;
    private String cancelDate;            // : 취소날짜
    private String cancelReason;            // : 취소사유
    private Long cancelAmount;             // : 15000,
    private String requestedAt;             // : "2021-01-01T10:01:30+09:00",
    private String approvedAt;              // : "2021-01-01T10:05:40+09:00",
    private String number;                      // "433012******1234",
    private String company;                     // "현대"
    private String canceledAt ;
}