package com.udacity.jdnd.course3.critter.pet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PetService {

    @Autowired
    private PetRepository petRepository;

    public PetEntity savePet(PetEntity petEntity) {
        log.info("Received request to save the following pet : {}", petEntity.toString());
        PetEntity savedPetEntity = petRepository.save(petEntity);
        log.info("Saved pet : {}", savedPetEntity.toString());
        return savedPetEntity;
    }

    public PetEntity getPetById(Long petId) {
        log.info("Find pet by id : {}", petId);
        Optional<PetEntity> byId = petRepository.findById(petId);
        if (byId.isPresent()) {
            log.info("Pet Found !");
            return byId.get();
        } else {
            log.info("Pet not found!");
            return null;
        }
    }

    public List<PetEntity> getAllPets() {
        log.info("Return all the pets stored in db");
        List<PetEntity> petEntities = (List<PetEntity>) petRepository.findAll();
        if (petEntities != null && petEntities.size() > 0) {
            log.info("Total pets stored count : {}", petEntities.size());
        } else {
            log.info("No pet stored in db");
        }
        return petEntities;
    }


}
