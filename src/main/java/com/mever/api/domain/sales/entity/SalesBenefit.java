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
@Table(name = "sales_benefit")
@DynamicInsert
@DynamicUpdate
public class SalesBenefit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
   @Column
    private String userId;
   @Column
    private Long recommenderId;
   @Column
    private String amount;
   @Column
    private String calculateCheck;
   @Column
    private String crtDt;

    @Column
    private String type;
}