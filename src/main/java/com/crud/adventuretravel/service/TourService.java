package com.crud.adventuretravel.service;

import com.crud.adventuretravel.backendClient.BackendRequestException;
import com.crud.adventuretravel.backendClient.TourBackendClient;
import com.crud.adventuretravel.domain.TourDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TourService {

    private static TourBackendClient tourBackendClient;
    private static TourService tourService;

    public TourService(TourBackendClient tourBackendClient) {

        TourService.tourBackendClient = tourBackendClient;
    }

    public static TourService getInstance() {

        if (tourService == null) {
            tourService = new TourService(tourBackendClient);
        }
        return tourService;
    }

    public List<TourDto> getAllTours() {

        return tourBackendClient.getToursList();
    }

    public TourDto getTourById(long tourId) {

        return tourBackendClient.getTourById(tourId);
    }

    public void saveTourDto(TourDto tourDto) throws BackendRequestException {

        tourBackendClient.post(tourDto);
    }

    public void updateTourDto(TourDto tourDto) throws BackendRequestException {

        tourBackendClient.put(tourDto);
    }

    public void updateTourDtoDeactivate(TourDto tourDto) throws BackendRequestException {

        tourBackendClient.putTourDeactivate(tourDto);
    }

    public void deleteTourDto(TourDto tourDto) throws BackendRequestException, ReferentialIntegrityViolationException {

        long numberOfReservationsWithTour = ReservationService.getInstance().getAllReservations().stream()
                .filter(r -> r.getTourId() == tourDto.getId())
                .count();

        if(numberOfReservationsWithTour == 0) {
            tourBackendClient.delete(tourDto);
        } else {
            throw new ReferentialIntegrityViolationException("The data you are trying to delete is associated with other records " +
                    "and cannot be deleted to maintain data integrity. The data may by ony deactivated");
        }
    }

    public List<TourDto> findByName(String name) {

        List<TourDto> TourDtoList = getAllTours();
        return TourDtoList.stream()
                .filter(t -> t.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
}
