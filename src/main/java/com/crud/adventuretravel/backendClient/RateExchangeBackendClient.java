package com.crud.adventuretravel.backendClient;

import com.crud.adventuretravel.config.BackendConfig;
import com.crud.adventuretravel.domain.RateExchangeDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RateExchangeBackendClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(RateExchangeBackendClient.class);
    private final RestTemplate restTemplate;
    private final BackendConfig backendConfig;

    public RateExchangeDto getRateExchange() {

        URI url = UriComponentsBuilder
                .fromHttpUrl(backendConfig.getBackendApiEndpoint() + "/" + "rate_exchange")
                .build()
                .encode()
                .toUri();
        try {
            RateExchangeDto responseDto = restTemplate.getForObject(url, RateExchangeDto.class);
            return Optional.ofNullable(responseDto)
                    .orElseGet(() -> new RateExchangeDto());
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new RateExchangeDto();
        }
    }
}

