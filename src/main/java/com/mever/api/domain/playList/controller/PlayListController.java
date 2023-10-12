package com.mever.api.domain.playList.controller;

import com.mever.api.domain.payment.dto.PaymentResHandleDto;
import com.mever.api.domain.playList.dto.PlayListDto;
import com.mever.api.domain.playList.entity.PlayList;
import com.mever.api.domain.playList.service.PlayListService;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "결제")
@RestController
public class PlayListController {

    @Autowired
    private PlayListService playListService;

    @PostMapping("/palylist")
    @Operation(summary = "결제 요청", description = "결제 요청에 필요한 값들을 반환합니다.")
    public ResponseEntity<List<PlayList>> palylist(
            @ApiParam(value = "요청 객체", required = true)
            @RequestParam String homePage) throws Exception {
        try {
            return ResponseEntity.ok(playListService.playList(homePage));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    @PostMapping("/palylist/add")
    @Operation(summary = "결제 요청", description = "결제 요청에 필요한 값들을 반환합니다.")
    public ResponseEntity palylistAdd(
            @ApiParam(value = "요청 객체", required = true)
            @RequestBody PlayListDto playListDto) throws Exception {
        try {
            return ResponseEntity.ok(playListService.playListAdd(playListDto));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    @PostMapping("/palylist/del")
    @Operation(summary = "결제 요청", description = "결제 요청에 필요한 값들을 반환합니다.")
    public ResponseEntity palylistDel(
            @ApiParam(value = "요청 객체", required = true)
            @RequestBody PlayListDto playListDto) throws Exception {
        try {
            return ResponseEntity.ok(playListService.playListDel(playListDto));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }




}
