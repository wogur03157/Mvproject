package com.mever.api.domain.analytics;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.google.api.services.analyticsreporting.v4.AnalyticsReportingScopes;

import com.google.api.services.analyticsreporting.v4.model.*;
import com.mever.api.domain.analytics.dto.AnalyticsDto;
import com.mever.api.domain.analytics.entity.Analytics;
import com.mever.api.domain.analytics.repository.AnalyticsRepository;
import com.mever.api.domain.mainAdmin.entity.Menu;
import com.mever.api.domain.mainAdmin.repository.MainMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsReporting {

    @Autowired
    private final AnalyticsRepository analyticsRepository;
    private static final String APPLICATION_NAME = "Hello Analytics Reporting";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String KEY_FILE_LOCATION = "C:\\Users\\PC\\IdeaProjects\\mever\\src\\main\\java\\com\\mever\\api\\domain\\analytics\\client_secrets.json";
    private static final String VIEW_ID = "268763310";

    private final MainMapper mainMapper;


    public AnalyticsDto modelRate(){
        analyticReport("/modelhouse1/");
        AnalyticsDto analyticsDto;
        analyticsDto=mainMapper.getRate();
//        System.out.println(analyticsDto);
        return analyticsDto;
    }
    public List<AnalyticsDto> analyticReport(String category) {
        List<AnalyticsDto> analyticsList = null;
        try {
            com.google.api.services.analyticsreporting.v4.AnalyticsReporting service = initializeAnalyticsReporting();
            GetReportsResponse response = getReport(service);
//            GetReportsResponse response = getTodayUser(service);
//            GetReportsResponse response = getBrowser(service);
            analyticsList = printResponse(response);
            for (AnalyticsDto analyticsDto : analyticsList) {
                Optional<Analytics> existingAnalytics = analyticsRepository.findFirstByPathUrlOrderBySeqDesc(analyticsDto.getPathUrl());
                if (existingAnalytics.isPresent()) {
                    Analytics analytics = existingAnalytics.get();
                    analytics.setBrowser(analyticsDto.getBrowser());
                    analytics.setCountry(analyticsDto.getCountry());
                    analytics.setTotalUsers(analyticsDto.getUsers());
                    analytics.setNewUsers(analyticsDto.getNewUsers());
                    analytics.setGaDate(analyticsDto.getRegDate());
                    analytics.setStartDate(analyticsDto.getStartYmd());
                    analytics.setEndDate(analyticsDto.getEndYmd());
                    analytics.setUpdateDate(analyticsDto.getUpdateDate());
                    analytics.setPageTitle(analyticsDto.getPageTitle());
                    analytics.setChannel(analyticsDto.getChannel());
                    analytics.setOneDayUsers(analyticsDto.getOneDayUsers());
                    analytics.setTotalPageview(analyticsDto.getTotalPageview());
                    analytics.setDimension(analyticsDto.getDimension());
                    analytics.setRegDate(analyticsDto.getRegDate());
                    analytics.setSessions(analyticsDto.getSessions());
                    analytics.setPercentNewSessions(analyticsDto.getPercentNewSessions());
                    analytics.setAvgSessions(analyticsDto.getAvgSessions());
                    analytics.setExitRate(analyticsDto.getExitRate());

                    analyticsRepository.save(analytics);
                } else {
                    Analytics analytics = analyticsDto.toAnalData();
                    analyticsRepository.save(analytics);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return analyticsList;
    }

    /**
     * Initializes an Analytics Reporting API V4 service object.
     *
     * @return An authorized Analytics Reporting API V4 service object.
     * @throws IOException
     * @throws GeneralSecurityException
     */
    private static com.google.api.services.analyticsreporting.v4.AnalyticsReporting initializeAnalyticsReporting() throws GeneralSecurityException, IOException {

        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GoogleCredential credential = GoogleCredential
                .fromStream(new FileInputStream(KEY_FILE_LOCATION))
                .createScoped(AnalyticsReportingScopes.all());

        // Construct the Analytics Reporting service object.
        return new com.google.api.services.analyticsreporting.v4.AnalyticsReporting.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME).build();
    }

    /**
     * Queries the Analytics Reporting API V4.
     *
     * @param service An authorized Analytics Reporting API V4 service object.
     * @return GetReportResponse The Analytics Reporting API V4 response.
     * @throws IOException
     */
    private static GetReportsResponse getReport(com.google.api.services.analyticsreporting.v4.AnalyticsReporting service) throws IOException {
        // Create the DateRange object.
        LocalDate currentDate = LocalDate.now(); // 현재 날짜 가져오기
        String oneMonthAgo = String.valueOf(currentDate.minus(Period.ofMonths(1))); // 한 달 전 날짜 계산
        DateRange dateRange = new DateRange();
        dateRange.setStartDate(String.valueOf("30daysAgo"));
        dateRange.setEndDate("today");

        //Dimension별 조사가 이뤄진다. ex) 페이지타이틀별,성별별,브라우저별
        Dimension pageTitle = new Dimension().setName("ga:pageTitle");
        Dimension pathUrl = new Dimension().setName("ga:pagePath");


        // Metrics(조회할 컬럼) 객체 생성
        Metric sessions = new Metric()
                .setExpression("ga:sessions")
                .setAlias("sessions");

        Metric newUsers = new Metric().setExpression("ga:newUsers").setAlias("newUsers");
        Metric users = new Metric().setExpression("ga:users").setAlias("users");
        Metric sessionDuration = new Metric().setExpression("ga:avgSessionDuration").setAlias("sessionDuration");
        Metric pageviews = new Metric().setExpression("ga:pageviews").setAlias("pageviews");
        Metric hits = new Metric().setExpression("ga:hits").setAlias("hits");
        Metric percentNewSessions = new Metric().setExpression("ga:percentNewSessions").setAlias("percentNewSessions");
        Metric exitRate = new Metric().setExpression("ga:exits").setAlias("exitRate");

        // 여러 Metrics를 사용할 경우
        List<Metric> MetricList = new ArrayList<>();
        MetricList.add(pageviews);
        MetricList.add(newUsers);
        MetricList.add(users);
        MetricList.add(sessionDuration);
        MetricList.add(hits);
        MetricList.add(percentNewSessions);
        MetricList.add(exitRate);

        List<OrderBy> orderBys = new ArrayList<>();
        OrderBy orderBy = new OrderBy().setFieldName("pageviews")
                .setSortOrder("ascending");
        orderBys.add(orderBy);

        // Create the ReportRequest object.
        ReportRequest request = new ReportRequest()
                .setViewId(VIEW_ID)
                .setDateRanges(Arrays.asList(dateRange))
                .setMetrics(Arrays.asList(sessions))
                .setMetrics(MetricList)
                .setDimensions(Arrays.asList(pathUrl));
//                .setOrderBys(orderBys);

        ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
        requests.add(request);

        // Create the GetReportsRequest object.
        GetReportsRequest getReport = new GetReportsRequest()
                .setReportRequests(requests);

        // Call the batchGet method.
        GetReportsResponse response = service.reports().batchGet(getReport).execute();

        // Return the response.
        return response;
    }
    private GetReportsResponse getBrowser(com.google.api.services.analyticsreporting.v4.AnalyticsReporting service) throws IOException {
        DateRange dateRange = new DateRange();
        dateRange.setStartDate("30DaysAgo");
        dateRange.setEndDate("today");

        Dimension browser = new Dimension().setName("ga:deviceCategory");
        Dimension pathUrl = new Dimension().setName("ga:pagePath");


        // Metrics(조회할 컬럼) 객체 생성
        Metric sessions = new Metric()
                .setExpression("ga:sessions")
                .setAlias("sessions");

        // 여러 Metrics를 사용할 경우
        List<Metric> MetricList = new ArrayList<>();

        List<OrderBy> orderBys = new ArrayList<>();
        OrderBy orderBy = new OrderBy().setFieldName("pageviews")
                .setSortOrder("ascending");
        orderBys.add(orderBy);

        // Create the ReportRequest object.
        ReportRequest request = new ReportRequest()
                .setViewId(VIEW_ID)
                .setDateRanges(Arrays.asList(dateRange))
                .setMetrics(Arrays.asList(sessions))
                .setMetrics(MetricList)
                .setDimensions(Arrays.asList(browser
                        ,pathUrl
                ));

        ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
        requests.add(request);

        // Create the GetReportsRequest object.
        GetReportsRequest getReport = new GetReportsRequest()
                .setReportRequests(requests);

        // Call the batchGet method.
        GetReportsResponse response = service.reports().batchGet(getReport).execute();
        return response;
    }
    private GetReportsResponse getTodayUser(com.google.api.services.analyticsreporting.v4.AnalyticsReporting service) throws IOException {
        DateRange dateRange = new DateRange();
        dateRange.setStartDate("30DaysAgo");
        dateRange.setEndDate("today");
        Dimension gaDate = new Dimension().setName("ga:date");
        Dimension pathUrl = new Dimension().setName("ga:pagePath");


        // Metrics(조회할 컬럼) 객체 생성
        Metric sessions = new Metric()
                .setExpression("ga:sessions")
                .setAlias("sessions");

        // 여러 Metrics를 사용할 경우
        List<Metric> MetricList = new ArrayList<>();

        List<OrderBy> orderBys = new ArrayList<>();
        OrderBy orderBy = new OrderBy().setFieldName("pageviews")
                .setSortOrder("ascending");
        orderBys.add(orderBy);

        // Create the ReportRequest object.
        ReportRequest request = new ReportRequest()
                .setViewId(VIEW_ID)
                .setDateRanges(Arrays.asList(dateRange))
                .setMetrics(Arrays.asList(sessions))
                .setMetrics(MetricList)
                .setDimensions(Arrays.asList(gaDate
                        ,pathUrl
                ));

        ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
        requests.add(request);

        // Create the GetReportsRequest object.
        GetReportsRequest getReport = new GetReportsRequest()
                .setReportRequests(requests);

        // Call the batchGet method.
        GetReportsResponse response = service.reports().batchGet(getReport).execute();
        return response;
    }

    /**
     * Parses and prints the Analytics Reporting API V4 response.
     *
     * @param response An Analytics Reporting API V4 response.
     */
    @Transactional
    public List<AnalyticsDto> printResponse(GetReportsResponse response) {
        List<AnalyticsDto> analyticsList = new ArrayList<>();

        for (Report report: response.getReports()) {
            ColumnHeader header = report.getColumnHeader();
            List<String> dimensionHeaders = header.getDimensions();
            List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
            List<ReportRow> rows = report.getData().getRows();

            if (rows == null) {
                System.out.println("No data found for " + VIEW_ID);
                return analyticsList;
            }

            for (ReportRow row: rows) {
                List<String> dimensions = row.getDimensions();
                List<DateRangeValues> metrics = row.getMetrics();
                AnalyticsDto analyticsDto = new AnalyticsDto();

                for (int i = 0; i < dimensionHeaders.size() && i < dimensions.size(); i++) {
                    String dimensionHeader = dimensionHeaders.get(i);
                    String dimensionValue = dimensions.get(i);
                    System.out.println("디맨션 - "+dimensionHeaders.get(i) + ": " + dimensions.get(i));
                    if (dimensionHeader.equals("dimension")) {
                        analyticsDto.setDimension(dimensionValue);
                    } else if (dimensionHeader.equals("ga:pageTitle")) {
                        analyticsDto.setPageTitle(dimensionValue);
                    } else if (dimensionHeader.equals("ga:browser")) {
                        analyticsDto.setBrowser(dimensionValue);
                    } else if (dimensionHeader.equals("ga:country")) {
                        analyticsDto.setCountry(dimensionValue);
                    } else if (dimensionHeader.equals("ga:date")) {
                        analyticsDto.setRegDate(dimensionValue);
                    } else if (dimensionHeader.equals("ga:pagePath")) {
                        analyticsDto.setPathUrl(dimensionValue);
                    } else if (dimensionHeader.equals("ga:deviceCategory")) {
                        analyticsDto.setBrowser(dimensionValue);
                    }
                }
                for (int j = 0; j < metrics.size(); j++) {
                    System.out.print("Date Range (" + j + "): ");
                    DateRangeValues values = metrics.get(j);
                    for (int k = 0; k < values.getValues().size() && k < metricHeaders.size(); k++) {
                        MetricHeaderEntry metricHeaderEntry = metricHeaders.get(k);
                        String metricName = metricHeaderEntry.getName();
                        String metricValue = values.getValues().get(k);
                        if (metricName.equals("pageviews")) {
                            analyticsDto.setTotalPageview(metricValue);
                        } else if (metricName.equals("users")) {
                            analyticsDto.setUsers(metricValue);
                        } else if (metricName.equals("sessions")) {
                            analyticsDto.setSessions(metricValue);
                        } else if (metricName.equals("newUsers")) {
                            analyticsDto.setNewUsers(metricValue);
                        }else if (metricName.equals("percentNewSessions")) {
                            analyticsDto.setPercentNewSessions(metricValue);
                        }else if (metricName.equals("ga:visits")) {
                            analyticsDto.setOneDayUsers(metricValue);
                        }else if (metricName.equals("ga:exits")) {
                            analyticsDto.setExitRate(metricValue);
                        }
                        if (metricName.equals("sessionDuration")) {
                            double durationInSeconds = Double.parseDouble(metricValue);
                            int minutes = (int) (durationInSeconds / 60);
                            int seconds = (int) (durationInSeconds % 60);
                            String avgSession = minutes+"분"+seconds+"초";
                            analyticsDto.setAvgSessions(avgSession);
                            System.out.println("평균 페이지 머문 시간: " + minutes + "분 " + seconds + "초");
                        } else {
                            System.out.println("매트릭 - "+metricName + ": " + metricValue);
                        }
                    }
                    LocalDate currentDate = LocalDate.now();
                    LocalDate sevenDaysAgo = currentDate.minusDays(7);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String currentDateString = currentDate.format(formatter);
                    String sevenDaysAgoString = sevenDaysAgo.format(formatter);
                    analyticsDto.setStartYmd(sevenDaysAgoString);
                    analyticsDto.setEndYmd(currentDateString);
                    analyticsList.add(analyticsDto);
                }
            }
        }

        return  analyticsList;
    }

}