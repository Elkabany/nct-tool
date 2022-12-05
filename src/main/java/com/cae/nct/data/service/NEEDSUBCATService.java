package com.cae.nct.data.service;

import com.cae.nct.data.entity.NEEDSUBCAT;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NEEDSUBCATService {

    private final NEEDSUBCATRepository repository;

    @Autowired
    public NEEDSUBCATService(NEEDSUBCATRepository repository) {
        this.repository = repository;
    }

    public Optional<NEEDSUBCAT> get(UUID id) {
        return repository.findById(id);
    }

    public NEEDSUBCAT update(NEEDSUBCAT entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<NEEDSUBCAT> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
