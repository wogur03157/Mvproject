package com.mever.api.domain.email.repository;

import com.mever.api.domain.email.dto.ReservationEmailDto;
import com.mever.api.domain.payment.dto.PaymentResHandleDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmailMapper {
    List<ReservationEmailDto> getEmailList() ;
}
