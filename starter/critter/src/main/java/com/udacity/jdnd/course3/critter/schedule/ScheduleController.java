package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.PetEntity;
import com.udacity.jdnd.course3.critter.pet.PetService;
import com.udacity.jdnd.course3.critter.user.CustomerEntity;
import com.udacity.jdnd.course3.critter.user.CustomerService;
import com.udacity.jdnd.course3.critter.user.EmployeeEntity;
import com.udacity.jdnd.course3.critter.user.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
@Slf4j
public class ScheduleController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PetService petService;

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        log.info("Input schedule DTO to save {}", scheduleDTO);
        ScheduleEntity scheduleEntity = getScheduleEntityFromScheduleDTO(scheduleDTO);
        log.info("Converted schedule entity object : {}", scheduleEntity);
        ScheduleEntity savedSchedule = scheduleService.saveSchedule(scheduleEntity);
        log.info("Saved schedule : {}", savedSchedule);
        ScheduleDTO savedScheduleDTO = getScheduleDTOFromScheduleEntity(scheduleEntity);
        log.info("saved schedule DTO : {}", savedScheduleDTO);
        return savedScheduleDTO;
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {

        List<ScheduleEntity> scheduleEntityList = scheduleService.getAllSchedules();
        if (scheduleEntityList == null) {
            return null;
        }

        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        for (ScheduleEntity scheduleEntity : scheduleEntityList) {
            scheduleDTOList.add(getScheduleDTOFromScheduleEntity(scheduleEntity));
        }

        return scheduleDTOList;
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<ScheduleEntity> scheduleEntityList = scheduleService.getAllSchedules();
        if (scheduleEntityList == null) {
            return null;
        }

        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        for (ScheduleEntity scheduleEntity : scheduleEntityList) {
            if (scheduleEntity.getPetEntityList() != null) {
                List<PetEntity> petEntityList = scheduleEntity.getPetEntityList();
                for (PetEntity petEntity : petEntityList) {
                    if (petEntity.getId() == petId) {
                        scheduleDTOList.add(getScheduleDTOFromScheduleEntity(scheduleEntity));
                        break;
                    }
                }

            }
        }

        return scheduleDTOList;
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<ScheduleEntity> scheduleEntityList = scheduleService.getAllSchedules();
        if (scheduleEntityList == null) {
            return null;
        }

        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        for (ScheduleEntity scheduleEntity : scheduleEntityList) {
            if (scheduleEntity.getPetEntityList() != null) {
                List<EmployeeEntity> employeeEntityList = scheduleEntity.getEmployeeEntityList();
                for (EmployeeEntity employeeEntity : employeeEntityList) {
                    if (employeeEntity.getId() == employeeId) {
                        scheduleDTOList.add(getScheduleDTOFromScheduleEntity(scheduleEntity));
                        break;
                    }
                }

            }
        }

        return scheduleDTOList;
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<ScheduleEntity> scheduleEntityList = scheduleService.getAllSchedules();
        CustomerEntity customer = customerService.getCustomer(customerId);

        if (scheduleEntityList == null || customer == null) {
            return null;
        }

        List<PetEntity> petEntityList = customer.getPetEntityList();
        if (petEntityList == null) {
            return null;
        }

        List<Long> petIds = new ArrayList<>();
        for (PetEntity petEntity : petEntityList) {
            petIds.add(petEntity.getId());
        }


        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        for (ScheduleEntity scheduleEntity : scheduleEntityList) {
            if (scheduleEntity.getPetEntityList() != null) {
                List<PetEntity> petEntities = scheduleEntity.getPetEntityList();
                for (PetEntity petEntity : petEntities) {
                    if (petIds.contains(petEntity.getId())) {
                        scheduleDTOList.add(getScheduleDTOFromScheduleEntity(scheduleEntity));
                        break;
                    }
                }

            }
        }

        return scheduleDTOList;
    }

    private ScheduleEntity getScheduleEntityFromScheduleDTO(ScheduleDTO scheduleDTO) {
        if (scheduleDTO == null) {
            return null;
        }

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleDTO.getId());
        List<EmployeeEntity> employeeEntityList = new ArrayList<>();
        List<Long> employeeIds = scheduleDTO.getEmployeeIds();
        if (employeeIds != null) {
            for (Long employeeId : employeeIds) {
                EmployeeEntity employeeById = employeeService.getEmployeeById(employeeId);
                if (employeeById != null) {
                    employeeEntityList.add(employeeById);
                }
            }
        }
        scheduleEntity.setEmployeeEntityList(employeeEntityList);

        List<PetEntity> petEntityList = new ArrayList<>();
        List<Long> petIds = scheduleDTO.getPetIds();
        if (petIds != null) {
            for (Long petId : petIds) {
                PetEntity petEntity = petService.getPetById(petId);
                if (petEntity != null) {
                    petEntityList.add(petEntity);
                }
            }
        }
        scheduleEntity.setPetEntityList(petEntityList);
        scheduleEntity.setDate(scheduleDTO.getDate());
        scheduleEntity.setEmployeeSkills(scheduleDTO.getActivities());

        return scheduleEntity;

    }

    private ScheduleDTO getScheduleDTOFromScheduleEntity(ScheduleEntity scheduleEntity) {

        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setId(scheduleEntity.getId());
        List<Long> employeeIds = new ArrayList<>();
        List<EmployeeEntity> employeeEntityList = scheduleEntity.getEmployeeEntityList();
        if (employeeEntityList != null) {
            for (EmployeeEntity employeeEntity : employeeEntityList) {
                employeeIds.add(employeeEntity.getId());
            }
        }
        scheduleDTO.setEmployeeIds(employeeIds);

        List<Long> petIds = new ArrayList<>();

        List<PetEntity> petEntityList = scheduleEntity.getPetEntityList();
        if (petEntityList != null) {
            for (PetEntity petEntity : petEntityList) {
                petIds.add(petEntity.getId());
            }
        }

        scheduleDTO.setPetIds(petIds);
        scheduleDTO.setDate(scheduleEntity.getDate());
        scheduleDTO.setActivities(scheduleEntity.getEmployeeSkills());
        return scheduleDTO;

    }
}
