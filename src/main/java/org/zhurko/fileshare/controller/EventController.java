package org.zhurko.fileshare.controller;

import com.google.gson.Gson;
import org.zhurko.fileshare.dto.EventDTO;
import org.zhurko.fileshare.entity.EventEntity;
import org.zhurko.fileshare.repository.hibernate.EventRepositoryImpl;
import org.zhurko.fileshare.service.EventService;
import org.zhurko.fileshare.util.HttpUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class EventController extends HttpServlet {

    private final EventService eventService = new EventService(new EventRepositoryImpl());
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long userId = null;

        try {
            userId = Long.valueOf(request.getHeader("User-Id"));
        } catch (NumberFormatException numberFormatException) {
            HttpUtils.configureResponse(response, 400, HttpUtils.compileMessage("Bad request"));
            return;
        }

        List<EventEntity> events = eventService.getByUserId(userId);
        List<EventDTO> eventsDTO = events.stream().map(EventDTO::fromEntity).collect(Collectors.toList());
        String responseJson = gson.toJson(eventsDTO);
        HttpUtils.configureResponse(response, 400, responseJson);
    }
}
