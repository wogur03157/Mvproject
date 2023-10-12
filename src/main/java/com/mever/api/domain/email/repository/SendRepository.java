package com.mever.api.domain.email.repository;

import com.mever.api.domain.email.entity.SendHistory;
import com.mever.api.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SendRepository extends JpaRepository<SendHistory, Long> {

    List<SendHistory> findAllByEmail(String email);
    List<SendHistory> findAllByType(String type);
    List<SendHistory> findAllByTypeAndEmail(String type,String email);
    List<SendHistory> findAllByTypeAndPhone(String type,String phone);
}