package com.crud.adventuretravel.domain;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class AttractionDto {

    long id;
    private int location_id;
    private String city;
    private String name;
    private String description;
    private String category;
    private String title;
    private Double priceEuro;

    public AttractionDto(int location_id, String city, String name, String description, String category, String title, Double priceEuro) {
        this.location_id = location_id;
        this.city = city;
        this.name = name;
        this.description = description;
        this.category = category;
        this.title = title;
        this.priceEuro = priceEuro;
    }

    public String getCityAndName() {

        return getCity() + " - " + getName();
    }
}
