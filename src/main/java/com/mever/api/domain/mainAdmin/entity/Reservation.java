package com.mever.api.domain.mainAdmin.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq", nullable = false, unique = true)
    private Long seq;
    @Column(name = "email",nullable = true)
    String email;
    @Column(name = "phone",nullable = true)
    String phone;
    @Column(name = "reser_price",nullable = true)
    String reserPrice;
    @Column(name = "order_id",nullable = true)
    String orderId;
    @Column(name = "category",nullable = true)
    String category;
    @Column(name = "memo",nullable = true)
    String memo;
    @Column(name = "insert_date",nullable = true)
    String insertDate;
    @Column(name = "reservation_date",nullable = true)
    String reservationDate;
    @Column(name = "status",nullable = true)
    String status;
    @Column(name = "update_date",nullable = true)
    String updateDate;
}

