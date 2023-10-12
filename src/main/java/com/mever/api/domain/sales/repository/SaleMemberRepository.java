package com.mever.api.domain.sales.repository;

import com.mever.api.domain.sales.entity.SalesMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SaleMemberRepository extends JpaRepository<SalesMember, Long> {

    public boolean existsByUserIdAndPassword(String userId, String password);
    public Optional<SalesMember> findByUserIdAndPassword(String userId, String password);
    public Optional<SalesMember> findByUserId(String userId);
    boolean existsByUserId(String userId);
}