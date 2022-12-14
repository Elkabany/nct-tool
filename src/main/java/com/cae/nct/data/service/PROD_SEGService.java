package com.cae.nct.data.service;

import com.cae.nct.data.entity.PROD_SEG;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PROD_SEGService {

    private final PROD_SEGRepository repository;

    @Autowired
    public PROD_SEGService(PROD_SEGRepository repository) {
        this.repository = repository;
    }

    public Optional<PROD_SEG> get(UUID id) {
        return repository.findById(id);
    }

    public PROD_SEG update(PROD_SEG entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<PROD_SEG> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
