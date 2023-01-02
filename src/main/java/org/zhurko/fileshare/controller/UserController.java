package org.zhurko.fileshare.controller;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.zhurko.fileshare.dto.UserDTO;
import org.zhurko.fileshare.entity.UserEntity;
import org.zhurko.fileshare.repository.hibernate.UserRepositoryImpl;
import org.zhurko.fileshare.service.UserService;
import org.zhurko.fileshare.util.HttpUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class UserController extends HttpServlet {

    private final UserService userService = new UserService(new UserRepositoryImpl());
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long userId;
        List<UserEntity> users;

        String getPathInfo = request.getPathInfo();
        if (getPathInfo == null || getPathInfo.equals("/")) {
            users = userService.getAll();
            if (!users.isEmpty()) {
                List<UserDTO> usersDTO = users.stream().map(UserDTO::fromEntity).collect(Collectors.toList());
                String responseJson = gson.toJson(usersDTO);
                HttpUtils.configureResponse(response, 200, responseJson);
            } else {
                HttpUtils.configureResponse(response, 404, HttpUtils.compileMessage("Users not found"));
            }
        } else {
            try {
                userId = Long.valueOf(request.getPathInfo().substring(1));
            } catch (NumberFormatException numberFormatException) {
                HttpUtils.configureResponse(response, 400, HttpUtils.compileMessage("Illegal argument"));
                return;
            }
            try {
                UserEntity user = userService.getById(userId);
                UserDTO userDTO = UserDTO.fromEntity(user);
                String responseJson = gson.toJson(userDTO);
                HttpUtils.configureResponse(response, 200, responseJson);
            } catch (IllegalArgumentException illegalArgumentException) {
                HttpUtils.configureResponse(response, 404,
                        HttpUtils.compileMessage(illegalArgumentException.getMessage()));
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
            HttpUtils.configureResponse(response, 400, HttpUtils.compileMessage("Bad request"));
        }
        if (userDTO != null) {
            UserEntity savedUserEntity = userService.save(UserDTO.toEntity(userDTO));
            if (savedUserEntity != null) {
                UserDTO savedUserDTO = UserDTO.fromEntity(savedUserEntity);
                String responseJson = gson.toJson(savedUserDTO);
                HttpUtils.configureResponse(response, 200, responseJson);
            } else {
                HttpUtils.configureResponse(response, 400, HttpUtils.compileMessage("Bad request"));
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
            HttpUtils.configureResponse(response, 400, HttpUtils.compileMessage("Bad request"));
        }

        if (userDTO != null) {
            UserEntity updatedUserEntity = userService.update(UserDTO.toEntity(userDTO));
            if (updatedUserEntity != null) {
                UserDTO updatedUserDTO = UserDTO.fromEntity(updatedUserEntity);
                String responseJson = gson.toJson(updatedUserDTO);
                HttpUtils.configureResponse(response, 200, responseJson);
            } else {
                HttpUtils.configureResponse(response, 404, HttpUtils.compileMessage("Users not found"));
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long userId;

        try {
            userId = Long.valueOf(request.getPathInfo().substring(1));
        } catch (NumberFormatException numberFormatException) {
            HttpUtils.configureResponse(response, 404, HttpUtils.compileMessage("Illegal argument"));
            return;
        }
        userService.deleteById(userId);
        HttpUtils.configureResponse(response, 200, HttpUtils.compileMessage("Deleted"));
    }
}
