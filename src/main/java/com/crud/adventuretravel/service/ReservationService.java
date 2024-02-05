package com.crud.adventuretravel.service;

import com.crud.adventuretravel.backend.BackendClient;
import com.crud.adventuretravel.backend.BackendRequestException;
import com.crud.adventuretravel.domain.ReservationDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private static BackendClient backendClient;
    private static ReservationService reservationService;

    public ReservationService(BackendClient backendClient) {

        ReservationService.backendClient = backendClient;
    }

    public static ReservationService getInstance() {

        if (reservationService == null) {
            reservationService = new ReservationService(backendClient);
        }
        return reservationService;
    }

    public List<ReservationDto> getAllReservations() {

        return backendClient.getReservationsList();
    }

    public void saveReservationDto(ReservationDto reservationDto) throws BackendRequestException {

        System.out.println(reservationDto.getTourId());
        backendClient.post(reservationDto);
    }

    public List<ReservationDto> findByName(String name) {

        List<ReservationDto> reservationDtoList = getAllReservations();
        return reservationDtoList.stream()
                .map(r -> CustomerService.getInstance().getCustomerById(r.getCustomerId()))
                .filter(c -> c.getFullName().contains(name))
                .map(customer -> reservationDtoList.stream()
                        .filter(r -> r.getCustomerId() == customer.getId())
                        .findFirst()
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void deleteReservationDto(ReservationDto reservationDto) {

        backendClient.delete(reservationDto);
    }

    public void updateReservationDto(ReservationDto reservationDto) throws BackendRequestException {

        backendClient.put(reservationDto);
    }
}
