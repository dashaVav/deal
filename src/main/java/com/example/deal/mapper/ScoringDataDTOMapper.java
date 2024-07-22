package com.example.deal.mapper;

import com.example.deal.dto.FinishRegistrationRequestDTO;
import com.example.deal.dto.LoanOfferDTO;
import com.example.deal.dto.ScoringDataDTO;
import com.example.deal.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ScoringDataDTOMapper {
    ScoringDataDTOMapper INSTANCE = Mappers.getMapper(ScoringDataDTOMapper.class);

    @Mappings({
            @Mapping(target = "gender", source = "request.gender"),
            @Mapping(target = "maritalStatus", source = "request.maritalStatus"),
            @Mapping(target = "dependentAmount", source = "request.dependentAmount"),
            @Mapping(target = "employment", source = "request.employment"),
            @Mapping(target = "account", source = "request.account"),
            @Mapping(target = "passportSeries", source = "client.passport.series"),
            @Mapping(target = "passportNumber", source = "client.passport.number"),
            @Mapping(target = "passportIssueBranch", source = "request.passportIssueBranch"),
            @Mapping(target = "amount", source = "loanOffer.requestedAmount")
    })
    ScoringDataDTO convert(Client client, FinishRegistrationRequestDTO request, LoanOfferDTO loanOffer);
}
