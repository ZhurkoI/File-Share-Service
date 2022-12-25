package org.zhurko.fileshare.dto;

import org.zhurko.fileshare.entity.FileEntity;

public class FileDTO {

    private final long id;
    private final String filePath;


    public FileDTO(long id, String filePath) {
        this.id = id;
        this.filePath = filePath;
    }

    public static FileDTO fromEntity(FileEntity fileEntity) {
        return new FileDTO(fileEntity.getId(), fileEntity.getFilePath());
    }
}
