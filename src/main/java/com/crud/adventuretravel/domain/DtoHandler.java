package com.crud.adventuretravel.domain;

public interface DtoHandler {
    long getId();
    String endpointName();

    DtoHandler newDtoHandler();
}
