package com.mever.api.domain.member.dto;

import com.mever.api.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberReq {
    private String seq;
    private String email;			// 이메일
    private String phone;			// 휴대폰 번호
    private String password;		// 비밀번호
    private String name;			// 이름
    private String category;		// 사용자 소속 페이지
    private String adminYn;		    // 관리자yn
    private String survey;			// 설문
    private String dcrp;		    //메모
    private Long afterDay;		    //신규 가입 후 메일 순
    private String appointment;
    private String message;
    private String progress;
    private String manager;
    private String shareUuid;
    private String type;
    private int shareCount;
    private int shCheck;
    private String mvsh; //추천한 사람 uuid
    private String sharedBy; //추천한 사람 uuid

    public Member toMemberEntity(Member member) {
        member.setEmail(email);
        member.setName(name);
        member.setSurvey(survey);
        member.setDcrp(dcrp);
        return member;
    }
    public Member toMemberBuilder() {
        return Member.builder()
                .email(email)
                .phone(phone)
                .name(name)
                .category(category)
                .adminYn(adminYn)
                .survey(survey)
                .dcrp(dcrp)
                .password(phone)
                .shareUuid(shareUuid)
                .sharedBy(sharedBy)
                .afterDay(0L)
                .build();
    }
}