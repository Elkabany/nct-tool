package com.cae.nct.data.service;

import com.cae.nct.data.entity.PRODGR;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PRODGRService {

    private final PRODGRRepository repository;

    @Autowired
    public PRODGRService(PRODGRRepository repository) {
        this.repository = repository;
    }

    public Optional<PRODGR> get(UUID id) {
        return repository.findById(id);
    }

    public PRODGR update(PRODGR entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<PRODGR> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
