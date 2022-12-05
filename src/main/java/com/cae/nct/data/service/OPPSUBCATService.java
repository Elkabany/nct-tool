package com.cae.nct.data.service;

import com.cae.nct.data.entity.OPPSUBCAT;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OPPSUBCATService {

    private final OPPSUBCATRepository repository;

    @Autowired
    public OPPSUBCATService(OPPSUBCATRepository repository) {
        this.repository = repository;
    }

    public Optional<OPPSUBCAT> get(UUID id) {
        return repository.findById(id);
    }

    public OPPSUBCAT update(OPPSUBCAT entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<OPPSUBCAT> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
