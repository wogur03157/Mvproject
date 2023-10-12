package com.mever.api.domain.analytics.controller;

import com.mever.api.domain.analytics.AnalyticsReporting;
import com.mever.api.domain.analytics.dto.AnalyticsDto;
import com.mever.api.domain.mainAdmin.entity.Menu;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.SimpleTimeZone;
import java.util.stream.Collectors;

@RestController
public class AnalyticsController {
    @Autowired
    AnalyticsReporting analyticsReporting;

    @PostMapping("/analyticsList")
    public ResponseEntity<List> analytics(
            @ApiParam(value = "소속 그룹", required = true) @RequestParam String category){
        List<AnalyticsDto> analyticsList = analyticsReporting.analyticReport(category);
        List<AnalyticsDto> filteredList;
        filteredList = analyticsList.stream()
                .filter(dto -> dto.getPathUrl().equals(category))
                .collect(Collectors.toList());

        return ResponseEntity.ok(filteredList);
    }
    @PostMapping("/getRate")
    public ResponseEntity<AnalyticsDto> getRate(){
        AnalyticsDto rate = analyticsReporting.modelRate();
        System.out.println(rate);
        return ResponseEntity.ok(rate);
    }
}
