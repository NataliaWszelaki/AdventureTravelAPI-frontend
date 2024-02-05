package com.crud.adventuretravel.backend;

import com.crud.adventuretravel.domain.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BackendClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(BackendClient.class);
    private final RestTemplate restTemplate;
    private final BackendConfig backendConfig;


    public AttractionDto getAttractionById(long id) {

        URI url = UriComponentsBuilder
                .fromHttpUrl(backendConfig.getBackendApiEndpoint() + "/" + "attractions" + "/" + id)
                .build()
                .encode()
                .toUri();
        try {
            AttractionDto responseDto = restTemplate.getForObject(url, AttractionDto.class);
            return Optional.ofNullable(responseDto)
                    .orElseGet(() -> new AttractionDto());
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new AttractionDto();
        }
    }

    public CustomerDto getCustomerById(long id) {

        URI url = UriComponentsBuilder
                .fromHttpUrl(backendConfig.getBackendApiEndpoint() + "/" + "customers" + "/" + id)
                .build()
                .encode()
                .toUri();
        try {
            CustomerDto responseDto = restTemplate.getForObject(url, CustomerDto.class);
            return Optional.ofNullable(responseDto)
                    .orElseGet(() -> new CustomerDto());
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new CustomerDto();
        }
    }

    public TourDto getTourById(long id) {

        URI url = UriComponentsBuilder
                .fromHttpUrl(backendConfig.getBackendApiEndpoint() + "/" + "tours" + "/" + id)
                .build()
                .encode()
                .toUri();
        try {
            TourDto responseDto = restTemplate.getForObject(url, TourDto.class);
            return Optional.ofNullable(responseDto)
                    .orElseGet(() -> new TourDto());
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new TourDto();
        }
    }

    public void post(DtoHandler dtoHandler) {

        URI url = UriComponentsBuilder
                .fromHttpUrl(backendConfig.getBackendApiEndpoint() + "/" + dtoHandler.endpointName())
                .build()
                .encode()
                .toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<DtoHandler> request = new HttpEntity<>(dtoHandler, headers);
        restTemplate.postForObject(url, request, DtoHandler.class);
    }

    public void put(DtoHandler dtoHandler) throws BackendRequestException {

        try {
            URI url = UriComponentsBuilder
                    .fromHttpUrl(backendConfig.getBackendApiEndpoint() + "/" + dtoHandler.endpointName())
                    .build()
                    .encode()
                    .toUri();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<DtoHandler> request = new HttpEntity<>(dtoHandler, headers);
            restTemplate.put(url, request);
        } catch (RestClientException e) {
            throw new BackendRequestException("Error", e);
        }
    }

    public void delete(DtoHandler dtoHandler) {

        URI url = UriComponentsBuilder
                .fromHttpUrl(backendConfig.getBackendApiEndpoint() + "/" + dtoHandler.endpointName()+ "/" + dtoHandler.getId())
                .build()
                .encode()
                .toUri();
        restTemplate.delete(url);
    }

    public List<AttractionDto> getAttractionsList() {

        URI url = UriComponentsBuilder
                .fromHttpUrl(backendConfig.getBackendApiEndpoint() + "/attractions")
                .build()
                .encode()
                .toUri();
        try {
            AttractionDto[] boardsResponse = restTemplate.getForObject(url, AttractionDto[].class);
            return Optional.ofNullable(boardsResponse)
                    .map(Arrays::asList)
                    .orElse(Collections.emptyList());
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<CustomerDto> getCustomersList() {

        URI url = UriComponentsBuilder
                .fromHttpUrl(backendConfig.getBackendApiEndpoint() + "/customers")
                .build()
                .encode()
                .toUri();
        try {
            CustomerDto[] boardsResponse = restTemplate.getForObject(url, CustomerDto[].class);
            return Optional.ofNullable(boardsResponse)
                    .map(Arrays::asList)
                    .orElse(Collections.emptyList());
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<TourDto> getToursList() {

        URI url = UriComponentsBuilder
                .fromHttpUrl(backendConfig.getBackendApiEndpoint() + "/tours")
                .build()
                .encode()
                .toUri();
        try {
            TourDto[] boardsResponse = restTemplate.getForObject(url, TourDto[].class);
            return Optional.ofNullable(boardsResponse)
                    .map(Arrays::asList)
                    .orElse(Collections.emptyList());
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<ReservationDto> getReservationsList() {

        URI url = UriComponentsBuilder
                .fromHttpUrl(backendConfig.getBackendApiEndpoint() + "/reservations")
                .build()
                .encode()
                .toUri();
        try {
            ReservationDto[] boardsResponse = restTemplate.getForObject(url, ReservationDto[].class);
            return Optional.ofNullable(boardsResponse)
                    .map(Arrays::asList)
                    .orElse(Collections.emptyList());
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}

