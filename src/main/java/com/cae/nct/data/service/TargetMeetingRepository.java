package com.cae.nct.data.service;

import com.cae.nct.data.entity.TargetMeeting;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TargetMeetingRepository extends JpaRepository<TargetMeeting, UUID> {

}