package com.mever.api.domain.playList.dto;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayListDto {
    private Long seq;
    private String videoId;
    private String trackTitle;
    private String homePage;
}