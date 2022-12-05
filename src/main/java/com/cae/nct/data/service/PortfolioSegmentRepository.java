package com.cae.nct.data.service;

import com.cae.nct.data.entity.PortfolioSegment;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioSegmentRepository extends JpaRepository<PortfolioSegment, UUID> {

}