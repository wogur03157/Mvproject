package com.mever.api.domain.sales.dto;


import com.mever.api.domain.sales.entity.SalesMember;
import com.mever.api.domain.sales.entity.SalesBenefit;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaleMemberDto {
    private Long id;
    private String userId;
    private String password;
    private String userNm;
    private String userPh;
    private LocalDateTime crtDt;
    private Long recommender;
    private String recommenderId;
    private String referrer1;
    private String referrer2;
    private String referrer3;
    private Long type;
    private String franchiseFee;
    private String productionCost;

    public static SalesMember toMemberEntity(SaleMemberDto saleMemberDto) {
        SalesMember member = new SalesMember();
        member.setUserId(saleMemberDto.getUserId());
        member.setPassword(saleMemberDto.getPassword());
        member.setUserNm(saleMemberDto.getUserNm());
        member.setUserPh(saleMemberDto.getUserPh());
        member.setReferrer1(saleMemberDto.getReferrer1());
        member.setReferrer2(saleMemberDto.getReferrer2());
        member.setReferrer3(saleMemberDto.getReferrer3());
        member.setRecommender(saleMemberDto.getRecommender());
        member.setFranchiseFee(saleMemberDto.getFranchiseFee());
        member.setProductionCost(saleMemberDto.getProductionCost());
        return member;
    }
}