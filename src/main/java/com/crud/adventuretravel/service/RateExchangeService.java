package com.crud.adventuretravel.service;

import com.crud.adventuretravel.backendClient.RateExchangeBackendClient;
import com.crud.adventuretravel.domain.RateExchangeDto;
import org.springframework.stereotype.Service;

@Service
public class RateExchangeService {

    private static RateExchangeBackendClient rateExchangeBackendClient;
    private static RateExchangeService rateExchangeService;

    public RateExchangeService(RateExchangeBackendClient rateExchangeBackendClient) {

        RateExchangeService.rateExchangeBackendClient = rateExchangeBackendClient;
    }

    public static RateExchangeService getInstance() {

        if (rateExchangeService== null) {
            rateExchangeService = new RateExchangeService(rateExchangeBackendClient);
        }
        return rateExchangeService;
    }

    public RateExchangeDto getRateExchange() {

        return rateExchangeBackendClient.getRateExchange();
    }
}
