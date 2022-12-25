package org.zhurko.fileshare.controller;

import org.zhurko.fileshare.entity.EventEntity;
import org.zhurko.fileshare.entity.EventType;
import org.zhurko.fileshare.entity.FileEntity;
import org.zhurko.fileshare.entity.UserEntity;
import org.zhurko.fileshare.repository.hibernate.EventRepositoryImpl;
import org.zhurko.fileshare.repository.hibernate.FileRepositoryImpl;
import org.zhurko.fileshare.repository.hibernate.UserRepositoryImpl;
import org.zhurko.fileshare.service.EventService;
import org.zhurko.fileshare.service.FileService;
import org.zhurko.fileshare.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileController extends HttpServlet {
    private static final String UPLOAD_DIRECTORY = "uploads";
    private static final String JSON_CONTENT_TYPE = "application/json";
    private final FileService fileService = new FileService(new FileRepositoryImpl());
    private final EventService eventService = new EventService(new EventRepositoryImpl());
    private final UserService userService = new UserService(new UserRepositoryImpl());


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getContentType().contains("multipart/form-data")) {
            String uploadPath = createUploadDirectory();
            EventEntity uploadEvent = new EventEntity();
            Long userId = null;
            String userDirPath;
            try {
                userId = Long.valueOf(request.getHeader("User-Id"));
            } catch (NumberFormatException numberFormatException) {
                configureResponse(response, 404, JSON_CONTENT_TYPE, "There is no user with specified id");
                return;
            }
            UserEntity user = userService.getById(userId);
            if (user != null) {
                userDirPath = createUserUploadDirectory(uploadPath, userId);
                uploadEvent.setEventType(EventType.UPLOADED);
                uploadEvent.setCreated(new Date());
                user.addEvent(uploadEvent);
            } else {
                configureResponse(response, 404, JSON_CONTENT_TYPE, "There is no user with specified id");
                return;
            }

            Part part = request.getPart("User-File");
            if (part != null) {
                String fileName = part.getSubmittedFileName();
                if ((fileName != null) && !(fileName.equals(""))) {
                    String fullFilePath = userDirPath + File.separator + fileName;
                    if (new File(fullFilePath).exists()) {
                        configureResponse(response, 400, JSON_CONTENT_TYPE, "File already exists");
                        return;
                    }
                    part.write(fullFilePath);
                    FileEntity savedFile = fileService.save(new FileEntity(fullFilePath));
                    savedFile.addEvent(uploadEvent);
                    eventService.save(uploadEvent);
                    configureResponse(response, 200, JSON_CONTENT_TYPE, "File uploaded successfully");
                } else {
                    configureResponse(response, 400, JSON_CONTENT_TYPE, "No file in request");
                    return;
                }
            } else {
                configureResponse(response, 400, JSON_CONTENT_TYPE, "No file in request");
                return;
            }
        }
    }

    private String createUserUploadDirectory(String uploadPath, Long userId) {
        String userDirPath = uploadPath + File.separator + userId.toString();
        File userDir = new File(userDirPath);
        if (!userDir.exists()) {
            userDir.mkdir();
        }
        return userDirPath;
    }

    private String createUploadDirectory() {
        String uploadPath = getServletContext().getRealPath("") + UPLOAD_DIRECTORY;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        return uploadPath;
    }

    private void configureResponse(HttpServletResponse response, int code, String contentType,
                                   String message) throws IOException {
        response.setStatus(code);
        response.setContentType(contentType);
        response.getWriter().printf("{\"message\": \"%s\"}", message);
    }
}
