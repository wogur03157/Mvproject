package com.mever.api.domain.mainAdmin.repository;

import com.mever.api.domain.mainAdmin.entity.MainTitle;
import com.mever.api.domain.mainAdmin.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MainRepository extends JpaRepository<MainTitle, Integer> {
    public MainTitle findByCategory(String category);
    public List<MainTitle> findByCategoryOrderBySeqDesc(String category);
}
