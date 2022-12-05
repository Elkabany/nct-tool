package com.cae.nct.data.service;

import com.cae.nct.data.entity.NEEDCAT;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NEEDCATRepository extends JpaRepository<NEEDCAT, UUID> {

}