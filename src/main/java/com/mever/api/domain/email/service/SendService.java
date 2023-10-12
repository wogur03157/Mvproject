package com.mever.api.domain.email.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mever.api.domain.email.dto.EmailDto;
import com.mever.api.domain.email.dto.ReservationEmailDto;
import com.mever.api.domain.email.dto.SmsDto;
import com.mever.api.domain.email.dto.SmsResponseDTO;
import com.mever.api.domain.email.entity.ReservationMail;
import com.mever.api.domain.email.entity.SendHistory;
import com.mever.api.domain.email.repository.ReservationMailRepository;
import com.mever.api.domain.email.repository.SendRepository;
import com.mever.api.domain.member.entity.Member;
import com.mever.api.domain.member.repository.MemberRepository;
import com.mever.api.domain.payment.dto.CancelOrderDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendService {
    @Value("${spring.mail.username}")
    private String FROM_ADDRESS;
//    @Value("${send.sms.userid}")
//    private String smsUserid;
//    @Value("${send.sms.userkey}")
//    private String smsUserkey;
    private final JavaMailSender emailSender;
    @Autowired
    private MemberRepository memberRepository;
    private final SendRepository sendRepository;
    private final ReservationMailRepository reservationMailRepository;
    @Value("${naver-cloud-sms.accessKey}")
    private String accessKey;
    @Value("${naver-cloud-sms.secretKey}")
    private String secretKey;
    @Value("${naver-cloud-sms.serviceId}")
    private String serviceId;
    @Value("${naver-cloud-sms.senderPhone}")
    private String phone;

    public String makeSignature(Long time) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String url = "/sms/v2/services/"+ this.serviceId+"/messages";
        String timestamp = time.toString();

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.getEncoder().encodeToString(rawHmac);
        return encodeBase64String;
    }
    @Transactional
    public Object sendNaver(SmsDto smsDto) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException, URISyntaxException {
        Long time = System.currentTimeMillis();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", accessKey);
        headers.set("x-ncp-apigw-signature-v2", makeSignature(time));
        SmsDto.MessageDTO messageDto = new SmsDto.MessageDTO();
        messageDto.setContent(smsDto.getMsg());
        messageDto.setTo(smsDto.getPhone());
        List<SmsDto.MessageDTO> messages = new ArrayList<>();
        messages.add(messageDto);

        SmsDto.SmsRequest smsRequest = SmsDto.SmsRequest.builder()
                .type("SMS")
                .from(phone) // 발신자 번호
                .to(smsDto.getPhone()) // 수신자 번호
                .content(smsDto.getMsg())
                .messages(messages)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(smsRequest);
        HttpEntity<String> httpBody = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        SmsResponseDTO response = restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/sms/v2/services/"+ serviceId +"/messages"), httpBody, SmsResponseDTO.class);
        SmsDto msgDto = SmsDto.builder()
                .msg(smsRequest.getContent())
                .email(smsDto.getEmail())
                .phone(smsRequest.getTo())
                .type("sms")
                .build();
       ResponseEntity.ok(sendRepository.save(msgDto.toSendBuilder()));
       return  response;
    }

    @Transactional
    public ResponseEntity sendMultipleMessage(EmailDto mailDto) throws MessagingException, IOException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        //메일 제목 설정
        helper.setSubject(mailDto.getTitle());

        //참조자 설정
        //helper.setCc(mailDto.getCcAddress());

        helper.setText(mailDto.getContent(), false);

        helper.setFrom(FROM_ADDRESS);

        //첨부 파일 설정
        // Add the attachment
        if (mailDto.getFile() != null && !mailDto.getFile().isEmpty()) {
            String fileName = mailDto.getFile().getOriginalFilename();
            byte[] fileBytes = mailDto.getFile().getBytes();
            InputStreamSource inputStreamSource = new ByteArrayResource(fileBytes);
            helper.addAttachment(fileName, inputStreamSource);
        }

        //  수신자 개별 전송
        /*for(String s : mailDto.getAddress()) {
        	helper.setTo(s);
        	emailSender.send(message);
        }*/
        //수신자 한번에 전송
        helper.setTo(mailDto.getAddress());
        emailSender.send(message);
        log.info("mail multiple send complete.");
        EmailDto emailDto = EmailDto.builder()
                .content(mailDto.getContent())
                .address(mailDto.getAddress())
                .phone(mailDto.getPhone())
                .title(mailDto.getPhone())
                .build();
        sendRepository.save(emailDto.toMailBuilder());

        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity sendModelhouseMessage(EmailDto mailDto) throws MessagingException, IOException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        //메일 제목 설정
        helper.setSubject("3D 메버 모델하우스 입니다.");

        //참조자 설정
        //helper.setCc(mailDto.getCcAddress());
        if(mailDto.getType().equals("signup")){
                helper.setText("3D 메버 모델하우스 응모완료.\n" +
                        "추첨일 : 9월 9일(토) 낮 12시 라이브 추첨\n" +
                        "\n" +
                        "아래 추가 이벤트 진행중입니다!\n" +
                        " \n" +
                        "-공유 이벤트-\n" +
                        "1등 맥북 m2 max\n" +
                        "2등 신세계 100만원 상품권\n" +
                        "3등 제주도 항공권\n" +
                        "\n" +
                        "-방문예약 이벤트-\n" +
                        "3D모델하우스로 예약방문시 응모권 중복 지급!!!\n" +
                        "\n" +
                        "\n" +
                        "3D모델하우스 참여 해주셔서 진심으로 감사드립니다.\n" +
                        "궁금하신 내용 있으시면 3D모델하우스 상담을 통해 문의해주세요^^", false);
        }
        if(mailDto.getType().equals("reservation")){
            helper.setText(mailDto.getContent());
            String fileName = "test.png"; // 이미지 파일명
            Resource resource = new FileSystemResource("../image/" + fileName);
            helper.addAttachment(fileName, resource);
        }
        helper.setFrom(FROM_ADDRESS);

        //첨부 파일 설정
        // Add the attachment
        if (mailDto.getFile() != null && !mailDto.getFile().isEmpty()) {
            String fileName = mailDto.getFile().getOriginalFilename();
            byte[] fileBytes = mailDto.getFile().getBytes();
            InputStreamSource inputStreamSource = new ByteArrayResource(fileBytes);
            helper.addAttachment(fileName, inputStreamSource);
        }

        //  수신자 개별 전송
        /*for(String s : mailDto.getAddress()) {
        	helper.setTo(s);
        	emailSender.send(message);
        }*/
        //수신자 한번에 전송
        helper.setTo(mailDto.getAddress());
        emailSender.send(message);
        log.info("mail multiple send complete.");
        EmailDto emailDto = EmailDto.builder()
                .content(mailDto.getContent())
                .address(mailDto.getAddress())
                .phone(mailDto.getPhone())
                .title("3D 메버 모델하우스 입니다.")
                .build();
        sendRepository.save(emailDto.toMailBuilder());

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity saveReservationMail(List<ReservationEmailDto> list) throws MessagingException, IOException {
        LocalDate today = LocalDate.now();
        LocalDate sendDate = today;
        //TODO 스케쥴 돌릴 때 리스트로 받아와서 작업해야함

        LocalDate formattedDate = LocalDate.parse(sendDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        for (int i = 0; i < list.size(); i++) {
            switch (list.get(i).getPeriod()) {
                case "new":
                    sendDate = today.plusDays(1);
                    break;
                case "day":
                    sendDate = today.plusDays(1);
                    break;
                case "week":
                    sendDate = today.plusWeeks(1);
                    break;
                case "month":
                    sendDate = today.plusMonths(1);
                    break;
                case "year":
                    sendDate = today.plusYears(1);
                    break;
                default:
                    // 유효하지 않은 period 값이 들어온 경우
                    break;
//        )
            }
            ReservationEmailDto reservationEmailDto = ReservationEmailDto.builder()
                    .email(list.get(i).getEmail())
                    .title(String.valueOf(list.get(i).getTitle()))
                    .content(String.valueOf(list.get(i).getContent()))
                    .sendDate(sendDate.toString())
                    .period(list.get(i).getPeriod())
                    .build();
            reservationMailRepository.save(reservationEmailDto.toReservationMailBuilder());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public void sendMessage(EmailDto mailDto) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        //메일 제목 설정
        helper.setSubject(mailDto.getTitle());

        //참조자 설정
        //helper.setCc(mailDto.getCcAddress());

        helper.setText(mailDto.getContent(), false);

        helper.setFrom(FROM_ADDRESS);

        //첨부 파일 설정
       /* String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        helper.addAttachment(MimeUtility.encodeText(fileName, "UTF-8", "B"), new ByteArrayResource(IOUtils.toByteArray(file.getInputStream())));
*/
        //  수신자 개별 전송
        /*for(String s : mailDto.getAddress()) {
        	helper.setTo(s);
        	emailSender.send(message);
        }*/
        //수신자 한번에 전송
        helper.setTo(mailDto.getAddress());
        emailSender.send(message);
        SmsDto smsDto = SmsDto.builder()
                .msg(mailDto.getContent())
                .email(mailDto.getAddress())
                .phone(mailDto.getPhone())
                .type("mail")
                .build();
        sendRepository.save(smsDto.toSendBuilder());

    }

    @Transactional
    public void schedulEmail(ReservationEmailDto reservationEmailDto) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        //메일 제목 설정
        helper.setSubject(reservationEmailDto.getTitle());
        helper.setText(reservationEmailDto.getContent(), false);
        helper.setFrom(new InternetAddress(FROM_ADDRESS,"메버", "UTF-8"));
        helper.setTo(reservationEmailDto.getEmail());

        EmailDto emailDto = EmailDto.builder()
                .content(reservationEmailDto.getContent())
                .address(reservationEmailDto.getEmail())
                .title(reservationEmailDto.getTitle())
                .phone(reservationEmailDto.getPhone())
                .type("mail")
                .build();
        sendRepository.save(emailDto.toMailBuilder());

        LocalDate today = LocalDate.now();
        LocalDate sendDate = today;

        LocalDate formattedDate = LocalDate.parse(sendDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        String content = "";
        switch (reservationEmailDto.getPeriod()) {
            case "new":

                sendDate = today.plusDays(1);
                content = String.valueOf(reservationEmailDto.getContent());
                Member member = memberRepository.findByEmail(reservationEmailDto.getEmail()).orElse(null);
                member.setAfterDay(Long.valueOf(member.getAfterDay()) + 1);

                if(Long.valueOf(member.getAfterDay())>5)return;

                memberRepository.save(member);
                emailSender.send(message);
                break;
            case "day":
                sendDate = today.plusDays(1);
                content = String.valueOf(reservationEmailDto.getContent());
                emailSender.send(message);
                break;
            case "week":
                sendDate = today.plusWeeks(1);
                content = String.valueOf(reservationEmailDto.getContent());
                emailSender.send(message);
                break;
            case "month":
                sendDate = today.plusMonths(1);
                content = String.valueOf(reservationEmailDto.getContent());
                emailSender.send(message);
                break;
            case "year":
                sendDate = today.plusYears(1);
                content = String.valueOf(reservationEmailDto.getContent());
                emailSender.send(message);
                break;
            default:
                // 유효하지 않은 period 값이 들어온 경우
                break;
//        )
        }
        //날짜 업데이트
        ReservationEmailDto reservationEmail = ReservationEmailDto.builder()
                .seq(reservationEmailDto.getSeq())
                .email(reservationEmailDto.getEmail())
                .title(reservationEmailDto.getTitle())
                .content(content)
                .sendDate(sendDate.toString())
                .period(reservationEmailDto.getPeriod())
                .build();
        reservationMailRepository.save(reservationEmail.toReservationMailBuilder());
    }


//        @Transactional
//    public ResponseEntity<Object> sendSms(String phone, String email) throws Exception {
//        RestTemplate rest = new RestTemplate();
//        rest.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
//
//        //URI uri = URI.create("http://link.smsceo.co.kr/sendsms_test.php");
//        URI uri = URI.create("http://link.smsceo.co.kr/sendsms_utf8.php");
//
//        HttpHeaders headers = new HttpHeaders();
//        /*  headers.setContentType(MediaType.TEXT_HTML);*/
//        //headers.add("Content-Type", "text/html; charset=UTF-8");
//        //headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//        //headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED,);
//        // headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8));
//        headers.setContentType(new MediaType("application", "x-www-form-urlencoded", Charset.forName("UTF-8")));
//
//        /*JSONObject param = new JSONObject();*/
//        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
//       /* param.put("phone", "01063645022");
//        param.put("msg", "test");
//        param.put("callback", "01072818209");
//        param.put("userid", "mever");
//        param.put("userkey", "BzxQZV1sDzVSYAdmUX4HOFZmB3QAM1MuA34=");*/
//        param.add("phone", phone);
//        param.add("msg", "300,000원 진행 <br>" +
//                "7일간 18,550,000 마감 <br>" +
//                "\"당일수익\"환급원칙<br>" +
//                "https://mever.me/");
//        param.add("callback", "01072818209");
//        param.add("userid", smsUserid);
//        param.add("return_url", "http://localhost:8080/send/sms/success");
//        param.add("userkey", smsUserkey);
//
//        SmsDto smsDto = SmsDto.builder()
//                .msg(param.get("msg").toString())
//                .email(email)
//                .phone(param.get("phone").toString())
//                .type("sms")
//                .build();
//
//        try {
//            String respon = rest.postForObject(uri, new HttpEntity<>(param, headers), String.class);
//            System.out.println(respon);
//            String[] params = respon.split("&");
//            Map<String, String> map = new HashMap<String, String>();
//           /*for (int i =0;i<1;i++)
//            {
//                String name = params[0].split("=")[0];
//                System.out.println(name);
//                String value="";
//                if(para.split("=")[1]!=null||!para.split("=")[1].equals("")){
//                    value = para.split("=")[1];
//                }
//                map.put(name, value);
//            }*/
//            sendRepository.save(smsDto.toSendBuilder());
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new Exception(e.getMessage());
//        }
//        // if (SmsDto == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//
//      /*  Orders orders = orderRepository.findByPaymentKey(paymentKey);
//        cancelOrderRepository.save(paymentResHandleDto.toCancelOrder(orders));
//        orders.setStatus(paymentResHandleDto.getStatus());
//        orderRepository.save(orders);*/
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    @Transactional
    public ResponseEntity<Object> successSms(SmsDto smsDto) throws Exception {

        try {
            sendRepository.save(smsDto.toSendBuilder());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public Object sendList(String type, String email, String phone) throws Exception {

        try {
            if ((email == null || email.equals("")) && (phone == null || phone.equals(""))) {
                return sendRepository.findAllByType(type);
            }
            if (email != null || !email.equals("")) {
                return sendRepository.findAllByTypeAndEmail(type, email);
            }
            if (phone != null || !phone.equals("")) {
                return sendRepository.findAllByTypeAndPhone(type, phone);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    @Transactional
    public Object reservationList() throws Exception {

        try {
            return reservationMailRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    @Transactional
    public Object delReservation(String id) throws Exception {

        try {
            reservationMailRepository.deleteById(Long.valueOf(id));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
