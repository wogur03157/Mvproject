package com.mever.api.domain.member.repository;

import com.mever.api.domain.member.dto.LotteryDto;
import com.mever.api.domain.member.dto.MemberRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {
    List<MemberRes> getMemberList(String category) ;
    List<LotteryDto> getLotteryList() ;
}
