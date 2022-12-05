package com.cae.nct.data.service;

import com.cae.nct.data.entity.OPPCAT;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OPPCATService {

    private final OPPCATRepository repository;

    @Autowired
    public OPPCATService(OPPCATRepository repository) {
        this.repository = repository;
    }

    public Optional<OPPCAT> get(UUID id) {
        return repository.findById(id);
    }

    public OPPCAT update(OPPCAT entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<OPPCAT> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
