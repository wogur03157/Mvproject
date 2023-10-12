package com.mever.api.domain.payment.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mever.api.domain.email.dto.EmailDto;
import com.mever.api.domain.email.service.SendService;
import com.mever.api.domain.member.entity.Member;
import com.mever.api.domain.member.repository.MemberRepository;
import com.mever.api.domain.payment.dto.CancelOrderDto;
import com.mever.api.domain.payment.dto.PaymentResHandleDto;
import com.mever.api.domain.payment.entity.Orders;
import com.mever.api.domain.payment.entity.Subscription;
import com.mever.api.domain.payment.repository.CancelOrderRepository;
import com.mever.api.domain.payment.repository.OrderMapper;
import com.mever.api.domain.payment.repository.OrderRepository;
import com.mever.api.domain.payment.repository.SubscriptionRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final CancelOrderRepository cancelOrderRepository;
    private final MemberRepository memberRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SendService sendService;
    private final Map<String,String> mailData;

    @Value("${payments.toss.test_client_api_key}")
    private String testClientApiKey;

    @Value("${payments.toss.test_secret_api_key}")
    private String testSecretApiKey;

    private String successCallBackUrl = "/success";

    private String failCallBackUrl = "/fail";

    private String tossOriginUrl = "https://api.tosspayments.com/v1/payments/";

    private String autoPayUrl = "https://api.tosspayments.com/v1/billing/authorizations/issue/";

    private String autoPayRequest = "https://api.tosspayments.com/v1/billing/";
    @Transactional
    public PaymentResHandleDto requestPayments(PaymentResHandleDto paymentResHandleDto) throws Exception {


        /*if (amount == null || amount != 3000) {
            throw new BussinessException(ExMessage.PAYMENT_ERROR_ORDER_PRICE);
        }

        if (!payType.equals("CARD") && !payType.equals("카드")) {
            throw new BussinessException(ExMessage.PAYMENT_ERROR_ORDER_PAY_TYPE);
        }

        if (!orderName.equals(OrderNameType.상품명1.name()) &&
                !orderName.equals(OrderNameType.상품명2.name())) {
            throw new BussinessException(ExMessage.PAYMENT_ERROR_ORDER_NAME);
        }*/
        try {
            System.out.println(paymentResHandleDto);
            Member member = memberRepository.findFirstByEmailAndPhoneOrderBySeqDesc(paymentResHandleDto.getEmail(),paymentResHandleDto.getPhone());
            if (member == null){
                Member newMember = new Member();
//                member.setName(paymentResHandleDto.getName());
                newMember.setEmail(paymentResHandleDto.getEmail());
                newMember.setPhone(paymentResHandleDto.getPhone());
                newMember.setPassword(paymentResHandleDto.getPhone());
                newMember.setCategory(paymentResHandleDto.getCategory());
                memberRepository.save(newMember);
            }else {
//                member.setName(paymentResHandleDto.getName());
                memberRepository.save(member);
            }
            Orders orders = paymentResHandleDto.toOrderBuilder();
            orderRepository.save(orders);
            paymentResHandleDto.setSuccessUrl(successCallBackUrl);
            paymentResHandleDto.setFailUrl(failCallBackUrl);
            paymentResHandleDto.setOrderId(orders.getOrderId());
            return paymentResHandleDto;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    public PaymentResHandleDto requestFinalPayment(String paymentKey, String orderId, Long amount) throws Exception {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        testSecretApiKey = testSecretApiKey + ":";
        String encodedAuth = new String(Base64.getEncoder().encode(testSecretApiKey.getBytes(StandardCharsets.UTF_8)));
        headers.setBasicAuth(encodedAuth);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        JSONObject param = new JSONObject();
        param.put("orderId", orderId);
        param.put("amount", amount);
        JSONObject resJson =  rest.postForEntity(
                tossOriginUrl + paymentKey,
                new HttpEntity<>(param, headers),
                JSONObject.class
        ).getBody();
//        for(Object key : resJson.keySet()){
//            System.out.println(key + " : " + resJson.get(key));
//        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        String jsonString = objectMapper.writeValueAsString(resJson);
        PaymentResHandleDto paymentResHandleDtorder = objectMapper.readValue(jsonString,PaymentResHandleDto.class);
        paymentResHandleDtorder.setMId(resJson.get("mId").toString());

        if(resJson.get("card")!=null){
            JSONParser parser = new JSONParser();
            String card = objectMapper.writeValueAsString(resJson.get("card"));
            Object obj = parser.parse( card );
            JSONObject jsonObj = (JSONObject) obj;
            paymentResHandleDtorder.setNumber(jsonObj.get("number").toString());
//            paymentResHandleDtorder.setCompany(jsonObj.get("company").toString());
        }

        if(resJson.get("easyPay")!=null){
            JSONParser parser = new JSONParser();
            String easyPay= objectMapper.writeValueAsString(resJson.get("easyPay"));
            Object obj = parser.parse(easyPay);
            JSONObject jsontest = (JSONObject) obj;
            paymentResHandleDtorder.setCompany(jsontest.get("provider").toString());
        }
        Orders orders = orderRepository.findByOrderId(orderId);
        orderRepository.save(paymentResHandleDtorder.toOrderEntity(orders));
        mailData.put("title",orders.getEmail()+"님 ["+orders.getOrderName()+"] 상품이 결제 되었습니다.");
        mailData.put("content","["+orders.getOrderName()+"] 이 상품 "+orders.getTotalAmount()+"원 결제 되었습니다.");
        mailData.put("email",orders.getEmail());
        mailSetup(mailData);

        return paymentResHandleDtorder;
    }
    @Transactional
    public PaymentResHandleDto autoPayments(PaymentResHandleDto paymentResHandleDto) throws Exception {
        try {
            List<Subscription> subscriptions = subscriptionRepository.findByStatusAndCustomerKeyIsNotNullAndBillingKeyIsNotNull("ACTIVE");
//            for (Subscription subscription : subscriptions) {
//                System.out.println(subscription.getCustomerKey() + " : " + subscription.getBillingKey());
//                // 원하는 작업 수행
//            }
            Member member = memberRepository.findByEmail(paymentResHandleDto.getEmail()).orElse(null);
            member.setName(paymentResHandleDto.getName());
            memberRepository.save(member);
            Orders orders = paymentResHandleDto.toAutoOrderBuilder();
            orderRepository.save(orders);
            paymentResHandleDto.setSuccessUrl(successCallBackUrl);
            paymentResHandleDto.setFailUrl(failCallBackUrl);
            paymentResHandleDto.setOrderId(orders.getOrderId());
            paymentResHandleDto.setCustomerKey(orders.getCustomerKey());
            return paymentResHandleDto;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    @Transactional
    public PaymentResHandleDto autoFinalPayment(String authKey,String customerKey) throws Exception {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        //시크릿 키 인코딩
        testSecretApiKey = testSecretApiKey + ":";
        String encodedAuth = new String(Base64.getEncoder().encode(testSecretApiKey.getBytes(StandardCharsets.UTF_8)));
        headers.setBasicAuth(encodedAuth);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        //billngKey를 받기위한 호출
        JSONObject param = new JSONObject();
        param.put("customerKey", customerKey);
        param.put("authKey", authKey);
        JSONObject resJson =  rest.postForEntity(
                autoPayUrl,
                new HttpEntity<>(param, headers),
                JSONObject.class
        ).getBody();
//        //받아온 값 확인
//        for(Object key : resJson.keySet()){
//            System.out.println(key + " : " + resJson.get(key));
//        }
        String billingKey=resJson.get("billingKey").toString();

        //billingKey 승인 호출
        Orders orders = orderRepository.findByCustomerKey(customerKey);
        Subscription subscription = new Subscription();
        param.put("amount",orders.getTotalAmount());
        param.put("orderId",orders.getOrderId());
        JSONObject resJson2 =  rest.postForEntity(
                autoPayRequest+billingKey,
                new HttpEntity<>(param, headers),
                JSONObject.class
        ).getBody();

//        for(Object key : resJson2.keySet()){
//            System.out.println(key + " : " + resJson2.get(key));
//        }
        LinkedHashMap<String, String> receiptMap = (LinkedHashMap<String, String>) resJson2.get("receipt");
        LinkedHashMap<String, String> checkoutMap = (LinkedHashMap<String, String>) resJson2.get("checkout");
        String checkout = receiptMap.get("url");
        String receipt = checkoutMap.get("url");
        String period = orders.getPeriod().toString();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String jsonString = objectMapper.writeValueAsString(resJson2);
        PaymentResHandleDto paymentResHandleDtorder = objectMapper.readValue(jsonString,PaymentResHandleDto.class);

        paymentResHandleDtorder.setCustomerKey(customerKey);
        paymentResHandleDtorder.setMId(resJson2.get("mId").toString());
        paymentResHandleDtorder.setBillingKey(billingKey);
        paymentResHandleDtorder.setPaymentKey(resJson2.get("paymentKey").toString());
        paymentResHandleDtorder.setChekout(checkout);
        paymentResHandleDtorder.setReceipt(receipt);
        paymentResHandleDtorder.setOrderName(orders.getOrderName());

//카드 정보 추출
        if(resJson.get("card")!=null){
            JSONParser parser = new JSONParser();
            String card = objectMapper.writeValueAsString(resJson.get("card"));
            Object obj = parser.parse( card );
            JSONObject jsonObj = (JSONObject) obj;
            paymentResHandleDtorder.setNumber(jsonObj.get("number").toString());
//            paymentResHandleDtorder.setCompany(jsonObj.get("company").toString());
        }
        if(resJson.get("easyPay")!=null){
            JSONParser parser = new JSONParser();
            String easyPay= objectMapper.writeValueAsString(resJson.get("easyPay"));
            Object obj = parser.parse(easyPay);
            JSONObject jsontest = (JSONObject) obj;
            paymentResHandleDtorder.setCompany(jsontest.get("provider").toString());
        }
        //결제 내역 저장
        orderRepository.save(paymentResHandleDtorder.autoOrderEntity(orders));
        //구독자 목록 저장
        subscription.setOrderId(orders.getOrderId());
        paymentResHandleDtorder.setStatus("ACTIVE");
        subscription.setAmount(orders.getTotalAmount().toString());
        subscription.setCreatedAt(resJson2.get("approvedAt").toString());
        paymentResHandleDtorder.setPeriod(period);
        subscriptionRepository.save(paymentResHandleDtorder.subEntity(subscription));
        //자동 결제 날짜 계산
        LocalDate payDate = autoPayPeriod(customerKey,billingKey,period);
        //결제 메일 보내기 자동결제
        mailData.put("title",orders.getEmail()+"님 ["+orders.getOrderName()+"] 상품이 결제 되었습니다.");
        mailData.put("content","["+orders.getOrderName()+"] 이 상품 "+orders.getTotalAmount()+"원 자동 결제 되었습니다.\n"+"다음 결제일 :"+ payDate.toString());
        mailData.put("email",orders.getEmail());
        mailSetup(mailData);

        return paymentResHandleDtorder;
    }

    @Transactional
    public LocalDate autoPayPeriod(String customerKey, String billingKey, String period) {
        Subscription subscription = subscriptionRepository.findByCustomerKeyAndBillingKey(customerKey, billingKey);
        LocalDate today = LocalDate.now();
//        int paymentDay = LocalDate.parse(subscription.getStartDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).getDayOfMonth();
        LocalDate billingDate = today;
        switch (period) {
            case "day":
                billingDate = today.plusDays(1);
                break;
            case "week":
                billingDate = today.plusWeeks(1);
                break;
            case "month":
                billingDate = today.plusMonths(1);

                break;
            case "year":
                billingDate = today.plusYears(1);

                break;
            default:
                // 유효하지 않은 period 값이 들어온 경우
                break;
        }
        LocalDate formattedDate = LocalDate.parse(billingDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        subscription.setPaymentDate(formattedDate);
        subscriptionRepository.save(subscription); // 변경 내용 저장
        return formattedDate;
    }
    @Transactional
    public List<PaymentResHandleDto> subscriptionList(String email,String phone) {
        List<PaymentResHandleDto> paymentRes=orderMapper.getSubscriptionList(email,phone);
        return paymentRes;
    }
    @Transactional
    public ResponseEntity subCancel(String billingKey) {
        Subscription subscription = subscriptionRepository.findByBillingKey(billingKey);
        subscription.setStatus("INACTIVE"); // 예시로 현재 시간으로 업데이트
        subscriptionRepository.save(subscription);
//        List<PaymentResHandleDto> paymentRes=orderMapper.setSubCancel();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public List<PaymentResHandleDto> paymentList(Map<String,String> requestData) {
        List<PaymentResHandleDto> paymentRes;
        String category = requestData.get("category");
        if (category != null && !category.isEmpty()) {
            paymentRes=orderMapper.getPartPayList(category);
            return paymentRes;
        }else{
            paymentRes=orderMapper.getPayList();
            return paymentRes;
        }
    }

    @Transactional
    public  ResponseEntity cancelOrder(String paymentKey, String cancelReason) throws Exception {

        RestTemplate rest = new RestTemplate();

        URI uri = URI.create(tossOriginUrl + paymentKey + "/cancel");

        HttpHeaders headers = new HttpHeaders();
        byte[] secretKeyByte = (testSecretApiKey + ":").getBytes(StandardCharsets.UTF_8);
        headers.setBasicAuth(new String(Base64.getEncoder().encode(secretKeyByte)));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        JSONObject param = new JSONObject();
        param.put("cancelReason", cancelReason);

        PaymentResHandleDto paymentResHandleDto;
        try {
            paymentResHandleDto = rest.postForObject(
                    uri,
                    new HttpEntity<>(param, headers),
                    PaymentResHandleDto.class
            );
        } catch(Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        if (paymentResHandleDto == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        CancelOrderDto cancelOrderDto;
        System.out.println(paymentResHandleDto);
        Orders orders = orderRepository.findByPaymentKey(paymentKey);
        cancelOrderRepository.save(paymentResHandleDto.toCancelOrder(orders));
        orders.setStatus(paymentResHandleDto.getStatus());
        orderRepository.save(orders);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    public void mailSetup(Map<String,String> mailData) throws MessagingException, IOException {
        EmailDto emailDto = new EmailDto();
        emailDto.setAddress(mailData.get("email"));
        emailDto.setPhone(mailData.get("phone"));
        emailDto.setTitle(mailData.get("title"));
        emailDto.setContent(mailData.get("content"));
        sendService.sendMultipleMessage(emailDto);

    }
}
