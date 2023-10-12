package com.mever.api.domain.sales.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sales_fee")
@DynamicInsert
@DynamicUpdate
public class SalesFee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
   @Column
    private String franchiseFee;
   @Column
    private String productionCost;
   @Column
    private String referrerFee;

}