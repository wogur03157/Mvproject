package com.mever.api.domain.mainAdmin.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "main_title")
public class MainTitle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq", nullable = false, unique = true)
    int seq;
    @Column(name = "title",nullable = true)
    String title;
    @Column(name = "sub_title",nullable = true)
    String subTitle;
    @Column(name = "category",nullable = true)
    String category;
    @Column(name = "update_date",nullable = true)
    String updateDate;
    @Column(name = "insert_date",nullable = true)
    String insertDate;
    @Column(name = "temp1",nullable = true)
    String temp1;
}

