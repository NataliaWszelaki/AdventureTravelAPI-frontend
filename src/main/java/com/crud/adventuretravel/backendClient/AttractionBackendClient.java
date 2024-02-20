package com.crud.adventuretravel.backendClient;

import com.crud.adventuretravel.config.BackendConfig;
import com.crud.adventuretravel.domain.AttractionDto;
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
public class AttractionBackendClient {

    private final RestTemplate restTemplate;
    private final BackendConfig backendConfig;

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
            log.error("An error occurred: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public AttractionDto getAttractionById(long id) {

        URI url = UriComponentsBuilder
                .fromHttpUrl(backendConfig.getBackendApiEndpoint() + "/attractions/" + id)
                .build()
                .encode()
                .toUri();
        try {
            AttractionDto responseDto = restTemplate.getForObject(url, AttractionDto.class);
            return Optional.ofNullable(responseDto)
                    .orElseGet(() -> new AttractionDto());
        } catch (RestClientException e) {
            log.error("An error occurred: {}", e.getMessage(), e);
            return new AttractionDto();
        }
    }

    public void post(AttractionDto attractionDto) throws BackendRequestException {

        URI url = UriComponentsBuilder
                .fromHttpUrl(backendConfig.getBackendApiEndpoint() + "/attractions")
                .build()
                .encode()
                .toUri();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<AttractionDto> request = new HttpEntity<>(attractionDto, headers);
            restTemplate.postForObject(url, request, AttractionDto.class);
        } catch (RestClientException e) {
            log.error("An error occurred: {}", e.getMessage(), e);
            throw new BackendRequestException(e);
        }
    }

    public void put(AttractionDto attractionDto) throws BackendRequestException {

        URI url = UriComponentsBuilder
                .fromHttpUrl(backendConfig.getBackendApiEndpoint() + "/attractions")
                .build()
                .encode()
                .toUri();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<AttractionDto> request = new HttpEntity<>(attractionDto, headers);
            restTemplate.put(url, request);
        } catch (RestClientException e) {
            log.error("An error occurred: {}", e.getMessage(), e);
            throw new BackendRequestException(e);
        }
    }

    public void putAttractionDeactivate(AttractionDto attractionDto) throws BackendRequestException {

        URI url = UriComponentsBuilder
                .fromHttpUrl(backendConfig.getBackendApiEndpoint() + "/attractions/deactivate")
                .build()
                .encode()
                .toUri();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<AttractionDto> request = new HttpEntity<>(attractionDto, headers);
            restTemplate.put(url, request);
        } catch (RestClientException e) {
            log.error("An error occurred: {}", e.getMessage(), e);
            throw new BackendRequestException(e);
        }
    }

    public void delete(AttractionDto attractionDto) throws BackendRequestException {

        URI url = UriComponentsBuilder
                .fromHttpUrl(backendConfig.getBackendApiEndpoint() + "/attractions/" + attractionDto.getId())
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

