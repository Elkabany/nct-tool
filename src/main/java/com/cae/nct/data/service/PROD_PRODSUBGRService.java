package com.cae.nct.data.service;

import com.cae.nct.data.entity.PROD_PRODSUBGR;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PROD_PRODSUBGRService {

    private final PROD_PRODSUBGRRepository repository;

    @Autowired
    public PROD_PRODSUBGRService(PROD_PRODSUBGRRepository repository) {
        this.repository = repository;
    }

    public Optional<PROD_PRODSUBGR> get(UUID id) {
        return repository.findById(id);
    }

    public PROD_PRODSUBGR update(PROD_PRODSUBGR entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<PROD_PRODSUBGR> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
