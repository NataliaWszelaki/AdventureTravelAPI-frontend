package com.crud.adventuretravel.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class CustomerDto implements DtoHandler {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private int phoneNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate accountCreationDate;

    @Override
    public String endpointName() {

        return "customers";
    }

    @Override
    public DtoHandler newDtoHandler() {

        return new CustomerDto();
    }

    public String getFullName() {

        return getFirstName() + " " + getLastName();
    }
}

