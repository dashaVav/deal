package com.example.deal.mappers;

import com.example.deal.dtos.FinishRegistrationRequestDTO;
import com.example.deal.dtos.LoanApplicationRequestDTO;
import com.example.deal.model.Client;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ClientMapper {
    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    @Mappings({
            @Mapping(target = "passport.series", source = "passportSeries"),
            @Mapping(target = "passport.number", source = "passportNumber")
    })
    Client from(LoanApplicationRequestDTO dto);

    @Mappings({
            @Mapping(target = "gender", source = "request.gender"),
            @Mapping(target = "maritalStatus", source = "request.maritalStatus"),
            @Mapping(target = "dependentAmount", source = "request.dependentAmount"),
            @Mapping(target = "employment", source = "request.employment"),
            @Mapping(target = "account", source = "request.account"),
            @Mapping(target = "passport.series", source = "sourceClient.passport.series"),
            @Mapping(target = "passport.number", source = "sourceClient.passport.number"),
            @Mapping(target = "passport.issueDate", source = "request.passportIssueDate"),
            @Mapping(target = "passport.issueBranch", source = "request.passportIssueBranch")
    })
    Client from(Client sourceClient, FinishRegistrationRequestDTO request);
}
