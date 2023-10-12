package com.mever.api.domain;

import com.mever.api.domain.email.dto.SmsDto;
import com.mever.api.domain.email.service.SendService;
import com.mever.api.domain.payment.service.PaymentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String home(){
        return "index";
    }

    @Autowired
    PaymentService paymentService;

    @Autowired
    SendService sendService;

    @GetMapping("/success")
    @ApiOperation(value = "결제 성공 리다이렉트", notes = "결제 성공 시 최종 결제 승인 요청을 보냅니다.")
    public String requestFinalPayments(
            @ApiParam(value = "토스 측 결제 고유 번호", required = true) @RequestParam String paymentKey,
            @ApiParam(value = "우리 측 주문 고유 번호", required = true) @RequestParam String orderId,
            @ApiParam(value = "실제 결제 금액", required = true) @RequestParam Long amount,
            @ApiParam(value = "실제 결제 금액", required = true) @RequestParam String url,
            HttpServletRequest request
    ) throws Exception {
        try {
            Map<String, String[]> paraMap = request.getParameterMap();
            for (String name : paraMap.keySet()){
                String[] values = paraMap.get(name);
                System.out.println(name+" = "+ Arrays.toString(values));
            }
            System.out.println("paymentKey = " + paymentKey);
            System.out.println("orderId = " + orderId);
            System.out.println("amount = " + amount);
            System.out.println("url = " + url);

            String result = paymentService.requestFinalPayment(paymentKey, orderId, amount).toString();

            return "redirect:"+url;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    @RequestMapping("/autoSuccess")
    @GetMapping("/autoSuccess")
    @ApiOperation(value = "자동 결제 성공 리다이렉트", notes = "자동 결제 성공 시 최종 결제 승인 요청을 보냅니다.")
    public String autoFinalPayments(
            @ApiParam(value = "토스 측 자동 결제 고유 번호", required = true) @RequestParam String authKey,
            @ApiParam(value = "우리 측 주문 고객 고유 번호", required = true) @RequestParam String customerKey,
//            @ApiParam(value = "우리 측 주문 고객 고유 번호", required = true) @RequestParam String url,
            HttpServletRequest request) throws Exception {
        try {
            Map<String, String[]> paraMap = request.getParameterMap();
//            for (String name : paraMap.keySet()){
//                String[] values = paraMap.get(name);
//                System.out.println(name+" = "+ Arrays.toString(values));
//            }
            String result = paymentService.autoFinalPayment(authKey,customerKey).toString();
           String url2 = request.getContextPath();
           String url3 = request.getRequestURI();
            String url = "https://mever.me/";

            return "redirect:"+url;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
            //sm@mever.me
            // mever12!@
        }
    }
    @PostMapping(value = "/send/sms/success")
    @Operation(summary  = "sms api success", description = "sms 전송 후 처리결과.")
    public String successSms(@ApiParam(value = "sms 전송 후 처리 결과") SmsDto smsDto,@ApiParam(value = "sms 전송 후 처리 결과") String url) throws Exception {
        try {
            System.out.println("result_code = " + smsDto.getResult_code());
            System.out.println("result_msg = " + smsDto.getResult_msg());
            /*System.out.println("total_count = " + total_count);
            System.out.println("succ_count = " + succ_count);
            System.out.println("fail_count = " + fail_count);
            System.out.println("money = " + money);*/

           ResponseEntity.ok(sendService.successSms(smsDto));
            return "redirect:"+url;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    @GetMapping("/refundRequire")
    public String refundRequire(){
        return "redirect:w/refundRequire.html";
    }
}
