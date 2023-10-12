package com.mever.api.domain.sales.service;


import com.mever.api.domain.sales.dto.SaleMemberDto;
import com.mever.api.domain.sales.entity.SalesMember;
import com.mever.api.domain.sales.repository.SaleMemberRepository;
import com.mever.api.domain.sales.repository.SaleBenefitRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaleMemberService {
    private final SaleMemberRepository saleMemberRepository;
    private final SaleBenefitRepository saleBenefitRepository;

    @Transactional
    public ResponseEntity signUp(SaleMemberDto memberDto) throws Exception {
        String id= memberDto.getUserId();
        // 아이디 중복 가입 체크
        if (saleMemberRepository.existsByUserId(memberDto.getUserId())) {
            return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
        }
        if(memberDto.getRecommenderId()!=null){
            SalesMember salesMember = saleMemberRepository.findByUserId(memberDto.getRecommenderId()).orElse(null);
            memberDto.setRecommender(salesMember.getId());
        }
        // 비밀번호 유효성 체크
//        if (!isValidPassword(memberDto.getPassword())) {
//            throw new Exception("비밀번호는 최소 8자 이상이어야 하며, 특수문자와 숫자를 포함해야 합니다.");
//        }
        // 비밀번호 암호화 등 회원 가입 처리 로직

        // userRepository를 사용하여 사용자 정보를 저장하는 코드
        SalesMember member=SaleMemberDto.toMemberEntity(memberDto);
        saleMemberRepository.save(member);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity updateMember(Long id,SaleMemberDto memberDto) {
        SalesMember member= saleMemberRepository.findById(id).orElse(null);
        if (member != null) {
            if (memberDto.getPassword() != null) {
                member.setPassword(memberDto.getPassword());
            }
            if (memberDto.getUserNm() != null) {
                member.setUserNm(memberDto.getUserNm());
            }
            if (memberDto.getFranchiseFee() != null&&!memberDto.getFranchiseFee().equals("")) {
                member.setFranchiseFee(memberDto.getFranchiseFee());
            }
            if (memberDto.getProductionCost() != null&&!memberDto.getProductionCost().equals("")) {
                member.setProductionCost(memberDto.getProductionCost());
            }
            saleMemberRepository.save(member);
            return ResponseEntity.ok(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    @Transactional
    public ResponseEntity deleteMember(Long id) {
        saleMemberRepository.delete(saleMemberRepository.findById(id).orElse(null));
        return ResponseEntity.ok(HttpStatus.OK);
    }
    private boolean isValidPassword(String password) {
        // 비밀번호 유효성 체크 로직을 구현하세요.
        // 예를 들어, 비밀번호는 최소 8자 이상이어야 하며, 특수문자와 숫자를 포함해야 한다고 가정합니다.
        // 필요에 따라 정규식을 사용하여 비밀번호 유효성을 검사할 수도 있습니다.
        // 유효한 비밀번호이면 true를 반환하고, 그렇지 않으면 false를 반환합니다.
        if (password.length() < 8) {
            return false;
        }
        // 특수문자 포함 여부 체크
        if (!password.matches(".*[!@#$%^&*()].*")) {
            return false;
        }
        // 숫자 포함 여부 체크
        if (!password.matches(".*\\d.*")) {
            return false;
        }
        return true;
    }

    @Transactional
    public Object chkLogin(SaleMemberDto memberDto) {
        String id= memberDto.getUserId();
        String password= memberDto.getPassword();
        boolean member = saleMemberRepository.existsByUserIdAndPassword(id,password);
        if(member) {
            SalesMember user=saleMemberRepository.findByUserIdAndPassword(id,password).orElse(null);
            return user;
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public Object memberList(String email) {
        if(email==null||email.equals("")) {
            return saleMemberRepository.findAll();
        }
        return saleMemberRepository.findByUserId(email).orElse(null);
    }

    @Transactional
    public ResponseEntity addRecommend(Long id,SaleMemberDto memberDto) {
        SalesMember member= saleMemberRepository.findById(id).orElse(null);
        // userRepository를 사용하여 사용자 정보를 저장하는 코드
        if (member != null) {
            if (memberDto.getReferrer1() != null) {
                member.setReferrer1(memberDto.getReferrer1());
            }
            if (memberDto.getReferrer2() != null) {
                member.setReferrer2(memberDto.getReferrer2());
            }
            if (memberDto.getReferrer3() != null) {
                member.setReferrer3(memberDto.getReferrer3());
            }
            saleMemberRepository.save(member);
            return ResponseEntity.ok(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
