package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.CustomerEntity;
import com.udacity.jdnd.course3.critter.user.CustomerRepository;
import com.udacity.jdnd.course3.critter.user.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
@Slf4j
public class PetController {

    @Autowired
    private PetService petService;

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        log.info("Request received to save the petDTO : {}", petDTO.toString());
        PetEntity petEntity = getPetEntityFromPetDTO(petDTO);
        log.info("Input DTO converted to pet Entity : {}", petEntity.toString());
        PetEntity savedPetEntity = petService.savePet(petEntity);
        log.info("Pet saved : {}", savedPetEntity.toString());
        PetDTO savedPetDTO = getPetDTOFromPetEntity(savedPetEntity);
        log.info("Converting saved pet entity to DTO : {}", savedPetDTO.toString());
        return savedPetDTO;
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        log.info("Input request to search pet with id : {}", petId);
        PetEntity petEntity = petService.getPetById(petId);
        log.info("Result received from the DB : {}", petEntity.toString());
        return getPetDTOFromPetEntity(petEntity);
    }

    @GetMapping
    public List<PetDTO> getPets() {
        log.info("Request to retrieve all the pets from DB");
        List<PetEntity> petEntityList = petService.getAllPets();
        List<PetDTO> petDTOList = new ArrayList<>();
        if (petEntityList != null && petEntityList.size() > 0) {
            log.info("Input pets list size : {}", petEntityList.size());
            for (PetEntity petEntity : petEntityList) {
                PetDTO petDTO = getPetDTOFromPetEntity(petEntity);
                petDTOList.add(petDTO);
            }
        } else {
            log.info("No pet stored in DB. Nothing to retrieve");
        }
        return petDTOList;
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        log.info("Finding all pets with owne id : {}", ownerId);
        List<PetEntity> petEntityList = petService.getAllPets();
        List<PetDTO> petDTOList = new ArrayList<>();
        if (petEntityList != null && petEntityList.size() > 0) {
            log.info("Total pets owned by the current owner is : {}", petEntityList.size());
            for (PetEntity petEntity : petEntityList) {
                if (petEntity.getCustomerEntity() != null && petEntity.getCustomerEntity().getId() == ownerId) {
                    PetDTO petDTO = getPetDTOFromPetEntity(petEntity);
                    petDTOList.add(petDTO);
                }
            }
        } else {
            log.info("No pets owned by this customer");
        }
        return petDTOList;
    }

    private PetDTO getPetDTOFromPetEntity(PetEntity petEntity) {

        if (petEntity == null) {
            return null;
        }

        PetDTO petDTO = new PetDTO();
        petDTO.setId(petEntity.getId());
        petDTO.setType(petEntity.getType());
        petDTO.setName(petEntity.getName());
        CustomerEntity customerEntity = petEntity.getCustomerEntity();
        long ownerId = -1;
        if (customerEntity != null) {
            ownerId = customerEntity.getId();
        }
        petDTO.setOwnerId(ownerId);
        petDTO.setBirthDate(petEntity.getBirthDate());
        petDTO.setNotes(petEntity.getNotes());
        return petDTO;
    }

    private PetEntity getPetEntityFromPetDTO(PetDTO petDTO) {

        if (petDTO == null) {
            return null;
        }

        PetEntity petEntity = new PetEntity();
        petEntity.setType(petDTO.getType());
        petEntity.setName(petDTO.getName());
        long ownerId = petDTO.getOwnerId();
        CustomerEntity customer = customerService.getCustomer(ownerId);
        petEntity.setCustomerEntity(customer);
        petEntity.setBirthDate(petDTO.getBirthDate());
        petEntity.setNotes(petDTO.getNotes());
        return petEntity;
    }
}
