package com.crud.adventuretravel.domain;

import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class ReservationDto implements DtoHandler {

    private long id;
    private long tourId;
    private long customerId;
    private Set<Long> attractionLongSet;
    private LocalDate reservationDate;
    private PaymentStatus paymentStatus;
    private ReservationStatus reservationStatus;

    @Override
    public String endpointName() {

        return "reservations";
    }

    @Override
    public DtoHandler newDtoHandler() {

        return new ReservationDto();
    }
}
