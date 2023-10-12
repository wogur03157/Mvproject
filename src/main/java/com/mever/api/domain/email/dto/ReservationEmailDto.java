package com.mever.api.domain.email.dto;

import com.mever.api.domain.email.entity.ReservationMail;
import com.mever.api.domain.email.entity.SendHistory;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationEmailDto {
    private Long seq;
    private String sendDate;
    private String email;
    private String title;
    private String date;
    private String content;
    private String period;
    private String phone;
    private String afterDay;
    private String mailtitle;
    private String mailcontent;

    public ReservationMail toReservationMailBuilder() {
        return ReservationMail.builder()
                .seq(seq)
                .email(email)
                .sendDate(sendDate)
                .title(title)
                .content(content)
                .period(period)
                .build();
    }
}