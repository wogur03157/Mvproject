package com.mever.api.domain.mainAdmin.repository;

import com.mever.api.domain.analytics.dto.AnalyticsDto;
import com.mever.api.domain.mainAdmin.entity.Menu;
import com.mever.api.domain.member.dto.MemberReq;
import com.sun.tools.javac.Main;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MainMapper {
    List<Main> getMenuList();
    List<MemberReq> getRanking();
    AnalyticsDto getRate();
}
