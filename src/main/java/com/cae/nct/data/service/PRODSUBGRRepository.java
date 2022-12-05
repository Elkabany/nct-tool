package com.cae.nct.data.service;

import com.cae.nct.data.entity.PRODSUBGR;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PRODSUBGRRepository extends JpaRepository<PRODSUBGR, UUID> {

}