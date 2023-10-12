package com.mever.api.domain.playList.repository;

import com.mever.api.domain.playList.entity.PlayList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayListRepository extends JpaRepository<PlayList, Long> {
    public List<PlayList> findAll();
    public List<PlayList> findByHomePage(String homePage);

    public void deleteByVideoIdAndHomePage(String vedioId,String group);
}
