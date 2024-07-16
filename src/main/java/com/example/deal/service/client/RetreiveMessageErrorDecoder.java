package com.example.deal.service.client;

import com.example.deal.exception.ConveyorException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class RetreiveMessageErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        try (InputStream bodyIs = response.body().asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            ConveyorExceptionDTO e = mapper.readValue(bodyIs, ConveyorExceptionDTO.class);
            throw new ConveyorException(e.getError(), e.getStatus());
        } catch (IOException e) {
            return errorDecoder.decode(methodKey, response);
        }
    }
}
