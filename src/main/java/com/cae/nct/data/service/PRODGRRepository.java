package com.cae.nct.data.service;

import com.cae.nct.data.entity.PRODGR;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PRODGRRepository extends JpaRepository<PRODGR, UUID> {

}