package com.mever.api.domain.sales.controller;


import com.mever.api.domain.sales.dto.SaleMemberDto;
import com.mever.api.domain.sales.repository.SaleMemberRepository;
import com.mever.api.domain.sales.service.SaleMemberService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "영업")
@RestController
@RequestMapping("/sales")
public class SaleMemberController {

    @Autowired
    private SaleMemberService saleMemberService;
    @Autowired
    SaleMemberRepository memberRepository;

    @PostMapping("/signup")
    @Operation(summary = "회원정보 저장", description = "회원정보 저장.")
    public ResponseEntity signUp(@RequestBody SaleMemberDto memberDto)throws Exception {
        return ResponseEntity.ok(saleMemberService.signUp(memberDto));

    }
    @PostMapping("/chklogin")
    @Operation(summary = "로그인 체크", description = "로그인.")
    public ResponseEntity chkLogin( @ApiParam(value = "요청 객체", required = true) @RequestBody SaleMemberDto memberDto) throws Exception {
       return ResponseEntity.ok().body(saleMemberService.chkLogin(memberDto));
    }
    @PostMapping("/{id}/update")
    public ResponseEntity updateMember(@PathVariable("id") Long id, @RequestBody SaleMemberDto memberDto)throws Exception {
       return ResponseEntity.ok( saleMemberService.updateMember(id, memberDto));
    }
    @PostMapping("/{id}/delete")
    @Operation(summary = "회원삭제", description = "회원삭제.")
    public ResponseEntity deleteMember(@PathVariable("id") Long id) {
        return ResponseEntity.ok(saleMemberService.deleteMember(id));
    }
    @PostMapping("/list")
    @ApiOperation(value = "회원정보", notes = "회원정보")
    public ResponseEntity memberList(
            @ApiParam(value = "요청 객체", required = false) @RequestParam(required = false) String email) throws Exception {
        try {
            return ResponseEntity.ok(saleMemberService.memberList(email));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    @PostMapping("/{id}/addrecommender")
    @Operation(summary = "추천인 저장", description = "추천인 저장.")
    public ResponseEntity addRecommender(@PathVariable("id") Long id,@RequestBody SaleMemberDto memberDto)throws Exception {
        return ResponseEntity.ok(saleMemberService.addRecommend(id, memberDto));
    }
//    @PostMapping("/recommender/list")
//    @ApiOperation(value = "회원정보", notes = "회원정보")
//    public ResponseEntity recommenderList(
//            @ApiParam(value = "요청 객체", required = false) @RequestParam(required = false) Long id) throws Exception {
//        try {
//            return ResponseEntity.ok(saleMemberService.recommenderList(id));
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new Exception(e.getMessage());
//        }
//    }
}
