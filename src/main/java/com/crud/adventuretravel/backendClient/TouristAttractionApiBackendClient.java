package com.crud.adventuretravel.backendClient;

import com.crud.adventuretravel.config.BackendConfig;
import com.crud.adventuretravel.domain.AttractionDetailsDto;
import com.crud.adventuretravel.domain.LocationSearchDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class TouristAttractionApiBackendClient {

    private final RestTemplate restTemplate;
    private final BackendConfig backendConfig;
    public List<LocationSearchDto> getSearchedLocations(String text) {

        URI url = UriComponentsBuilder
                .fromHttpUrl(backendConfig.getBackendApiEndpoint() + "/tourist-attractions/location/" + text)
                .build()
                .encode()
                .toUri();
        try {
            LocationSearchDto[] boardsResponse = restTemplate.getForObject(url, LocationSearchDto[].class);
            return Optional.ofNullable(boardsResponse)
                    .map(Arrays::asList)
                    .orElse(Collections.emptyList());
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public List<AttractionDetailsDto> getAttractionDetails(int locationId) {

        URI url = UriComponentsBuilder
                .fromHttpUrl(backendConfig.getBackendApiEndpoint() + "/tourist-attractions/details/" + locationId)
                .build()
                .encode()
                .toUri();
        try {
            AttractionDetailsDto[] boardsResponse = restTemplate.getForObject(url, AttractionDetailsDto[].class);
            return Optional.ofNullable(boardsResponse)
                    .map(Arrays::asList)
                    .orElse(Collections.emptyList());
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);

            return Collections.emptyList();
        }
    }
}
