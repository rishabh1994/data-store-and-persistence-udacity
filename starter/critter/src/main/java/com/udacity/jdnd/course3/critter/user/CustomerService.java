package com.udacity.jdnd.course3.critter.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public CustomerEntity getCustomer(Long customerId) {
        log.info("Searching for a customer with id : {}", customerId);
        Optional<CustomerEntity> byId = customerRepository.findById(customerId);

        if (byId.isPresent()) {
            log.info("Customer found!");
            return byId.get();
        } else {
            log.info("Customer not found!");
            return null;
        }
    }
}
