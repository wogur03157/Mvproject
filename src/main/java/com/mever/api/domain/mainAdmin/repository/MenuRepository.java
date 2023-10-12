package com.mever.api.domain.mainAdmin.repository;

import com.mever.api.domain.mainAdmin.entity.MainTitle;
import com.mever.api.domain.mainAdmin.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    public Menu findBySeq(int seq);
}
