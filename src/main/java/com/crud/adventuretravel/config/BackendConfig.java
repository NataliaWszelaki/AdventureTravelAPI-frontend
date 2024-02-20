package com.crud.adventuretravel.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class BackendConfig {

    @Value("${backend.api.endpoint.prod}")
    private String backendApiEndpoint;
}
