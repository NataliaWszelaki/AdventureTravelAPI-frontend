package com.crud.adventuretravel.backend;

import org.springframework.web.client.RestClientException;

public class BackendRequestException extends Throwable {
    public BackendRequestException(String error, RestClientException e) {
    }
}
