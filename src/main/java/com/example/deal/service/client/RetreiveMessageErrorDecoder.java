package com.example.deal.service.client;

import com.example.deal.exception.ConveyorException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
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
            Exception exception = errorDecoder.decode(methodKey, response);
            log.error("Deal service return exception: {}", exception.getMessage());
            return exception;
        }
    }
}
