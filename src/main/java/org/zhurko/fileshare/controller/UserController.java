package org.zhurko.fileshare.controller;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.zhurko.fileshare.dto.UserDTO;
import org.zhurko.fileshare.entity.UserEntity;
import org.zhurko.fileshare.repository.hibernate.UserRepositoryImpl;
import org.zhurko.fileshare.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserController extends HttpServlet {

    private static final String JSON_CONTENT_TYPE = "application/json";
    private final UserService userService = new UserService(new UserRepositoryImpl());
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long userId;
        UserEntity user = null;
        List<UserEntity> users;

        String getPathInfo = request.getPathInfo();
        if (getPathInfo == null || getPathInfo.equals("/")) {
            users = userService.getAll();
            if (!users.isEmpty()) {
                List<UserDTO> usersDTO = new ArrayList<>();
                for (UserEntity userEntity : users) {
                    usersDTO.add(UserDTO.fromEntity(userEntity));
                }
                String responseJson = gson.toJson(usersDTO);
                response.setStatus(200);
                response.setContentType(JSON_CONTENT_TYPE);
                response.getWriter().println(responseJson);
            } else {
                sendResponseUserNotFound(response);
            }
        } else {
            try {
                userId = Long.valueOf(request.getPathInfo().substring(1));
            } catch (NumberFormatException nfe) {
                sendResponseUserNotFound(response);
                return;
            }
            user = userService.getById(userId);
            if (user != null) {
                UserDTO userDTO = UserDTO.fromEntity(user);
                String responseJson = gson.toJson(userDTO);
                response.setStatus(200);
                response.setContentType(JSON_CONTENT_TYPE);
                response.getWriter().println(responseJson);
            } else {
                sendResponseUserNotFound(response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestJson = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        UserDTO userDTO = null;
        try {
            userDTO = gson.fromJson(requestJson, UserDTO.class);
        } catch (JsonSyntaxException jsonSyntaxException) {
            response.setStatus(400);
        }
        if (userDTO != null) {
            UserEntity savedUserEntity = userService.save(UserDTO.toEntity(userDTO));
            if (savedUserEntity != null) {
                UserDTO savedUserDTO = UserDTO.fromEntity(savedUserEntity);
                String responseJson = gson.toJson(savedUserDTO);
                response.setStatus(200);
                response.setContentType(JSON_CONTENT_TYPE);
                response.getWriter().println(responseJson);
            } else {
                configureResponse(response, 400, JSON_CONTENT_TYPE, "Bad request");
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestJson = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        UserDTO userDTO = null;

        try {
            userDTO = gson.fromJson(requestJson, UserDTO.class);
        } catch (JsonSyntaxException jsonSyntaxException) {
            response.setStatus(400);
        }

        if (userDTO != null) {
            UserEntity updatedUserEntity = userService.update(UserDTO.toEntity(userDTO));
            if (updatedUserEntity != null) {
                UserDTO updatedUserDTO = UserDTO.fromEntity(updatedUserEntity);
                String responseJson = gson.toJson(updatedUserDTO);
                response.setStatus(200);
                response.setContentType(JSON_CONTENT_TYPE);
                response.getWriter().println(responseJson);
            } else {
                sendResponseUserNotFound(response);
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long userId;

        try {
            userId = Long.valueOf(request.getPathInfo().substring(1));
            userService.deleteById(userId);
        } catch (NumberFormatException nfe) {
            sendResponseUserNotFound(response);
        }
        response.setStatus(200);
        response.setContentType(JSON_CONTENT_TYPE);
        response.getWriter().println("{\"message\": \"Deleted\"}");
    }

    private void sendResponseUserNotFound(HttpServletResponse response) throws IOException {
        response.setStatus(404);
        response.setContentType(JSON_CONTENT_TYPE);
        response.getWriter().println("{\"message\": \"User not found\"}");
    }

    private void configureResponse(HttpServletResponse response, int code, String contentType,
                                   String message) throws IOException {
        response.setStatus(code);
        response.setContentType(contentType);
        response.getWriter().printf("{\"message\": \"%s\"}", message);
    }
}
