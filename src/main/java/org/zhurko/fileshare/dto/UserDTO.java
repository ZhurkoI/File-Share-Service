package org.zhurko.fileshare.dto;

import org.zhurko.fileshare.entity.EventEntity;
import org.zhurko.fileshare.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

public class UserDTO {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<EventDTO> events;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<EventDTO> getEvents() {
        return events;
    }

    public void setEvents(List<EventDTO> events) {
        this.events = events;
    }

    public static UserDTO fromEntity(UserEntity userEntity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userEntity.getId());
        userDTO.setFirstName(userEntity.getFirstName());
        userDTO.setLastName(userEntity.getLastName());
        userDTO.setEmail(userEntity.getEmail());

        List<EventDTO> eventsDTO = new ArrayList<>();
        for (EventEntity eventEntity : userEntity.getEvents()) {
            eventsDTO.add(EventDTO.fromEntity(eventEntity));
        }

        userDTO.setEvents(eventsDTO);

        return userDTO;
    }

    public static UserEntity toEntity(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        if (userDTO.getId() != 0) {
            userEntity.setId(userDTO.getId());
        }
        userEntity.setFirstName(userDTO.getFirstName());
        userEntity.setLastName(userDTO.getLastName());
        userEntity.setEmail(userDTO.getEmail());

        return userEntity;
    }
}
