package com.crud.adventuretravel.backendClient;

import com.crud.adventuretravel.config.BackendConfig;
import com.crud.adventuretravel.domain.CustomerDto;
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
public class CustomerBackendClient {

    private final RestTemplate restTemplate;
    private final BackendConfig backendConfig;

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
            log.error(e.getMessage(), e);
            return Collections.emptyList();
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
            log.error(e.getMessage(), e);
            return new CustomerDto();
        }
    }

    public void post(CustomerDto customerDto) throws BackendRequestException {

        URI url = UriComponentsBuilder
                .fromHttpUrl(backendConfig.getBackendApiEndpoint() + "/customers")
                .build()
                .encode()
                .toUri();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<CustomerDto> request = new HttpEntity<>(customerDto, headers);
            restTemplate.postForObject(url, request, CustomerDto.class);
        } catch (RestClientException e) {
            log.error("An error occurred: {}", e.getMessage(), e);
            throw new BackendRequestException(e);
        }
    }

    public void put(CustomerDto customerDto) throws BackendRequestException {

        URI url = UriComponentsBuilder
                .fromHttpUrl(backendConfig.getBackendApiEndpoint() + "/customers")
                .build()
                .encode()
                .toUri();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<CustomerDto> request = new HttpEntity<>(customerDto, headers);
            restTemplate.put(url, request);
        } catch (RestClientException e) {
            log.error("An error occurred: {}", e.getMessage(), e);
            throw new BackendRequestException(e);
        }
    }

    public void putCustomerDeactivate(CustomerDto customerDto) throws BackendRequestException {

        URI url = UriComponentsBuilder
                .fromHttpUrl(backendConfig.getBackendApiEndpoint() + "/customers/deactivate")
                .build()
                .encode()
                .toUri();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<CustomerDto> request = new HttpEntity<>(customerDto, headers);
            restTemplate.put(url, request);
        } catch (RestClientException e) {
            log.error("An error occurred: {}", e.getMessage(), e);
            throw new BackendRequestException(e);
        }
    }

    public void delete(CustomerDto customerDto) throws BackendRequestException {

        URI url = UriComponentsBuilder
                .fromHttpUrl(backendConfig.getBackendApiEndpoint() + "/customers/" + customerDto.getId())
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

