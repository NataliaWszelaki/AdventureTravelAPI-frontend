package com.crud.adventuretravel.domain;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class AttractionDto implements DtoHandler {

    long id;
    private String name;
    private String city;
    private String description;
    private Double priceEuro;
    private Double pricePln;

    @Override
    public String endpointName() {

        return "attractions";
    }

    @Override
    public DtoHandler newDtoHandler() {

        return new AttractionDto();
    }
}
