package org.zhurko.fileshare.controller;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.zhurko.fileshare.model.User;
import org.zhurko.fileshare.repository.hibernate.UserRepositoryImpl;
import org.zhurko.fileshare.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class UserController extends HttpServlet {

    private static final String JSON_CONTENT_TYPE = "application/json";
    private final Gson gson = new Gson();
    private final UserService userService = new UserService(new UserRepositoryImpl());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long userId;
        User user = null;
        List<User> users;

        String getPathInfo = request.getPathInfo();
        if (getPathInfo == null || getPathInfo.equals("/")) {
            users = userService.getAll();
            if (!users.isEmpty()) {
                String responseJson = gson.toJson(users);
                response.setStatus(200);
                response.setContentType(JSON_CONTENT_TYPE);
                response.getWriter().println(responseJson);
            } else {
                sendResponseUserNotFound(response);
            }
        } else {
            try {
                userId = Long.valueOf(request.getPathInfo().substring(1));
                user = userService.getById(userId);
            } catch (NumberFormatException nfe) {
                sendResponseUserNotFound(response);
                return;
            }
            if (user != null) {
                String responseJson = gson.toJson(user);
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
        User user = null;
        User savedUser = null;

        String requestJson = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        try {
            user = gson.fromJson(requestJson, User.class);
            savedUser = userService.save(user);
        } catch (JsonSyntaxException jsonSyntaxException) {
            response.setStatus(400);
        }

        if (savedUser != null) {
            String responseJson = gson.toJson(savedUser);
            response.setStatus(200);
            response.setContentType(JSON_CONTENT_TYPE);
            response.getWriter().println(responseJson);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestJson = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        User user = null;
        User updatedUser = null;

        try {
            user = gson.fromJson(requestJson, User.class);
            updatedUser = userService.update(user);
        } catch (JsonSyntaxException jsonSyntaxException) {
            response.setStatus(400);
        }

        if (updatedUser != null) {
            String responseJson = gson.toJson(updatedUser);
            response.setStatus(200);
            response.setContentType(JSON_CONTENT_TYPE);
            response.getWriter().println(responseJson);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long userId;

        try {
            userId = Long.valueOf(request.getPathInfo().substring(1));
            userService.deleteById(userId);
            response.setStatus(200);
            response.setContentType(JSON_CONTENT_TYPE);
            response.getWriter().println("{\"status\": \"deleted\"}");
        } catch (NumberFormatException nfe) {
            sendResponseUserNotFound(response);
        }
    }

    private void sendResponseUserNotFound(HttpServletResponse response) throws IOException {
        response.setStatus(404);
        String errorMessage = "{\"type\": \"error\", \"message\": \"User not found\"}";
        response.setContentType(JSON_CONTENT_TYPE);
        response.getWriter().println(errorMessage);
    }
}
