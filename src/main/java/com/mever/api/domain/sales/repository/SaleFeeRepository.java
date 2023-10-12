package com.mever.api.domain.sales.repository;

import com.mever.api.domain.sales.entity.SalesFee;
import com.mever.api.domain.sales.entity.SalesMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SaleFeeRepository extends JpaRepository<SalesFee, Long> {


}