package com.crud.adventuretravel.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationSearchDto {

    private int location_id;
    private String name;
    private String ancestor;
    private String country;

    public LocationSearchDto(String name, String ancestor, String country) {
        this.name = name;
        this.ancestor = ancestor;
        this.country = country;
    }
}
