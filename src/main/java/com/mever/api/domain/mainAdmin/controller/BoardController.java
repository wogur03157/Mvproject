package com.mever.api.domain.mainAdmin.controller;

import com.mever.api.domain.mainAdmin.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RequiredArgsConstructor
@Controller
public class BoardController {
    private final FileService fileService;

    @PostMapping("/register/action")
    public ResponseEntity boardRegisterAction(MultipartHttpServletRequest multiRequest) throws Exception {
        fileService.uploadFile(multiRequest);
        return ResponseEntity.ok().build();
    }
}
