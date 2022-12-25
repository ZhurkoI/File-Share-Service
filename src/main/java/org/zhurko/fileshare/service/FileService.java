package org.zhurko.fileshare.service;

import org.zhurko.fileshare.entity.FileEntity;
import org.zhurko.fileshare.repository.FileRepository;

public class FileService {
    private final FileRepository fileRepo;

    public FileService(FileRepository fileRepo) {
        this.fileRepo = fileRepo;
    }

    public FileEntity save(FileEntity file) {
        return fileRepo.save(file);
    }
}
