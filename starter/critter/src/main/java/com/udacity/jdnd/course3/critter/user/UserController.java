package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.PetEntity;
import com.udacity.jdnd.course3.critter.pet.PetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Handles web requests related to Users.
 * <p>
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PetService petService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        log.info("Received request to save customer with customer DTO being : {}", customerDTO);
        CustomerEntity customerEntity = getCustomerEntityFromCustomerDTO(customerDTO);
        log.info("Customer DTO converted to customer entity : {}", customerEntity);
        CustomerEntity savedCustomerEntity = customerService.saveCustomer(customerEntity);
        log.info("Saved customer entity : {}", savedCustomerEntity);
        CustomerDTO savedCustomerDTO = getCustomerDTOFromCustomerEntity(savedCustomerEntity);
        log.info("Saved customer dto created from customer entity : {}", savedCustomerDTO);
        return savedCustomerDTO;
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers() {
        log.info("Request received to retrieve all customers");
        List<CustomerEntity> customerEntityList = customerService.getAllCustomers();
        List<CustomerDTO> customerDTOList = new ArrayList<>();
        if (customerEntityList != null && customerEntityList.size() > 0) {
            for (CustomerEntity customerEntity : customerEntityList) {
                CustomerDTO customerDTO = getCustomerDTOFromCustomerEntity(customerEntity);
                customerDTOList.add(customerDTO);
            }
        }
        return customerDTOList;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId) {
        log.info("Input pet id : {}", petId);
        PetEntity petEntity = petService.getPetById(petId);
        log.info("Pet Entity info for the above pet id : {}", petEntity);
        CustomerEntity customerEntity = petEntity.getCustomerEntity();
        CustomerDTO customerDTO = getCustomerDTOFromCustomerEntity(customerEntity);
        log.info("customerDTO object after conversion : {}", customerDTO);
        return customerDTO;
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("Employee DTO to save : {}", employeeDTO);
        EmployeeEntity employeeEntity = getEmployeeEntityFromEmployeeDTO(employeeDTO);
        log.info("Employee Entity generated from employee DTO : {}", employeeEntity);
        EmployeeEntity savedEmployee = employeeService.saveEmployee(employeeEntity);
        log.info("Saved employee entity : {}", savedEmployee);
        EmployeeDTO savedEmployeeDTO = getEmployeeDTOFromEmployeeEntity(savedEmployee);
        log.info("savedEmployeeDTO : {}", savedEmployeeDTO);
        return savedEmployeeDTO;
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        log.info("Input employee id to search : {}", employeeId);
        EmployeeEntity employeeEntity = employeeService.getEmployeeById(employeeId);
        log.info("Employee entity returned by db search : {}", employeeEntity);
        EmployeeDTO employeeDTO = getEmployeeDTOFromEmployeeEntity(employeeEntity);
        log.info("Converted employee DTO : {}", employeeDTO);
        return employeeDTO;
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        log.info("New set of days available : {}", daysAvailable);
        log.info("employee id to update : {}", employeeId);
        EmployeeEntity employeeEntity = employeeService.getEmployeeById(employeeId);

        if (employeeEntity == null) {
            return;
        }
        employeeEntity.setDaysAvailable(daysAvailable);
        employeeService.saveEmployee(employeeEntity);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        throw new UnsupportedOperationException();
    }

    private CustomerDTO getCustomerDTOFromCustomerEntity(CustomerEntity customerEntity) {
        if (customerEntity == null) {
            return null;
        }

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customerEntity.getId());
        customerDTO.setName(customerEntity.getName());
        customerDTO.setPhoneNumber(customerEntity.getPhoneNumber());
        customerDTO.setNotes(customerEntity.getNotes());
        List<Long> petIds = new ArrayList<>();
        for (PetEntity pet : customerEntity.getPetEntityList()) {
            petIds.add(pet.getId());
        }
        customerDTO.setPetIds(petIds);

        return customerDTO;

    }

    private CustomerEntity getCustomerEntityFromCustomerDTO(CustomerDTO customerDTO) {

        if (customerDTO == null) {
            return null;
        }

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(customerDTO.getId());
        customerEntity.setName(customerDTO.getName());
        customerEntity.setPhoneNumber(customerDTO.getPhoneNumber());
        customerEntity.setNotes(customerDTO.getNotes());
        List<PetEntity> petEntityList = new ArrayList<>();
        for (Long petId : customerDTO.getPetIds()) {
            PetEntity petEntity = petService.getPetById(petId);
            petEntityList.add(petEntity);
        }
        customerEntity.setPetEntityList(petEntityList);
        return customerEntity;
    }

    private EmployeeEntity getEmployeeEntityFromEmployeeDTO(EmployeeDTO employeeDTO) {

        if (employeeDTO == null) {
            return null;
        }

        EmployeeEntity employeeEntity = new EmployeeEntity();

        employeeEntity.setId(employeeDTO.getId());
        employeeEntity.setName(employeeDTO.getName());
        employeeEntity.setSkills(employeeDTO.getSkills());
        employeeEntity.setDaysAvailable(employeeDTO.getDaysAvailable());

        return employeeEntity;
    }

    private EmployeeDTO getEmployeeDTOFromEmployeeEntity(EmployeeEntity employeeEntity) {
        if (employeeEntity == null) {
            return null;
        }

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employeeEntity.getId());
        employeeDTO.setName(employeeEntity.getName());
        employeeDTO.setSkills(employeeEntity.getSkills());
        employeeDTO.setDaysAvailable(employeeEntity.getDaysAvailable());
        return employeeDTO;
    }

}
