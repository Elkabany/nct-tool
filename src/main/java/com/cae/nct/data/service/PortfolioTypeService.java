package com.cae.nct.data.service;

import com.cae.nct.data.entity.PortfolioType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PortfolioTypeService {

    private final PortfolioTypeRepository repository;

    @Autowired
    public PortfolioTypeService(PortfolioTypeRepository repository) {
        this.repository = repository;
    }

    public Optional<PortfolioType> get(UUID id) {
        return repository.findById(id);
    }

    public PortfolioType update(PortfolioType entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<PortfolioType> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
