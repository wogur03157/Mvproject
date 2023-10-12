package com.mever.api.domain.payment.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResHandleCardDto {
    @ApiModelProperty("")
    String company;                     // "현대",
    String number;                      // "433012******1234",
    String installmentPlanMonths;       // 0,
    String isInterestFree;              // false,
    String approveNo;                   // "00000000",
    String useCardPoint;                // false,
    String cardType;                    // "신용",
    String ownerType;                   // "개인",
    String acquireStatus;               // "READY",
    String receiptUrl;   // : "NORMAL",	결제 타입 정보 (NOMAL, BILLING, CONNECTPAY)

}