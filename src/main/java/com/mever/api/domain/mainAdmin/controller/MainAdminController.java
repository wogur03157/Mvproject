package com.mever.api.domain.mainAdmin.controller;


import com.mever.api.domain.mainAdmin.dto.MainDto;
import com.mever.api.domain.mainAdmin.entity.MainTitle;
import com.mever.api.domain.mainAdmin.entity.Menu;
import com.mever.api.domain.mainAdmin.service.MainAdminService;
import io.swagger.annotations.ApiParam;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class MainAdminController {
    @Autowired
    private MainAdminService mainAdminService;

    @PostMapping("/getMainTitle")
    public ResponseEntity<MainDto> getMainTitle(
            @RequestBody Map<String,String> requestData
    ){
//        String category = requestData.get("category");
        return ResponseEntity.ok(mainAdminService.getMainTitle(requestData));
    }
    @PostMapping("/getMainTitleList")
    public ResponseEntity getMainTitleList(
            @RequestBody Map<String,String> requestData
    ){
//        String category = requestData.get("category");
        return ResponseEntity.ok(mainAdminService.getMainTitleList(requestData));
    }
    @PostMapping("/updateTitle")
    public ResponseEntity<MainDto> updateTitle(
            @RequestBody Map<String,String> requestData
    ){
        mainAdminService.updateTitle(requestData);

        return ResponseEntity.ok().build();
    }
    @PostMapping("/deleteTitle")
    public ResponseEntity<MainDto> deleteTitle(
            @RequestBody Map<String,String> requestData
    ){
        mainAdminService.deleteTitle(requestData);

        return ResponseEntity.ok().build();
    }
    @PostMapping("/updateContents")
    public ResponseEntity<MainDto> updateContents(
            @RequestBody Map<String,String> requestData
    ){
        mainAdminService.updateContents(requestData);

        return ResponseEntity.ok().build();
    }
    @PostMapping("/getItemContents")
    public ResponseEntity<MainDto> getItemContents(
            @RequestBody Map<String,String> requestData
    ){
            String category = requestData.get("category");
            String orderId = requestData.get("orderId");
            return ResponseEntity.ok(mainAdminService.getItemContents(category,orderId));
    }
    @PostMapping("/itemList")
    public ResponseEntity itemList(
            @RequestBody Map<String,String> requestData) throws Exception {
        try {
            String category = requestData.get("category");
            return ResponseEntity.ok(mainAdminService.getItemList(category));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    @PostMapping("/getReservation")
    public ResponseEntity getReservation(
            @RequestBody Map<String,String> requestData) throws Exception {
        try {
            String category = requestData.get("category");
            return ResponseEntity.ok(mainAdminService.getReservation(category));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    @PostMapping("/getReservation2")
    public ResponseEntity getReservation2(
            @RequestBody Map<String,String> requestData) throws Exception {
        try {
            String category = requestData.get("category");
            return ResponseEntity.ok(mainAdminService.getReservation2(category));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    @PostMapping("/setReservation")
    public ResponseEntity<MainDto> setReservation(
            @RequestBody Map<String,String> requestData
    )throws Exception {
        mainAdminService.setReservation(requestData);

        return ResponseEntity.ok().build();
    }
    @PostMapping("/menuList")
    public ResponseEntity menuList() throws Exception {
        try {
            return ResponseEntity.ok(mainAdminService.getMenuList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    @PostMapping("/updateMenu")
    public ResponseEntity<Menu> updateMenu(
            @RequestBody Map<String,String> requestData
    ) throws Exception {
        try {
            mainAdminService.updateMenu(requestData);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    @PostMapping("/updateReservation")
    public ResponseEntity<MainDto> updateReservation(
            @RequestBody Map<String,String> requestData
    ) throws MessagingException, IOException {
        mainAdminService.updateReservation(requestData);

        return ResponseEntity.ok().build();
    }
    @PostMapping("/getRanking")
    public ResponseEntity getRanking() throws Exception {
        try {
            return ResponseEntity.ok(mainAdminService.getRanking());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

}
