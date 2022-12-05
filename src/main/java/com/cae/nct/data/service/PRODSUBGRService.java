package com.cae.nct.data.service;

import com.cae.nct.data.entity.PRODSUBGR;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PRODSUBGRService {

    private final PRODSUBGRRepository repository;

    @Autowired
    public PRODSUBGRService(PRODSUBGRRepository repository) {
        this.repository = repository;
    }

    public Optional<PRODSUBGR> get(UUID id) {
        return repository.findById(id);
    }

    public PRODSUBGR update(PRODSUBGR entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<PRODSUBGR> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
