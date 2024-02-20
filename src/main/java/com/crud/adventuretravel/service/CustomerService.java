package com.crud.adventuretravel.service;

import com.crud.adventuretravel.backendClient.BackendRequestException;
import com.crud.adventuretravel.backendClient.CustomerBackendClient;
import com.crud.adventuretravel.domain.CustomerDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private static CustomerBackendClient customerBackendClient;
    private static CustomerService customerService;

    public CustomerService(CustomerBackendClient customerBackendClient) {

        CustomerService.customerBackendClient = customerBackendClient;
    }

    public static CustomerService getInstance() {

        if (customerService == null) {
            customerService = new CustomerService(customerBackendClient);
        }
        return customerService;
    }

    public List<CustomerDto> getAllCustomers() {

        return customerBackendClient.getCustomersList();
    }

    public CustomerDto getCustomerById(long customerId) {

        return customerBackendClient.getCustomerById(customerId);
    }

    public void saveCustomerDto(CustomerDto customerDto) throws BackendRequestException {

        customerBackendClient.post(customerDto);
    }

    public void updateCustomerDto(CustomerDto customerDto) throws BackendRequestException {

        customerBackendClient.put(customerDto);
    }

    public void updateCustomerDtoDeactivate(CustomerDto customerDto) throws BackendRequestException {

        customerBackendClient.putCustomerDeactivate(customerDto);
    }

    public void deleteCustomerDto(CustomerDto customerDto) throws BackendRequestException, ReferentialIntegrityViolationException {

        long numberOfReservationsWithCustomer = ReservationService.getInstance().getAllReservations().stream()
                .filter(r -> r.getCustomerId() == customerDto.getId())
                .count();

        if(numberOfReservationsWithCustomer == 0) {
            customerBackendClient.delete(customerDto);
        } else {
            throw new ReferentialIntegrityViolationException("The data you are trying to delete is associated with other records " +
                    "and cannot be deleted to maintain data integrity. The data may by ony deactivated");
        }
    }

    public List<CustomerDto> findByEmail(String email) {

        List<CustomerDto> customerDtoList = getAllCustomers();
        return customerDtoList.stream()
                .filter(c -> c.getEmail().contains(email.toLowerCase()))
                .collect(Collectors.toList());
    }
}
