package com.mever.api.domain.analytics.dto;

import com.mever.api.domain.analytics.entity.Analytics;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnalyticsDto {
    // 통계 시작년월일
    private String startYmd;
    // 통계 종료년월일
    private String endYmd;
    // 측정기준(성별, 브라우저별, OS별...... 접속현황, 이때 성별 이런것이 Dimension)
    private String dimension;
    // 브라우저 별
    private String browser;
    // 나라 별
    private String country;
    // 채널 별
    private String channel;
    // 측정기준값(male, female)
    private String dim_val;
    // 측정기준 페이지 타이틀
    private String pageTitle;
    // 페이지뷰
    private String totalPageview;
    // 페이지 경로
    private String pathUrl;
    // 접속사용자수
    private String users;
    // 하루 접속자 수
    private String oneDayUsers;
    // 접속 세션수
    private String sessions;
    // 페이지 접속 평균 시간
    private String avgSessions;
    // 새로운 사용자 유입 퍼센트
    private String percentNewSessions;
    // 새로운사용자수
    private String newUsers;
    // 등록일시
    private String regDate;
    // 수정일시
    private String updateDate;

    private String startDate;
    private String exitRate;
    private String rateCounting;

    public Analytics toAnalData(){
        return Analytics.builder()
                .browser(browser)
                .country(country)
                .pathUrl(pathUrl)
                .totalUsers(users)
                .newUsers(newUsers)
                .gaDate(regDate)
                .startDate(startYmd)
                .endDate(endYmd)
                .updateDate(updateDate)
                .pageTitle(pageTitle)
                .channel(channel)
                .oneDayUsers(oneDayUsers)
                .totalPageview(totalPageview)
                .dimension(dimension)
                .regDate(regDate)
                .sessions(sessions)
                .percentNewSessions(percentNewSessions)
                .avgSessions(avgSessions)
                .exitRate(exitRate)
                .build();
    }

}
