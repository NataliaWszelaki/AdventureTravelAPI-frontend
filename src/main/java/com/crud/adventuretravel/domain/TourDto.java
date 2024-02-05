package com.crud.adventuretravel.domain;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class TourDto implements DtoHandler {

    private long id;
    private String name;
    private String country;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String startLocation;
    private String endLocation;
    private Double priceEuro;
    private Double pricePln;

    @Override
    public String endpointName() {

        return "tours";
    }

    @Override
    public DtoHandler newDtoHandler() {

        return new TourDto();
    }

    public long getId() {

        return id;
    }
}
