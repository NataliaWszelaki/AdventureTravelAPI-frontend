package com.crud.adventuretravel.service;

import com.crud.adventuretravel.backendClient.TouristAttractionApiBackendClient;
import com.crud.adventuretravel.domain.AttractionDetailsDto;
import com.crud.adventuretravel.domain.LocationSearchDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TouristAttractionApiService {

    private static TouristAttractionApiBackendClient touristAttractionApiBackendClient;
    private static TouristAttractionApiService touristAttractionApiService;

    public TouristAttractionApiService(TouristAttractionApiBackendClient touristAttractionApiBackendClient) {

        TouristAttractionApiService.touristAttractionApiBackendClient = touristAttractionApiBackendClient;
    }

    public static TouristAttractionApiService getInstance() {

        if (touristAttractionApiService == null) {
            touristAttractionApiService = new TouristAttractionApiService(touristAttractionApiBackendClient);
        }
        return touristAttractionApiService;
    }

    public List<LocationSearchDto> getAllSearchedLocations(String text) {

        return touristAttractionApiBackendClient.getSearchedLocations(text);
    }

    public List<AttractionDetailsDto> getAllAttractionDetails(int locationId) {

        return touristAttractionApiBackendClient.getAttractionDetails(locationId);
    }
}
