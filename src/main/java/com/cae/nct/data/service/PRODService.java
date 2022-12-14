package com.cae.nct.data.service;

import com.cae.nct.data.entity.PROD;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PRODService {

    private final PRODRepository repository;

    @Autowired
    public PRODService(PRODRepository repository) {
        this.repository = repository;
    }

    public Optional<PROD> get(UUID id) {
        return repository.findById(id);
    }

    public PROD update(PROD entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<PROD> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
