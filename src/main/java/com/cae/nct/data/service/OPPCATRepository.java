package com.cae.nct.data.service;

import com.cae.nct.data.entity.OPPCAT;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OPPCATRepository extends JpaRepository<OPPCAT, UUID> {

}