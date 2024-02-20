package com.crud.adventuretravel.service;

import com.crud.adventuretravel.backendClient.BackendRequestException;
import com.crud.adventuretravel.backendClient.ReservationBackendClient;
import com.crud.adventuretravel.domain.ReservationDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private static ReservationBackendClient reservationBackendClient;
    private static ReservationService reservationService;

    public ReservationService(ReservationBackendClient reservationBackendClient) {

        ReservationService.reservationBackendClient = reservationBackendClient;
    }

    public static ReservationService getInstance() {

        if (reservationService == null) {
            reservationService = new ReservationService(reservationBackendClient);
        }
        return reservationService;
    }

    public List<ReservationDto> getAllReservations() {

        return reservationBackendClient.getReservationsList();
    }

    public ReservationDto getReservationById(long reservationId) {

        return reservationBackendClient.getReservationById(reservationId);
    }

    public void saveReservationDto(ReservationDto reservationDto) throws BackendRequestException {

        System.out.println(reservationDto.getTourId());
        reservationBackendClient.post(reservationDto);
    }

    public void updateReservationDto(ReservationDto reservationDto) throws BackendRequestException {

        reservationBackendClient.put(reservationDto);
    }

    public void updateReservationDtoDeactivate(ReservationDto reservationDto) throws BackendRequestException {

        reservationBackendClient.putReservationDeactivate(reservationDto);
    }

    public void deleteReservationDto(long reservationId) throws BackendRequestException {

        reservationBackendClient.delete(reservationId);
    }

    public List<ReservationDto> findByName(String name) {

        List<ReservationDto> reservationDtoList = getAllReservations();
        return reservationDtoList.stream()
                .map(r -> CustomerService.getInstance().getCustomerById(r.getCustomerId()))
                .filter(c -> c.getFullName().toLowerCase().contains(name.toLowerCase()))
                .map(customer -> reservationDtoList.stream()
                        .filter(r -> r.getCustomerId() == customer.getId())
                        .findFirst()
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
