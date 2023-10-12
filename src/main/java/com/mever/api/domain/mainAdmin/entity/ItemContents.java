package com.mever.api.domain.mainAdmin.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "item_contents")
public class ItemContents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq", nullable = false, unique = true)
    private Long seq;
    @Column(name = "item_title",nullable = true)
    String itemTitle;
    @Column(name = "order_name",nullable = true)
    String orderName;
    @Column(name = "price",nullable = true)
    String price;
    @Column(name = "order_id",nullable = true)
    String orderId;
    @Column(name = "category",nullable = true)
    String category;
    @Column(name = "contents",nullable = true)
    String contents;
    @Column(name = "thumb_url",nullable = true)
    String photoUrl;
    @Column(name = "video_url",nullable = true)
    String videoUrl;
    @Column(name = "img_url",nullable = true)
    String imgUrl;
    @Column(name = "updated_date",nullable = true)
    String updateDate;

}

