package com.crud.adventuretravel.service;

import com.crud.adventuretravel.backend.BackendClient;
import com.crud.adventuretravel.backend.BackendRequestException;
import com.crud.adventuretravel.domain.TourDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TourService {

    private static BackendClient backendClient;
    private static TourService tourService;

    public TourService(BackendClient backendClient) {

        TourService.backendClient = backendClient;
    }

    public static TourService getInstance() {

        if (tourService == null) {
            tourService = new TourService(backendClient);
        }
        return tourService;
    }

    public List<TourDto> getAllTours() {

        return backendClient.getToursList();
    }

    public void saveTourDto(TourDto tourDto) throws BackendRequestException {

        backendClient.post(tourDto);
    }

    public List<TourDto> findByName(String name) {

        List<TourDto> TourDtoList = getAllTours();
        return TourDtoList.stream()
                .filter(c -> c.getName().contains(name))
                .collect(Collectors.toList());
    }

    public void deleteTourDto(TourDto tourDto) {

        backendClient.delete(tourDto);
    }

    public void updateTourDto(TourDto tourDto) throws BackendRequestException {

        backendClient.put(tourDto);
    }

    public TourDto getTourById(long tourId) {

        return backendClient.getTourById(tourId);
    }
}
