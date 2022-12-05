package com.cae.nct.data.service;

import com.cae.nct.data.entity.NEEDCAT_SEG;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NEEDCAT_SEGService {

    private final NEEDCAT_SEGRepository repository;

    @Autowired
    public NEEDCAT_SEGService(NEEDCAT_SEGRepository repository) {
        this.repository = repository;
    }

    public Optional<NEEDCAT_SEG> get(UUID id) {
        return repository.findById(id);
    }

    public NEEDCAT_SEG update(NEEDCAT_SEG entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<NEEDCAT_SEG> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
