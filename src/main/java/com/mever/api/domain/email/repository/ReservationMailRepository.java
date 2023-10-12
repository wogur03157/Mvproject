package com.mever.api.domain.email.repository;

import com.mever.api.domain.email.dto.ReservationEmailDto;
import com.mever.api.domain.email.entity.ReservationMail;
import com.mever.api.domain.email.entity.SendHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationMailRepository extends JpaRepository<ReservationMail, Long> {
    public List<ReservationMail> findAll();
    public void deleteById(Long id);

}