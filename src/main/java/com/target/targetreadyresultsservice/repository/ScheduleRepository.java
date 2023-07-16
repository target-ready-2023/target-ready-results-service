package com.target.targetreadyresultsservice.repository;

import com.target.targetreadyresultsservice.model.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends MongoRepository<Schedule, String> {
}
