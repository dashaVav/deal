package com.example.deal.mapper;

import com.example.deal.dto.FinishRegistrationRequestDTO;
import com.example.deal.dto.LoanOfferDTO;
import com.example.deal.dto.ScoringDataDTO;
import com.example.deal.model.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomConversionService {
    private final ConversionService conversionService;

    public <T, V> V convert(T dto, Class<V> targetType) {
        return conversionService.convert(dto, targetType);
    }

    public Client convert(Client sourceClient, FinishRegistrationRequestDTO request) {
        return ClientAndRequestDTOToClientMapper.INSTANCE.convert(sourceClient, request);
    }

    public ScoringDataDTO convert(Client client, FinishRegistrationRequestDTO request, LoanOfferDTO loanOffer) {
        return ScoringDataDTOMapper.INSTANCE.convert(client, request, loanOffer);
    }
}
