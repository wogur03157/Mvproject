package com.mever.api.domain.payment.repository;

import com.mever.api.domain.payment.entity.CancelOrder;
import com.mever.api.domain.payment.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CancelOrderRepository extends JpaRepository<CancelOrder, Long> {

}
