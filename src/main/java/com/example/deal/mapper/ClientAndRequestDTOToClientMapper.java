package com.example.deal.mapper;

import com.example.deal.dto.FinishRegistrationRequestDTO;
import com.example.deal.model.Client;
import org.jetbrains.annotations.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(componentModel = "spring")
public interface ClientAndRequestDTOToClientMapper extends Converter<FinishRegistrationRequestDTO, Client> {
    @Mapping(target = "passport.issueDate", source = "passportIssueDate")
    @Mapping(target = "passport.issueBranch", source = "passportIssueBranch")
    Client convert(@NotNull FinishRegistrationRequestDTO request);
}