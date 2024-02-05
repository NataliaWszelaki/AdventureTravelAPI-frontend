package com.crud.adventuretravel.service;

import com.crud.adventuretravel.backend.BackendClient;
import com.crud.adventuretravel.backend.BackendRequestException;
import com.crud.adventuretravel.domain.CustomerDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private static BackendClient backendClient;
    private static CustomerService customerService;

    public CustomerService(BackendClient backendClient) {

        CustomerService.backendClient = backendClient;
    }

    public static CustomerService getInstance() {

        if (customerService == null) {
            customerService = new CustomerService(backendClient);
        }
        return customerService;
    }

    public List<CustomerDto> getAllCustomers() {

        return backendClient.getCustomersList();
    }

    public void saveCustomerDto(CustomerDto customerDto) throws BackendRequestException {

        backendClient.post(customerDto);
    }

    public List<CustomerDto> findByEmail(String email) {

        List<CustomerDto> customerDtoList = getAllCustomers();
        return customerDtoList.stream()
                .filter(c -> c.getEmail().contains(email))
                .collect(Collectors.toList());
    }

    public void deleteCustomerDto(CustomerDto customerDto) {

        backendClient.delete(customerDto);
    }

    public void updateCustomerDto(CustomerDto customerDto) throws BackendRequestException {

        backendClient.put(customerDto);
    }

    public CustomerDto getCustomerById(long customerId) {

        return backendClient.getCustomerById(customerId);
    }
}
