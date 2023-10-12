package com.mever.api.domain.email.controller;

import com.mever.api.domain.email.dto.EmailDto;
import com.mever.api.domain.email.dto.ReservationEmailDto;
import com.mever.api.domain.email.dto.SmsDto;
import com.mever.api.domain.email.entity.ReservationMail;
import com.mever.api.domain.email.service.SendService;
import com.mever.api.domain.schedule.ScheduledTasks;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "send")
@RestController
public class SendController {
    @Autowired
    private SendService sendService;
    @Autowired
    private ScheduledTasks scheduledTasks;



    @GetMapping("/textMail")
    public String mailSend() {
        return "textMail";
    }
    @PostMapping(value="/send/mail", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary  = "메일 전송", description = "메일 전송합니다.")
    public ResponseEntity sendMail(
            @ApiParam(value = "요청 객체", required = true) @ModelAttribute EmailDto emailDto) throws Exception {
        try {
            return ResponseEntity.ok(sendService.sendMultipleMessage(emailDto));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    @PostMapping("/automail")
    public void autoMail() throws Exception {
        try {
            scheduledTasks.cronMailSend();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
//    @PostMapping(value = "/send/sms")
//    @Operation(summary  = "sms 전송", description = "email,phone 입력")
//    public ResponseEntity sendSms( @ApiParam(value = "email", required = true) @RequestBody SmsDto smsDto) throws Exception {
//        try {
//            return ResponseEntity.ok(sendService.sendSms(smsDto.getPhone(),smsDto.getEmail()));
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new Exception(e.getMessage());
//        }
//    }

    @PostMapping(value = "/send/reservation/mail")
    @Operation(summary  = "메일 예약 전송", description = "메일 예약 전송 정보 저장")
    public ResponseEntity ReservationMail(@ApiParam(value = "email", required = true) @RequestBody List<ReservationEmailDto> reservationEmailDto) throws Exception {
        try {
            return ResponseEntity.ok(sendService.saveReservationMail(reservationEmailDto));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @PostMapping(value = "/send/list")
    @Operation(summary  = "전송 이력", description = "email,phone 입력")
    public ResponseEntity sendList( @ApiParam(value = "type", required = false) @RequestParam String type
            ,@ApiParam(value = "phone")@RequestParam(required = false) String phone
            ,@ApiParam(value = "email")@RequestParam(required = false) String email) throws Exception {
        try {
            return ResponseEntity.ok(sendService.sendList(type,email,phone));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    @PostMapping(value = "/send/reservation/list")
    @Operation(summary  = "메일 예약 이력", description = "메일 예약 이력")
    public ResponseEntity ReservationList( )throws Exception {
        try {
            return ResponseEntity.ok(sendService.reservationList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    @PostMapping(value = "/send/reservation/delete")
    @Operation(summary  = "메일 예약 삭제", description = "메일 예약 삭제")
    public ResponseEntity delReservation(@ApiParam(value = "seq") @RequestParam String seq )throws Exception {
        try {
            return ResponseEntity.ok(sendService.delReservation(seq));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
}
