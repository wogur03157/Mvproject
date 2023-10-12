package com.mever.api.domain.payment.repository;

import com.mever.api.domain.payment.entity.CancelOrder;
import com.mever.api.domain.payment.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    public Orders findByOrderId(String orderId);
    public Orders findByPaymentKey(String paymentKey);
    public Orders findByCustomerKey(String CustomerKey);
}
