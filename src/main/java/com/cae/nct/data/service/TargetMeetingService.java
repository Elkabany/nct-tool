package com.cae.nct.data.service;

import com.cae.nct.data.entity.TargetMeeting;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TargetMeetingService {

    private final TargetMeetingRepository repository;

    @Autowired
    public TargetMeetingService(TargetMeetingRepository repository) {
        this.repository = repository;
    }

    public Optional<TargetMeeting> get(UUID id) {
        return repository.findById(id);
    }

    public TargetMeeting update(TargetMeeting entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<TargetMeeting> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
