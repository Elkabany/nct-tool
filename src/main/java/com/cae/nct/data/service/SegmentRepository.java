package com.cae.nct.data.service;

import com.cae.nct.data.entity.Segment;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SegmentRepository extends JpaRepository<Segment, UUID> {

}