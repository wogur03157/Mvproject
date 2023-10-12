package com.mever.api.domain.analytics.entity;

import lombok.*;
import jakarta.persistence.*;
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "analytics")
public class Analytics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "browser", length = 100, nullable = true)
    private String browser;

    @Column(name = "country", length = 100, nullable = true)
    private String country;

    @Column(name = "path_url", length = 100, nullable = true)
    private String pathUrl;

    @Column(name = "total_users", length = 100, nullable = true)
    private String totalUsers;

    @Column(name = "new_users", length = 100, nullable = true)
    private String newUsers;

    @Column(name = "ga_date", length = 100, nullable = true)
    private String gaDate;

    @Column(name = "start_date", length = 100, nullable = true)
    private String startDate;

    @Column(name = "end_date", length = 100, nullable = true)
    private String endDate;

    @Column(name = "update_date", length = 100, nullable = true)
    private String updateDate;

    @Column(name = "page_title", length = 100, nullable = true)
    private String pageTitle;

    @Column(name = "channel", length = 100, nullable = true)
    private String channel;

    @Column(name = "oneday_users", length = 100, nullable = true)
    private String oneDayUsers;

    @Column(name = "total_pageview", length = 100, nullable = true)
    private String totalPageview;

    @Column(name = "dimension", nullable = true)
    private String dimension;

    @Column(name = "reg_date", nullable = true)
    private String regDate;

    @Column(name = "sessions", nullable = true)
    private String sessions;

    @Column(name = "percent_newsessions", nullable = true)
    private String percentNewSessions;

    @Column(name = "avg_sessions", nullable = true)
    private String avgSessions;

    @Column(name = "exit_rate", nullable = true)
    private String exitRate;
}
