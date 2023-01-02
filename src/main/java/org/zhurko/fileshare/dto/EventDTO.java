package org.zhurko.fileshare.dto;

import org.zhurko.fileshare.entity.EventEntity;
import org.zhurko.fileshare.entity.EventType;

import java.util.Date;

public class EventDTO {
    private long id;
    private Date created;
    private EventType eventType;
    private FileDTO file;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public FileDTO getFile() {
        return file;
    }

    public void setFile(FileDTO file) {
        this.file = file;
    }

    public static EventDTO fromEntity(EventEntity eventEntity) {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(eventEntity.getId());
        eventDTO.setCreated(eventEntity.getCreated());
        eventDTO.setEventType(eventEntity.getEventType());
        eventDTO.setFile(FileDTO.fromEntity(eventEntity.getFile()));

        return eventDTO;
    }

    public static EventEntity toEntity(EventDTO eventDTO) {
        EventEntity eventEntity = new EventEntity();

        if (eventDTO.getId() != 0) {
            eventEntity.setId(eventDTO.getId());
        }
        eventEntity.setCreated(eventDTO.getCreated());
        eventEntity.setEventType(eventDTO.getEventType());
        eventEntity.setFile(FileDTO.toEntity(eventDTO.getFile()));

        return eventEntity;
    }
}
