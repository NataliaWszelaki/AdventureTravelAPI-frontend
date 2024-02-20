package com.crud.adventuretravel.domain;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class RateExchangeDto {

    private long id;
    private double pln;
    private LocalDate rateExchangeDate;
}
