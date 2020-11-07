package com.udacity.jdnd.course3.critter.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public EmployeeEntity saveEmployee(EmployeeEntity employeeEntity) {
        log.info("Input employee to save : {}", employeeEntity);
        EmployeeEntity savedEmployee = employeeRepository.save(employeeEntity);
        log.info("saved employee info : {}", savedEmployee);
        return savedEmployee;
    }

    public EmployeeEntity getEmployeeById(Long employeeId) {
        log.info("Searching DB for employee id : {}", employeeId);
        Optional<EmployeeEntity> byId = employeeRepository.findById(employeeId);
        if (byId.isPresent()) {
            EmployeeEntity employeeEntity = byId.get();
            log.info("Employee found in db is : {}", employeeEntity);
            return employeeEntity;
        } else {
            log.info("No employee found with the above id.");
            return null;
        }
    }

    public List<EmployeeEntity> getEmployees() {
        List<EmployeeEntity> employeeEntityList = (List<EmployeeEntity>) employeeRepository.findAll();
        log.info("Fetched all employees from the DB");
        return employeeEntityList;
    }
}
