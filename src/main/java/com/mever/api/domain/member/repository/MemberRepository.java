package com.mever.api.domain.member.repository;

import com.mever.api.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    public boolean existsByEmailAndPassword(String email, String password);
    public Optional<Member> findByEmail(String email);
    public List<Member> findByCategory(String Category);
    public Member findFirstByEmailAndPhoneOrderBySeqDesc(String email,String phone);
    public Member findByShareUuid(String shareUuid);
    public Optional<Member> findByEmailAndPassword(String email,String password);
    public Optional<Member> findByEmailAndCategory(String email,String category);
    public Optional<Member> findAllByCategory(String category);
    public Member findByName(String name);
    public Member findByEmailAndPasswordAndAdminYn(String email,String password,String adminYn);
    public Optional<Member> findByEmailAndPasswordAndCategory(String email,String password,String category);
}