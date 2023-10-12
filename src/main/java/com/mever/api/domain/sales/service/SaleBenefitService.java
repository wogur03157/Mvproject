package com.mever.api.domain.sales.service;


import com.mever.api.domain.sales.dto.SaleBenefitDto;
import com.mever.api.domain.sales.dto.SaleMemberDto;
import com.mever.api.domain.sales.entity.SalesFee;
import com.mever.api.domain.sales.entity.SalesMember;
import com.mever.api.domain.sales.entity.SalesBenefit;
import com.mever.api.domain.sales.repository.SaleFeeRepository;
import com.mever.api.domain.sales.repository.SaleMemberRepository;
import com.mever.api.domain.sales.repository.SaleBenefitRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaleBenefitService {
    private final SaleMemberRepository saleMemberRepository;
    private final SaleBenefitRepository saleBenefitRepository;
    private final SaleFeeRepository saleFeeRepository;


    @Transactional
    public ResponseEntity franchiseFee(Long id, SaleBenefitDto benefitDto) {
        SalesMember member= saleMemberRepository.findById(id).orElse(null);
        List<String> userList= List.of(member.getReferrer1(), member.getReferrer2(), member.getReferrer3());
        List<SalesFee> fee=saleFeeRepository.findAll();
        List<String> filterList=fee.stream()
                .filter(list -> list.getReferrerFee() !=null)
                .map(SalesFee::getReferrerFee)
                .collect(Collectors.toList());
        SalesMember recommender= saleMemberRepository.findByUserId(member.getReferrer1()).orElse(null);
        BigDecimal amount= new BigDecimal(benefitDto.getAmount());
        amount=amount.multiply(new BigDecimal(recommender.getFranchiseFee())).setScale(0, RoundingMode.HALF_EVEN);
        if (member != null) {
            for(int i =0;i<3;i++){
                BigDecimal benefit= new BigDecimal(filterList.get(i));
                BigDecimal money= amount.multiply(benefit).setScale(0, RoundingMode.HALF_EVEN);
                benefitDto.setUserId(userList.get(i));
                benefitDto.setRecommenderId(member.getRecommender());
                benefitDto.setCalculateCheck("N");
                benefitDto.setAmount(money.toString());
                benefitDto.setType("가맹비");
                saleBenefitRepository.save(SaleBenefitDto.toBenefitEntity(benefitDto));
            }
            return ResponseEntity.ok(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public ResponseEntity productionCost(Long id, SaleBenefitDto benefitDto) {
        SalesMember member= saleMemberRepository.findById(id).orElse(null);
        List<String> userList= List.of(member.getReferrer1(), member.getReferrer2(), member.getReferrer3());
        List<SalesFee> fee=saleFeeRepository.findAll();
        List<String> filterList=fee.stream()
                .filter(list -> list.getReferrerFee() !=null)
                .map(SalesFee::getReferrerFee)
                .collect(Collectors.toList());
        SalesMember recommender= saleMemberRepository.findByUserId(member.getReferrer1()).orElse(null);
        BigDecimal amount= new BigDecimal(benefitDto.getAmount());
        amount=amount.multiply(new BigDecimal(recommender.getProductionCost())).setScale(0, RoundingMode.HALF_EVEN);
        if (member != null) {
            for(int i =0;i<3;i++){
                BigDecimal benefit= new BigDecimal(filterList.get(i));
                BigDecimal money= amount.multiply(benefit).setScale(0, RoundingMode.HALF_EVEN);
                benefitDto.setUserId(userList.get(i));
                benefitDto.setRecommenderId(member.getRecommender());
                benefitDto.setCalculateCheck("N");
                benefitDto.setAmount(money.toString());
                benefitDto.setType("제작비");
                saleBenefitRepository.save(SaleBenefitDto.toBenefitEntity(benefitDto));
            }
            return ResponseEntity.ok(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public Object settleList(String email) {
        if(email==null||email.equals("")) {
            return saleBenefitRepository.findAll();
        }
        List<SalesBenefit> list =saleBenefitRepository.findAllByUserId(email);
        if (list == null || list.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);}
        return list;
    }
    @Transactional
    public ResponseEntity approveSettle(Long id) {
        SalesBenefit user= saleBenefitRepository.findById(id).orElse(null);
        // userRepository를 사용하여 사용자 정보를 저장하는 코드
        if (user != null) {
            user.setCalculateCheck("Y");
            saleBenefitRepository.save(user);
            return ResponseEntity.ok(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    @Transactional
    public ResponseEntity deleteMember(Long id) {
        saleMemberRepository.delete(saleMemberRepository.findById(id).orElse(null));
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
