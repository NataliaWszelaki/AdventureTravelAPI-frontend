package com.crud.adventuretravel.backendClient;

import org.springframework.web.client.RestClientException;

public class BackendRequestException extends Throwable {
    public BackendRequestException(RestClientException e) {
        super(e.getMessage());
    }
}
