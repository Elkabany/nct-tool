package com.cae.nct.data.service;

import com.cae.nct.data.entity.NEEDSUBCAT_PROD;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NEEDSUBCAT_PRODService {

    private final NEEDSUBCAT_PRODRepository repository;

    @Autowired
    public NEEDSUBCAT_PRODService(NEEDSUBCAT_PRODRepository repository) {
        this.repository = repository;
    }

    public Optional<NEEDSUBCAT_PROD> get(UUID id) {
        return repository.findById(id);
    }

    public NEEDSUBCAT_PROD update(NEEDSUBCAT_PROD entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<NEEDSUBCAT_PROD> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
