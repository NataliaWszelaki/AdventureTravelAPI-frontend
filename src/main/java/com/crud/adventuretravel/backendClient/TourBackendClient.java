package com.crud.adventuretravel.backendClient;

import com.crud.adventuretravel.config.BackendConfig;
import com.crud.adventuretravel.domain.TourDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class TourBackendClient {

    private final RestTemplate restTemplate;
    private final BackendConfig backendConfig;

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
            log.error(e.getMessage(), e);
            return Collections.emptyList();
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
            log.error(e.getMessage(), e);
            return new TourDto();
        }
    }

    public void post(TourDto tourDto) throws BackendRequestException {

        URI url = UriComponentsBuilder
                .fromHttpUrl(backendConfig.getBackendApiEndpoint() + "/tours")
                .build()
                .encode()
                .toUri();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<TourDto> request = new HttpEntity<>(tourDto, headers);
            restTemplate.postForObject(url, request, TourDto.class);
        } catch (RestClientException e) {
            log.error("An error occurred: {}", e.getMessage(), e);
            throw new BackendRequestException(e);
        }
    }

    public void put(TourDto tourDto) throws BackendRequestException {

        URI url = UriComponentsBuilder
                .fromHttpUrl(backendConfig.getBackendApiEndpoint() + "/tours")
                .build()
                .encode()
                .toUri();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<TourDto> request = new HttpEntity<>(tourDto, headers);
            restTemplate.put(url, request);
        } catch (RestClientException e) {
            log.error("An error occurred: {}", e.getMessage(), e);
            throw new BackendRequestException(e);
        }
    }

    public void putTourDeactivate(TourDto tourDto) throws BackendRequestException {

        URI url = UriComponentsBuilder
                .fromHttpUrl(backendConfig.getBackendApiEndpoint() + "/tours/deactivate")
                .build()
                .encode()
                .toUri();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<TourDto> request = new HttpEntity<>(tourDto, headers);
            restTemplate.put(url, request);
        } catch (RestClientException e) {
            log.error("An error occurred: {}", e.getMessage(), e);
            throw new BackendRequestException(e);
        }
    }

    public void delete(TourDto tourDto) throws BackendRequestException {

        URI url = UriComponentsBuilder
                .fromHttpUrl(backendConfig.getBackendApiEndpoint() + "/tours/" + tourDto.getId())
                .build()
                .encode()
                .toUri();
        try {
            restTemplate.delete(url);
        } catch (RestClientException e) {
            log.error("An error occurred: {}", e.getMessage(), e);
            throw new BackendRequestException(e);
        }
    }
}

