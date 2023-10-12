package com.mever.api.domain.sales.entity;


import com.mever.api.domain.sales.dto.SaleMemberDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sales_users")
@DynamicInsert
@DynamicUpdate
public class SalesMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
   @Column
    private String userId;
   @Column
    private String password;
   @Column
    private String userNm;
   @Column
    private String userPh;
   @Column
    private LocalDateTime crtDt;
   @Column
    private Long recommender;
    @Column(name="referrer1_id")
    private String referrer1;
    @Column(name="referrer2_id")
    private String referrer2;
    @Column(name="referrer3_id")
    private String referrer3;
    @Column
    private String franchiseFee;
    @Column
    private String productionCost;


    private SaleMemberDto convertToDto(SalesMember salesMember) {
        SaleMemberDto memberDto = new SaleMemberDto();
        memberDto.setId(salesMember.getId());
        memberDto.setUserId(salesMember.getUserId());
        memberDto.setPassword(salesMember.getPassword());
        memberDto.setUserNm(salesMember.getUserNm());
        memberDto.setUserPh(salesMember.getUserPh());
        memberDto.setCrtDt(salesMember.getCrtDt());
        memberDto.setRecommender(salesMember.getRecommender());
        return memberDto;
    }

}