package com.example.deal.mapper;

import com.example.deal.dto.CreditDTO;
import com.example.deal.model.Credit;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CreditMapper {
    CreditMapper INSTANCE = Mappers.getMapper(CreditMapper.class);

    Credit from(CreditDTO dto);
}
