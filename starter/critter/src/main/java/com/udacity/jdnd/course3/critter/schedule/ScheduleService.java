package com.udacity.jdnd.course3.critter.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    public ScheduleEntity saveSchedule(ScheduleEntity scheduleEntity) {
        log.info("Input schedule to save : {}", scheduleEntity);
        ScheduleEntity savedSchedule = scheduleRepository.save(scheduleEntity);
        log.info("saved schedule : {}", savedSchedule);
        return savedSchedule;
    }

    public List<ScheduleEntity> getAllSchedules() {
        List<ScheduleEntity> scheduleEntityList = (List<ScheduleEntity>) scheduleRepository.findAll();
        log.info("Fetched all the schedules from the DB");
        return scheduleEntityList;
    }


}
