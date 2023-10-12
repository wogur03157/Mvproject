package com.mever.api.domain.mainAdmin.repository;

import com.mever.api.domain.mainAdmin.entity.ItemContents;
import com.mever.api.domain.mainAdmin.entity.MainTitle;
import com.mever.api.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemContentsRepository extends JpaRepository<ItemContents, Long> {
    public ItemContents findByOrderName(String orderName);
    public ItemContents findByOrderId(String orderId);

    public Optional<List<ItemContents>>findByCategory(String category);
    public ItemContents findByOrderNameAndCategory(String orderName,String category);
}

