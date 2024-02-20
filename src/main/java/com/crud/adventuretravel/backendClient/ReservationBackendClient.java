package com.crud.adventuretravel.backendClient;

import com.crud.adventuretravel.config.BackendConfig;
import com.crud.adventuretravel.domain.ReservationDto;
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
public class ReservationBackendClient {

    private final RestTemplate restTemplate;
    private final BackendConfig backendConfig;

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
            log.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public ReservationDto getReservationById(long id) {

        URI url = UriComponentsBuilder
                .fromHttpUrl(backendConfig.getBackendApiEndpoint() + "/" + "reservations" + "/" + id)
                .build()
                .encode()
                .toUri();
        try {
            ReservationDto responseDto = restTemplate.getForObject(url, ReservationDto.class);
            return Optional.ofNullable(responseDto)
                    .orElseGet(() -> new ReservationDto());
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
            return new ReservationDto();
        }
    }

    public void post(ReservationDto reservationDto) throws BackendRequestException {

        URI url = UriComponentsBuilder
                .fromHttpUrl(backendConfig.getBackendApiEndpoint() + "/reservations")
                .build()
                .encode()
                .toUri();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<ReservationDto> request = new HttpEntity<>(reservationDto, headers);
            restTemplate.postForObject(url, request, ReservationDto.class);
        } catch (RestClientException e) {
            log.error("An error occurred: {}", e.getMessage(), e);
            throw new BackendRequestException(e);
        }
    }

    public void put(ReservationDto reservationDto) throws BackendRequestException {

        URI url = UriComponentsBuilder
                .fromHttpUrl(backendConfig.getBackendApiEndpoint() + "/reservations")
                .build()
                .encode()
                .toUri();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<ReservationDto> request = new HttpEntity<>(reservationDto, headers);
            restTemplate.put(url, request);
        } catch (RestClientException e) {
            log.error("An error occurred: {}", e.getMessage(), e);
            throw new BackendRequestException(e);
        }
    }

    public void putReservationDeactivate(ReservationDto reservationDto) throws BackendRequestException {

        URI url = UriComponentsBuilder
                .fromHttpUrl(backendConfig.getBackendApiEndpoint() + "/reservations/deactivate")
                .build()
                .encode()
                .toUri();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<ReservationDto> request = new HttpEntity<>(reservationDto, headers);
            restTemplate.put(url, request);
        } catch (RestClientException e) {
            log.error("An error occurred: {}", e.getMessage(), e);
            throw new BackendRequestException(e);
        }
    }

    public void delete(long reservationId) throws BackendRequestException {

        URI url = UriComponentsBuilder
                .fromHttpUrl(backendConfig.getBackendApiEndpoint() + "/reservations/" + reservationId)
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

