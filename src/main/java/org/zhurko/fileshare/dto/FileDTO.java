package org.zhurko.fileshare.dto;

import org.zhurko.fileshare.entity.EventEntity;
import org.zhurko.fileshare.entity.FileEntity;

import java.util.List;

public class FileDTO {

    private long id;
    private String filePath;
    private List<EventEntity> events;

    public FileDTO(long id, String filePath) {
        this.id = id;
        this.filePath = filePath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<EventEntity> getEvents() {
        return events;
    }

    public void setEvents(List<EventEntity> events) {
        this.events = events;
    }

    public static FileDTO fromEntity(FileEntity fileEntity) {
        return new FileDTO(fileEntity.getId(), fileEntity.getFilePath());
    }

    public static FileEntity toEntity(FileDTO fileDTO) {
        FileEntity fileEntity = new FileEntity();

        if (fileDTO.getId() != 0) {
            fileEntity.setId(fileDTO.getId());
        }
        fileEntity.setFilePath(fileDTO.getFilePath());
        fileEntity.setEvents(fileDTO.getEvents());

        return fileEntity;
    }
}
