package com.crud.adventuretravel.service;

import com.crud.adventuretravel.backendClient.AttractionBackendClient;
import com.crud.adventuretravel.backendClient.BackendRequestException;
import com.crud.adventuretravel.domain.AttractionDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttractionService {

    private static AttractionBackendClient attractionBackendClient;
    private static AttractionService attractionService;

    public AttractionService(AttractionBackendClient attractionBackendClient) {

        AttractionService.attractionBackendClient = attractionBackendClient;
    }

    public static AttractionService getInstance() {

        if (attractionService == null) {
            attractionService = new AttractionService(attractionBackendClient);
        }
        return attractionService;
    }

    public List<AttractionDto> getAllAttractions() {

        return attractionBackendClient.getAttractionsList();
    }

    public AttractionDto getAttractionById(Long id) {

        return attractionBackendClient.getAttractionById(id);
    }

    public void saveAttractionDto(AttractionDto AttractionDto) throws BackendRequestException {

        attractionBackendClient.post(AttractionDto);
    }

    public void updateAttractionDto(AttractionDto attractionDto) throws BackendRequestException {

        attractionBackendClient.put(attractionDto);
    }

    public void updateAttractionDtoDeactivate(AttractionDto attractionDto) throws BackendRequestException {

        attractionBackendClient.putAttractionDeactivate(attractionDto);
    }

    public void deleteAttractionDto(AttractionDto attractionDto) throws BackendRequestException, ReferentialIntegrityViolationException {

        long numberOfReservationsWithTour = ReservationService.getInstance().getAllReservations().stream()
                .flatMap(r -> r.getAttractionLongSet().stream())
                .filter(r -> r.equals(attractionDto.getId()))
                .count();


        if(numberOfReservationsWithTour == 0) {
            attractionBackendClient.delete(attractionDto);
        } else {
            throw new ReferentialIntegrityViolationException("The data you are trying to delete is associated with other records " +
                    "and cannot be deleted to maintain data integrity. The data may by ony deactivated");
        }
    }

    public List<AttractionDto> findByCity(String city) {

        List<AttractionDto> AttractionDtoList = getAllAttractions();
        return AttractionDtoList.stream()
                .filter(c -> c.getCity().toLowerCase().contains(city.toLowerCase()))
                .collect(Collectors.toList());
    }
}
