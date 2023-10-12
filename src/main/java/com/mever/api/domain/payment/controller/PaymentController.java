package com.mever.api.domain.payment.controller;

import com.mever.api.domain.payment.dto.PaymentResHandleDto;
import com.mever.api.domain.payment.service.PaymentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Tag(name = "결제")
@RestController
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/payment")
    @Operation(summary = "결제 요청", description = "결제 요청에 필요한 값들을 반환합니다.")
    public ResponseEntity<PaymentResHandleDto> requestPayments(
            @ApiParam(value = "요청 객체", required = true)
            @RequestBody PaymentResHandleDto paymentResHandleDto) throws Exception {
        try {
            System.out.println(paymentResHandleDto);
            return ResponseEntity.ok(paymentService.requestPayments(paymentResHandleDto));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    /*@GetMapping("/success")
    @ApiOperation(value = "결제 성공 리다이렉트", notes = "결제 성공 시 최종 결제 승인 요청을 보냅니다.")
    public ResponseEntity<String> requestFinalPayments(
            @ApiParam(value = "토스 측 결제 고유 번호", required = true) @RequestParam String paymentKey,
            @ApiParam(value = "우리 측 주문 고유 번호", required = true) @RequestParam String orderId,
            @ApiParam(value = "실제 결제 금액", required = true) @RequestParam Long amount
    ) throws Exception {
        try {
            System.out.println("paymentKey = " + paymentKey);
            System.out.println("orderId = " + orderId);
            System.out.println("amount = " + amount);

            //paymentService.verifyRequest(paymentKey, orderId, amount);
            String result = paymentService.requestFinalPayment(paymentKey, orderId, amount).toString();

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }*/
    @PostMapping("/autoPayment")
    @Operation(summary = "자동 결제 요청", description = "자동 결제 요청에 필요한 값들을 반환합니다.")
    public ResponseEntity<PaymentResHandleDto> autotPayments(
            @ApiParam(value = "요청 객체", required = true)
            @RequestBody PaymentResHandleDto paymentResHandleDto) throws Exception {
        try {
            return ResponseEntity.ok(paymentService.autoPayments(paymentResHandleDto));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    @PostMapping("/subscription/list")
    @ApiOperation(value = "정기 구독 리스트", notes = "정기 구독 결제 내용을 반환합니다.")
    public ResponseEntity<List> subscriptionList(
//            @ApiParam(value = "토스 측 결제 고유 번호", required = true) @RequestParam String email,
//            @ApiParam(value = "우리 측 주문 고유 번호", required = true) @RequestParam String phone,
            @RequestBody Map<String,String> requestData
            ) throws Exception {
        try {
            String email = requestData.get("email");
            String phone = requestData.get("phone");
            return ResponseEntity.ok(paymentService.subscriptionList(email,phone));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    @PostMapping("/subCancel")
    @ApiOperation(value = "정기결제 취소", notes = "다음 정기 결제 건에 대해서 결제취소를 요청합니다.")
    public ResponseEntity  subCancel(
            @ApiParam(value = "토스 측 주문 빌링키", required = true) @RequestParam String billingKey
            ) throws Exception {
        try {
            return ResponseEntity.ok(paymentService.subCancel(billingKey));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @PostMapping("/paymentList")
    @ApiOperation(value = "payment 정보", notes = "payment 정보를 반환합니다.")
    public ResponseEntity<List> paymentList(
            @RequestBody Map<String,String> requestData) throws Exception {
        try {
            return ResponseEntity.ok(paymentService.paymentList(requestData));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @PostMapping("/cancelOrder")
    @ApiOperation(value = "결제 취소", notes = "완료 된 결제 건에 대해서 결제취소를 요청합니다.")
    public ResponseEntity cancelOrder(
            @ApiParam(value = "토스 측 주문 고유 번호", required = true) @RequestParam String paymentKey,
            @ApiParam(value = "결제 취소 사유", required = true) @RequestParam String cancelReason) throws Exception {
        try {

            return ResponseEntity.ok(paymentService.cancelOrder(paymentKey,cancelReason));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }


}
