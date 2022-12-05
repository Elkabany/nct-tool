package com.cae.nct.data.service;

import com.cae.nct.data.entity.PortfolioType;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioTypeRepository extends JpaRepository<PortfolioType, UUID> {

}