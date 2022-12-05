package com.cae.nct.data.service;

import com.cae.nct.data.entity.PROD;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PRODRepository extends JpaRepository<PROD, UUID> {

}