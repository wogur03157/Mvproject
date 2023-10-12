package com.mever.api.domain.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mever.api.domain.email.dto.EmailDto;
import com.mever.api.domain.email.dto.SmsDto;
import com.mever.api.domain.email.service.SendService;
import com.mever.api.domain.member.dto.MemberReq;
import com.mever.api.domain.member.dto.MemberRes;
import com.mever.api.domain.member.entity.Member;
import com.mever.api.domain.member.repository.MemberMapper;
import com.mever.api.domain.member.repository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final SendService sendService;
    @Transactional
    public ResponseEntity insMember(MemberReq memberReq) throws IOException, NoSuchAlgorithmException, URISyntaxException, InvalidKeyException, MessagingException {

        String category = memberReq.getCategory();
        String email=memberReq.getEmail();
        String test="";
        Member member;
        int sum = 0;

        //가입 사이트별 아이디 찾기
        if (category == null && test.equals(category)) {
            member = memberRepository.findByEmail(email).orElse(null);
        } else {
            member = memberRepository.findByEmailAndCategory(email, category).orElse(null);
        }

        //신규 가입시 정보 입력 및 가입 메시지 전송
        if (member == null) {
            //추천인 카운트 늘리기
            if (memberReq.getShCheck()==1){
                member = memberRepository.findByShareUuid(memberReq.getMvsh());
                sum = member.getShareCount();
                member.setShareCount(sum+1);
                memberRepository.save(member);
                memberReq.setSharedBy(memberReq.getMvsh());
            }
            memberReq.setPassword(memberReq.getPhone());
            if (memberReq.getCategory() != null && !test.equals(memberReq.getCategory())) {
                memberReq.setCategory(memberReq.getCategory());
            }
            memberRepository.save(memberReq.toMemberBuilder());
            if(memberReq.getMessage()!=null&&!memberReq.getMessage().equals("")){
                SmsDto smsDto=new SmsDto();
                smsDto.setEmail(memberReq.getEmail());
                smsDto.setPhone(memberReq.getPhone());
                smsDto.setMsg(memberReq.getMessage());
                sendService.sendNaver(smsDto);
                EmailDto mailDto= new EmailDto();
                mailDto.setAddress(memberReq.getEmail());
                mailDto.setType("signup");
                mailDto.setPhone(memberReq.getPhone());
                sendService.sendModelhouseMessage(mailDto);
            }
            //가입 내역 있을 시 핸드폰 번호 체크
        } else {
            if (memberReq.getPhone() != null && !test.equals(memberReq.getPhone())) {
                member.setPhone(memberReq.getPhone());
            }
            if (memberReq.getAppointment() != null && !test.equals(memberReq.getAppointment())) {
                member.setAppointment(memberReq.getAppointment());
            }
//            //아이디가 있을시 공유 uuid 새로넣어주기
//            if (memberReq.getShareUuid() != null) {
//                member.setShareUuid(memberReq.getShareUuid());
//            }
            memberRepository.save(member);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Transactional
    public Member getUserInfo(Map<String,String> memberReq) {
        String email=memberReq.get("email");
        String phone=memberReq.get("phone");
        Member member;
        member = memberRepository.findFirstByEmailAndPhoneOrderBySeqDesc(email,phone);
        return member;
    }
    @Transactional
    public ResponseEntity chkLogin(MemberReq memberReq) {

        String email=memberReq.getEmail();
        String password=memberReq.getPassword();
        boolean member = memberRepository.existsByEmailAndPassword(email,password);
        if(member) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public Object chkAdmin(MemberReq memberReq) {

        String email=memberReq.getEmail();
        String password=memberReq.getPassword();
        Member member = memberRepository.findByEmailAndPasswordAndAdminYn(email,password,"Y");
        if(member != null) {

            return member;
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    @Transactional
    public ResponseEntity updateInfo(MemberReq memberReq) throws IOException, NoSuchAlgorithmException, URISyntaxException, InvalidKeyException, MessagingException {

        String password = memberReq.getPhone();
        String email=memberReq.getEmail();
        String category=memberReq.getCategory();
        Member member = memberRepository.findByEmailAndPasswordAndCategory(email,password,category).orElse(null);
        if (member != null) {
            member.setManager(memberReq.getManager());
            member.setProgress(memberReq.getProgress());
            member.setDcrp(memberReq.getDcrp());
            memberRepository.save(member);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public Object memberList(String category,String email) {

//        String email = requestData.get("email");
//        String category = requestData.get("category");
        if(category != null && category != ""){
            List<MemberRes> memberList = memberMapper.getMemberList(category);
            return memberList;
        }
        if(email==null||email.equals("")) {
            return memberRepository.findAll();
        }
        return memberRepository.findByEmail(email).orElse(null);
    }
}
