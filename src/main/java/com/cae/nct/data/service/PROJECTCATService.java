package com.cae.nct.data.service;

import com.cae.nct.data.entity.PROJECTCAT;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PROJECTCATService {

    private final PROJECTCATRepository repository;

    @Autowired
    public PROJECTCATService(PROJECTCATRepository repository) {
        this.repository = repository;
    }

    public Optional<PROJECTCAT> get(UUID id) {
        return repository.findById(id);
    }

    public PROJECTCAT update(PROJECTCAT entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<PROJECTCAT> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
