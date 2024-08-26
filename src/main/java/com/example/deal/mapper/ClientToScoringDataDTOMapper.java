package com.example.deal.mapper;

import com.example.deal.dto.ScoringDataDTO;
import com.example.deal.model.Client;
import org.jetbrains.annotations.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface ClientToScoringDataDTOMapper extends Converter<Client, ScoringDataDTO> {
    @Mapping(target = "passportSeries", source = "passport.series")
    @Mapping(target = "passportNumber", source = "passport.number")
    @Mapping(target = "passportIssueDate", source = "passport.issueDate")
    @Mapping(target = "passportIssueBranch", source = "passport.issueBranch")
    ScoringDataDTO convert(@NotNull Client client);
}
