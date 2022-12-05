package com.cae.nct.data.service;

import com.cae.nct.data.entity.NEEDCAT_PROJECTCAT;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NEEDCAT_PROJECTCATService {

    private final NEEDCAT_PROJECTCATRepository repository;

    @Autowired
    public NEEDCAT_PROJECTCATService(NEEDCAT_PROJECTCATRepository repository) {
        this.repository = repository;
    }

    public Optional<NEEDCAT_PROJECTCAT> get(UUID id) {
        return repository.findById(id);
    }

    public NEEDCAT_PROJECTCAT update(NEEDCAT_PROJECTCAT entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<NEEDCAT_PROJECTCAT> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
