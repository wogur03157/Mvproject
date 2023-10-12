package com.mever.api.domain.sales.repository;

import com.mever.api.domain.sales.entity.SalesBenefit;
import com.mever.api.domain.sales.entity.SalesFee;
import com.mever.api.domain.sales.entity.SalesMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SaleBenefitRepository extends JpaRepository<SalesBenefit, Long> {

    public Optional<SalesFee> findByUserId(String userId);
    public List<SalesBenefit> findAllByUserId(String userId);
}