package com.mever.api.domain.sales.controller;


import com.mever.api.domain.sales.dto.SaleBenefitDto;
import com.mever.api.domain.sales.dto.SaleMemberDto;
import com.mever.api.domain.sales.repository.SaleMemberRepository;
import com.mever.api.domain.sales.service.SaleBenefitService;
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
public class SaleBenefitController {

    @Autowired
    private SaleBenefitService saleBenefitService;
    @Autowired
    SaleMemberRepository memberRepository;

    @PostMapping("/{id}/franchiseFee")
    @Operation(summary = "가맹점 수수료", description = "id랑 amount만 보내주면 됩니다.")
    public ResponseEntity franchiseFee(@PathVariable("id") Long id,@RequestBody SaleBenefitDto saleBenefitDto)throws Exception {
        return ResponseEntity.ok(saleBenefitService.franchiseFee(id,saleBenefitDto));
    }
    @PostMapping("/{id}/productionCost")
    @Operation(summary = "제작비 수수료", description = "id랑 amount만 보내주면 됩니다.")
    public ResponseEntity productionCost(@PathVariable("id") Long id,@RequestBody SaleBenefitDto saleBenefitDto)throws Exception {
        return ResponseEntity.ok(saleBenefitService.productionCost(id,saleBenefitDto));
    }

    @PostMapping("/settle/list")
    @ApiOperation(value = "정산정보", notes = "정산정보")
    public ResponseEntity settleList(
            @ApiParam(value = "요청 객체", required = false) @RequestParam(required = false) String email) throws Exception {
        try {
            return ResponseEntity.ok(saleBenefitService.settleList(email));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    @PostMapping("/{id}/benefit/update")
    public ResponseEntity approveBenefit(@PathVariable("id") Long id)throws Exception {
        return ResponseEntity.ok( saleBenefitService.approveSettle(id));
    }
}
