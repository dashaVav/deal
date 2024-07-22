package com.example.deal.mapper;

import com.example.deal.dto.LoanApplicationRequestDTO;
import com.example.deal.model.Client;
import org.jetbrains.annotations.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface LoanApplicationRequestDTOToClientMapper extends Converter<LoanApplicationRequestDTO, Client> {
    @Mappings({
            @Mapping(target = "passport.series", source = "passportSeries"),
            @Mapping(target = "passport.number", source = "passportNumber")
    })
    Client convert(@NotNull LoanApplicationRequestDTO dto);
}
