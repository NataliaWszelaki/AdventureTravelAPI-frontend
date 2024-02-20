package com.crud.adventuretravel.domain;

import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class ReservationDto {

    private long id;
    private long tourId;
    private long customerId;
    private Set<Long> attractionLongSet;
    private LocalDate reservationDate;
    private PaymentStatus paymentStatus;
    private ReservationStatus reservationStatus;

    public ReservationDto(long tourId, long customerId, Set<Long> attractionLongSet, LocalDate reservationDate,
                          PaymentStatus paymentStatus, ReservationStatus reservationStatus) {
        this.tourId = tourId;
        this.customerId = customerId;
        this.attractionLongSet = attractionLongSet;
        this.reservationDate = reservationDate;
        this.paymentStatus = paymentStatus;
        this.reservationStatus = reservationStatus;
    }
}
