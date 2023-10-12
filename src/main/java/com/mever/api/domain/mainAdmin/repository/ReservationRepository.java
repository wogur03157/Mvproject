package com.mever.api.domain.mainAdmin.repository;

import com.mever.api.domain.mainAdmin.entity.ItemContents;
import com.mever.api.domain.mainAdmin.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    public Reservation findByOrderIdAndCategory(String orderId,String category);
    public List<Reservation> findByOrderId(String orderId);
    public Reservation findBySeq(String seq);
    public List<Reservation> findByCategory(String category);
}

