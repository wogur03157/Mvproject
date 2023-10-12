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
public class PaymentReq {
    @ApiModelProperty("지불방법")
    private String payType;
    @ApiModelProperty("지불금액")
    private Long amount;
    @ApiModelProperty("주문 상품 이름")
    private String orderName;
    @ApiModelProperty("구매자 이메일")
    private String email;
    @ApiModelProperty("구매자 이름")
    private String name;

}