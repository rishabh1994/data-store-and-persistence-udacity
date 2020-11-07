package com.udacity.jdnd.course3.critter.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
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

    public CustomerEntity saveCustomer(CustomerEntity customerEntity) {
        log.info("Request to save customer : {}", customerEntity);
        CustomerEntity savedCustomer = customerRepository.save(customerEntity);
        log.info("Saved customer : {}", savedCustomer.toString());
        return savedCustomer;
    }

    public List<CustomerEntity> getAllCustomers() {
        log.info("Get all customers from db");
        List<CustomerEntity> customerEntityList = (List<CustomerEntity>) customerRepository.findAll();
        if (customerEntityList != null && customerEntityList.size() > 0) {
            log.info("total customers stored in db : {}", customerEntityList.size());
        } else {
            log.info("No customer stored in db");
        }
        return customerEntityList;
    }

}
