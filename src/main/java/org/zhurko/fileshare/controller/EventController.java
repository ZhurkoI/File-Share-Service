package org.zhurko.fileshare.controller;

import com.google.gson.Gson;
import org.zhurko.fileshare.dto.EventDTO;
import org.zhurko.fileshare.entity.EventEntity;
import org.zhurko.fileshare.repository.hibernate.EventRepositoryImpl;
import org.zhurko.fileshare.service.EventService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EventController extends HttpServlet {

    private static final String JSON_CONTENT_TYPE = "application/json";
    private final EventService eventService = new EventService(new EventRepositoryImpl());
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long userId = null;

        try {
            userId = Long.valueOf(request.getHeader("User-Id"));
        } catch (NumberFormatException numberFormatException) {
            configureResponse(response, 400, JSON_CONTENT_TYPE, "Bad request");
            return;
        }

        List<EventEntity> events = eventService.getByUserId(userId);
        List<EventDTO> eventsDTO = new ArrayList<>();
        for (EventEntity event : events) {
            eventsDTO.add(EventDTO.fromEntity(event));
        }
        String responseJson = gson.toJson(eventsDTO);
        response.setStatus(200);
        response.setContentType(JSON_CONTENT_TYPE);
        response.getWriter().println(responseJson);
    }

    private void configureResponse(HttpServletResponse response, int code, String contentType,
                                   String message) throws IOException {
        response.setStatus(code);
        response.setContentType(contentType);
        response.getWriter().printf("{\"message\": \"%s\"}", message);
    }
}
