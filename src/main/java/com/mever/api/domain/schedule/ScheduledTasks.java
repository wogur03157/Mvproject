package com.mever.api.domain.schedule;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mever.api.domain.email.dto.EmailDto;
import com.mever.api.domain.email.dto.ReservationEmailDto;
import com.mever.api.domain.email.dto.SmsDto;
import com.mever.api.domain.email.repository.EmailMapper;
import com.mever.api.domain.email.service.SendService;
import com.mever.api.domain.member.dto.MemberRes;
import com.mever.api.domain.member.entity.Member;
import com.mever.api.domain.member.repository.MemberMapper;
import com.mever.api.domain.member.repository.MemberRepository;
import com.mever.api.domain.payment.dto.PaymentResHandleDto;
import com.mever.api.domain.payment.entity.Orders;
import com.mever.api.domain.payment.entity.Subscription;
import com.mever.api.domain.payment.repository.OrderRepository;
import com.mever.api.domain.payment.repository.SubscriptionRepository;
import com.mever.api.domain.payment.service.PaymentService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.io.DataInput;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledTasks {

    @Autowired
    private SendService sendService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private EmailMapper emailMapper;
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private PaymentService paymentService;
    private final OrderRepository orderRepository;
    private String autoPayRequest = "https://api.tosspayments.com/v1/billing/";

    @Value("${payments.toss.test_secret_api_key}")
    private String testSecretApiKey;

    @Scheduled(cron = "0 00 9 * * ?")
    public void cronMailSend() throws MessagingException, UnsupportedEncodingException {
        List<ReservationEmailDto> emailRes =emailMapper.getEmailList();

        for(int i =0; i<emailRes.size();i++){
            ReservationEmailDto reservationEmailDto=ReservationEmailDto.builder()
                    .seq(emailRes.get(i).getSeq())
                    .email(emailRes.get(i).getEmail())
                    .sendDate(emailRes.get(i).getSendDate())
                    .title(String.valueOf(emailRes.get(i).getTitle()))
                    .content(String.valueOf(emailRes.get(i).getContent()))
                    .phone(String.valueOf(emailRes.get(i).getPhone()))
                    .period(String.valueOf(emailRes.get(i).getPeriod()))
                    .mailcontent(String.valueOf(emailRes.get(i).getMailcontent()))
                    .mailtitle(String.valueOf(emailRes.get(i).getMailtitle()))
                    .build();
            sendService.schedulEmail(reservationEmailDto);
        }
    }
    // 매일 12시 0분 0초에 실행
//    @Scheduled(cron = "0 0 12 * * ?")
    public void cronBilling() throws IOException {

//        // API 호출에 필요한 헤더 설정
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        testSecretApiKey = testSecretApiKey + ":";
        String encodedAuth = new String(Base64.getEncoder().encode(testSecretApiKey.getBytes(StandardCharsets.UTF_8)));
        headers.setBasicAuth(encodedAuth);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        LocalDate today = LocalDate.now();
        List<Subscription> subscriptions = subscriptionRepository.findByStatusAndCustomerKeyIsNotNullAndBillingKeyIsNotNull("ACTIVE");
        JSONObject param = new JSONObject();

        for (Subscription subscription : subscriptions) {
            String billingKey = subscription.getBillingKey();
            String customerKey = subscription.getCustomerKey();
            LocalDate paymentDate = subscription.getPaymentDate(); // "2022-06-15"
            Orders orders = orderRepository.findByCustomerKey(customerKey);
            param.put("amount", subscription.getAmount());
            param.put("customerKey", customerKey);
            param.put("orderId", subscription.getOrderId());
//            LocalDate billingDate = today;
            // 결제일이 금일인 경우 billingKey 호출 후 업데이트 및 주문 내역 쌓기
            if (paymentDate != null && !paymentDate.equals("")) {
                if (today.isEqual(paymentDate)) {
                    // 빌링키 호출
                    JSONObject resJson2 = rest.postForEntity(
                            autoPayRequest + billingKey,
                            new HttpEntity<>(param, headers),
                            JSONObject.class
                    ).getBody();
                    LinkedHashMap<String, String> receiptMap = (LinkedHashMap<String, String>) resJson2.get("receipt");
                    LinkedHashMap<String, String> checkoutMap = (LinkedHashMap<String, String>) resJson2.get("checkout");
                    String checkout = receiptMap.get("url");
                    String receipt = checkoutMap.get("url");
                    orders.setPeriod("day");
                    paymentService.autoPayPeriod(customerKey, billingKey, subscription.getPeriod());
                    String jsonString = objectMapper.writeValueAsString(resJson2);
                    PaymentResHandleDto paymentResHandleDtorder = objectMapper.readValue(jsonString, PaymentResHandleDto.class);
                    paymentResHandleDtorder.setCustomerKey(customerKey);
                    paymentResHandleDtorder.setMId(resJson2.get("mId").toString());
                    paymentResHandleDtorder.setBillingKey(billingKey);
                    paymentResHandleDtorder.setPaymentKey(resJson2.get("paymentKey").toString());
                    paymentResHandleDtorder.setChekout(checkout);
                    paymentResHandleDtorder.setReceipt(receipt);
                    paymentResHandleDtorder.setOrderName(orders.getOrderName());
                    orderRepository.save(paymentResHandleDtorder.autoOrderEntity(orders));
                }
            }
        }
    }
}
