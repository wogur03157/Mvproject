package com.mever.api.domain.member.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LotteryDto {
    private String email;
    private int weight;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LotteryResult {
        private String email;
        private int rank;
        private String prize;
        // Getters and setters
    }


}