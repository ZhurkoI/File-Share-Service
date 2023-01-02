package org.zhurko.fileshare.controller;

import org.zhurko.fileshare.entity.FileEntity;
import org.zhurko.fileshare.repository.hibernate.FileRepositoryImpl;
import org.zhurko.fileshare.service.FileService;
import org.zhurko.fileshare.util.HttpUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;

@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileController extends HttpServlet {

    private final FileService fileService = new FileService(new FileRepositoryImpl());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getContentType().contains("multipart/form-data")) {
            Long userId;
            try {
                userId = Long.valueOf(request.getHeader("User-Id"));
            } catch (NumberFormatException numberFormatException) {
                HttpUtils.configureResponse(response, 404, HttpUtils.compileMessage("Illegal argument"));
                return;
            }

            Part part = request.getPart("User-File");
            if (part != null) {
                FileEntity fileEntity = null;
                try {
                    fileEntity = fileService.upload(userId, part);
                } catch (IllegalArgumentException | FileAlreadyExistsException | AccessDeniedException exception) {
                    HttpUtils.configureResponse(response, 400,
                            HttpUtils.compileMessage(exception.getMessage()));
                }
                if (fileEntity != null) {
                    HttpUtils.configureResponse(response, 200,
                            HttpUtils.compileMessage("File uploaded successfully"));
                }

            }
        } else {
            HttpUtils.configureResponse(response, 400, HttpUtils.compileMessage("Wrong content type"));
        }
    }
}
