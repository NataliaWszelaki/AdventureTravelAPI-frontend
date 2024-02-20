package com.crud.adventuretravel.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttractionDetailsDto {

    private int location_id;
    private String city;
    private String name;
    private String description;
    private String category;
    private String title;
    private int priceEuro;
}
