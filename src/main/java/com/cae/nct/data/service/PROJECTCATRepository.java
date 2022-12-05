package com.cae.nct.data.service;

import com.cae.nct.data.entity.PROJECTCAT;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PROJECTCATRepository extends JpaRepository<PROJECTCAT, UUID> {

}