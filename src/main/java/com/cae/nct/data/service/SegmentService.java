package com.cae.nct.data.service;

import com.cae.nct.data.entity.Segment;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SegmentService {

    private final SegmentRepository repository;

    @Autowired
    public SegmentService(SegmentRepository repository) {
        this.repository = repository;
    }

    public Optional<Segment> get(UUID id) {
        return repository.findById(id);
    }

    public Segment update(Segment entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<Segment> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
