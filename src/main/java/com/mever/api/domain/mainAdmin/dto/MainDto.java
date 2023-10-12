package com.mever.api.domain.mainAdmin.dto;

import com.mever.api.domain.mainAdmin.entity.ItemContents;
import com.mever.api.domain.mainAdmin.entity.MainTitle;
import com.mever.api.domain.mainAdmin.entity.Reservation;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
public class MainDto {
    @ApiModelProperty("")
    private String title;
    @ApiModelProperty("")
    private String subTitle;
    @ApiModelProperty("")
    private String itemTitle;
    @ApiModelProperty("")
    private String category;
    @ApiModelProperty("")
    private String updateDate;
    @ApiModelProperty("")
    private String temp1;
    @ApiModelProperty("")
    private String orderName;
    @ApiModelProperty("")
    private String orderId;
    @ApiModelProperty("")
    private String contents;
    @ApiModelProperty("")
    private String photoUrl;
    @ApiModelProperty("")
    private String videoUrl;
    @ApiModelProperty("")
    private String price;
    @ApiModelProperty("")
    private String imgUrl;
    @ApiModelProperty("")
    private String email;
    @ApiModelProperty("")
    private String phone;
    @ApiModelProperty("")
    private String reserPrice;
    @ApiModelProperty("")
    private String memo;
    @ApiModelProperty("")
    private String reservationDate;
    @ApiModelProperty("")
    private String insertDate;
    @ApiModelProperty("")
    private String message;
    public MainTitle toMainTitleBuilder(){
        return MainTitle.builder()
                .title(title)
                .subTitle(subTitle)
                .category(category)
                .insertDate(insertDate)
                .build();
    }
    public ItemContents toItemBuilder(){
        return ItemContents.builder()
                .itemTitle(itemTitle)
                .orderName(orderName)
                .contents(contents)
                .category(category)
                .photoUrl(photoUrl)
                .videoUrl(videoUrl)
                .price(price)
                .imgUrl(imgUrl)
                .build();
    }
    public Reservation toReserBuilder(){
        return Reservation.builder()
                .email(email)
                .phone(phone)
                .orderId(orderId)
                .category(category)
                .memo(memo)
                .reservationDate(reservationDate)
                .reserPrice(reserPrice)
                .insertDate(insertDate)
                .build();
    }
}

