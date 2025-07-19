package com.capstone.backend.member.domain.repository;

import com.capstone.backend.member.domain.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

}
