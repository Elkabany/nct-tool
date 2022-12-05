package com.cae.nct.data.service;

import com.cae.nct.data.entity.PortfolioSegment;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PortfolioSegmentService {

    private final PortfolioSegmentRepository repository;

    @Autowired
    public PortfolioSegmentService(PortfolioSegmentRepository repository) {
        this.repository = repository;
    }

    public Optional<PortfolioSegment> get(UUID id) {
        return repository.findById(id);
    }

    public PortfolioSegment update(PortfolioSegment entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<PortfolioSegment> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
