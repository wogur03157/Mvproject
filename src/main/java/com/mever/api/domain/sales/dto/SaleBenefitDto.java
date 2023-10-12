package com.mever.api.domain.sales.dto;


import com.mever.api.domain.sales.entity.SalesBenefit;
import com.mever.api.domain.sales.entity.SalesMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaleBenefitDto {
    private Long id;
    private String userId;
    private Long recommenderId;
    private String amount;
    private String benefit;
    private String calculateCheck;
    private String crtDt;
    private String type;


    public static SalesBenefit toBenefitEntity(SaleBenefitDto saleBenefitdto) {
        SalesBenefit benefit = new SalesBenefit();
        benefit.setUserId(saleBenefitdto.getUserId());
        benefit.setRecommenderId(saleBenefitdto.getRecommenderId());
        benefit.setAmount(saleBenefitdto.getAmount());
        benefit.setCalculateCheck(saleBenefitdto.getCalculateCheck());
        benefit.setType(saleBenefitdto.getType());
        return benefit;
    }
}