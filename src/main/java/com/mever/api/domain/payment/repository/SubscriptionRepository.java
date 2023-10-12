package com.mever.api.domain.payment.repository;

import com.mever.api.domain.payment.entity.CancelOrder;
import com.mever.api.domain.payment.entity.Orders;
import com.mever.api.domain.payment.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    public List<Subscription> findByStatusAndCustomerKeyIsNotNullAndBillingKeyIsNotNull(String status);
    public Subscription findByCustomerKeyAndBillingKey(String customerKey,String billingKey);
    public Subscription findByBillingKey(String billingKey);

}
