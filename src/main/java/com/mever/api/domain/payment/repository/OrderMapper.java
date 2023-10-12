package com.mever.api.domain.payment.repository;

import com.mever.api.domain.payment.dto.PaymentResHandleDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {
    List<PaymentResHandleDto> getPayList() ;
    List<PaymentResHandleDto> getPartPayList(String category);
    List<PaymentResHandleDto> getSubscriptionList(@Param("email") String email,@Param("phone") String phone);
    List<PaymentResHandleDto> setSubCancel();
}
