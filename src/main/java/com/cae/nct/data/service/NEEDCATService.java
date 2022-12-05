package com.cae.nct.data.service;

import com.cae.nct.data.entity.NEEDCAT;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NEEDCATService {

    private final NEEDCATRepository repository;

    @Autowired
    public NEEDCATService(NEEDCATRepository repository) {
        this.repository = repository;
    }

    public Optional<NEEDCAT> get(UUID id) {
        return repository.findById(id);
    }

    public NEEDCAT update(NEEDCAT entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<NEEDCAT> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
