package com.mever.api.domain.member.entity;

import com.mever.api.domain.member.dto.MemberRes;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Setter
@DynamicInsert
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq", nullable = false, unique = true)
    private Long seq;
    @Column(nullable = false)
    private String email;
    @Column
    private String phone;
    @Column
    private String password;
    @Column
    private String name;
    @Column
    private String category;
    @Column
    private String adminYn;
    @Column
    private String survey;
    @Column
    private String dcrp;
    @Column
    private String regdate;
    @Column
    private Long afterDay;
    @Column
    private String appointment;
    @Column
    private String progress;
    @Column
    private String manager;
    @Column
    private int shareCount;
    @Column
    private String shareUuid;
    @Column
    private String sharedBy;

    public MemberRes toDto() {
        return MemberRes.builder()
                .email(email)
                .phone(phone)
                .name(name)
                .survey(survey)
                .dcrp(dcrp)
                .afterDay(afterDay)
                .build();
    }
}