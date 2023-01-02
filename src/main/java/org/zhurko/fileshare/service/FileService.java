package org.zhurko.fileshare.service;

import org.zhurko.fileshare.entity.EventEntity;
import org.zhurko.fileshare.entity.EventType;
import org.zhurko.fileshare.entity.FileEntity;
import org.zhurko.fileshare.entity.UserEntity;
import org.zhurko.fileshare.repository.FileRepository;
import org.zhurko.fileshare.repository.hibernate.EventRepositoryImpl;
import org.zhurko.fileshare.repository.hibernate.UserRepositoryImpl;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Date;

public class FileService {

    private static final String UPLOAD_DIRECTORY = "FileShareServiceUploads";

    private final FileRepository fileRepo;
    private final UserService userService = new UserService(new UserRepositoryImpl());
    private final EventService eventService = new EventService(new EventRepositoryImpl());

    public FileService(FileRepository fileRepo) {
        this.fileRepo = fileRepo;
    }

    private FileEntity save(FileEntity file) {
        return fileRepo.save(file);
    }

    public FileEntity upload(Long userId, Part part) throws IOException {
        UserEntity user;
        try {
            user = userService.getById(userId);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new IllegalArgumentException(illegalArgumentException);
        }

        String generalUploadsDirPath = createGeneralUploadDirectory();
        String userDirPath = createUserUploadDirectory(generalUploadsDirPath, userId);

        EventEntity uploadEvent = new EventEntity();
        uploadEvent.setEventType(EventType.UPLOADED);
        uploadEvent.setCreated(new Date());
        user.addEvent(uploadEvent);

        String fileName = part.getSubmittedFileName();
        if ((fileName != null) && !(fileName.equals(""))) {
            String fullFilePath = userDirPath + File.separator + fileName;
            if (new File(fullFilePath).exists()) {
                throw new FileAlreadyExistsException("File already exists");
            }
            part.write(fullFilePath);
            FileEntity savedFile = this.save(new FileEntity(fullFilePath));
            savedFile.addEvent(uploadEvent);
            eventService.save(uploadEvent);
            return savedFile;
        } else {
            throw new IllegalArgumentException("No file found in the request");
        }
    }

    private String createGeneralUploadDirectory() throws AccessDeniedException {
        String pathToUploadDir = System.getProperty("java.io.tmpdir") + File.separator + UPLOAD_DIRECTORY;
        File uploadDir = new File(pathToUploadDir);
        if (!uploadDir.exists()) {
            if (uploadDir.mkdir()) {
                return pathToUploadDir;
            } else {
                throw new AccessDeniedException("Cannot create directory '" + pathToUploadDir + "'");
            }
        }
        return pathToUploadDir;
    }

    private String createUserUploadDirectory(String uploadPath, Long userId) throws AccessDeniedException {
        String userDirPath = uploadPath + File.separator + userId.toString();
        File userDir = new File(userDirPath);
        if (!userDir.exists()) {
            if (userDir.mkdir()) {
                return userDirPath;
            } else {
                throw new AccessDeniedException("Cannot create directory '" + userDirPath + "'");
            }
        }
        return userDirPath;
    }
}
