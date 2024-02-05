package com.crud.adventuretravel.domain;

import lombok.Getter;

@Getter
public enum ReservationStatus {

    NEW,
    CONFIRMED,
    PENDING,
    CANCELED;
}
