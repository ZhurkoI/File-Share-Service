package org.zhurko.fileshare.service;

import org.zhurko.fileshare.entity.EventEntity;
import org.zhurko.fileshare.repository.EventRepository;

import java.util.List;

public class EventService {

    private final EventRepository eventRepo;

    public EventService(EventRepository eventRepo) {
        this.eventRepo = eventRepo;
    }


    public EventEntity save(EventEntity event) {
        return eventRepo.save(event);
    }

    public List<EventEntity> getByUserId(Long userId) {
        return eventRepo.getByUserId(userId);
    }
}
