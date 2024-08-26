package com.example.deal.mapper;

import com.example.deal.dto.CreditDTO;
import com.example.deal.model.Credit;
import org.jetbrains.annotations.NotNull;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface CreditDTOToCreditMapper extends Converter<CreditDTO, Credit> {
    Credit convert(@NotNull CreditDTO dto);
}
