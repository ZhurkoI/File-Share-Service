package org.zhurko.fileshare.util;

import org.zhurko.fileshare.entity.EventEntity;
import org.zhurko.fileshare.entity.EventType;
import org.zhurko.fileshare.entity.FileEntity;
import org.zhurko.fileshare.entity.UserEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HibernateResultSetMapper {

    public UserEntity getUserWithChildElements(List<Object[]> resultSet) {
        UserEntity userEntity = new UserEntity();
        List<EventEntity> events = new ArrayList<>();
        if (!resultSet.isEmpty()) {
            Object[] userEntry = resultSet.get(0);
            userEntity.setId((Long) userEntry[0]);
            userEntity.setFirstName((String) userEntry[1]);
            userEntity.setLastName((String) userEntry[2]);
            userEntity.setEmail((String) userEntry[3]);

            for (Object[] entry : resultSet) {
                if (entry[4] != null) {
                    FileEntity fileEntity = new FileEntity();
                    fileEntity.setId((Long) entry[9]);
                    fileEntity.setFilePath((String) entry[10]);

                    EventEntity eventEntity = new EventEntity();
                    eventEntity.setId((Long) entry[4]);
                    eventEntity.setCreated((Date) entry[5]);
                    eventEntity.setEventType(EventType.valueOf((String) entry[6]));
                    eventEntity.setFile(fileEntity);
                    eventEntity.setUser(userEntity);
                    events.add(eventEntity);
                }
            }
            userEntity.setEvents(events);
            return userEntity;
        } else {
            return null;
        }
    }

    public List<UserEntity> getUsersWithChildElements(List<Object[]> resultSet) {
        Map<UserEntity, List<EventEntity>> usersWithEvents = new HashMap<>();

        if (!resultSet.isEmpty()) {
            resultSet.forEach(entry -> {
                UserEntity userEntity = new UserEntity();
                userEntity.setId((Long) entry[0]);
                userEntity.setFirstName((String) entry[1]);
                userEntity.setLastName((String) entry[2]);
                userEntity.setEmail((String) entry[3]);

                if (entry[4] == null) {
                    usersWithEvents.put(userEntity, new ArrayList<>());
                } else {
                    FileEntity fileEntity = new FileEntity();
                    fileEntity.setId((Long) entry[9]);
                    fileEntity.setFilePath((String) entry[10]);

                    EventEntity eventEntity = new EventEntity();
                    eventEntity.setId((Long) entry[4]);
                    eventEntity.setCreated((Date) entry[5]);
                    eventEntity.setEventType(EventType.valueOf((String) entry[6]));
                    eventEntity.setFile(fileEntity);
                    eventEntity.setUser(userEntity);

                    List<EventEntity> events = new ArrayList<>();
                    if (usersWithEvents.containsKey(userEntity)) {
                        events = usersWithEvents.get(userEntity);
                        events.add(eventEntity);
                        usersWithEvents.put(userEntity, events);
                    } else {
                        events.add(eventEntity);
                        usersWithEvents.put(userEntity, events);
                    }
                }
            });

            List<UserEntity> userEntities = new ArrayList<>();

            for (Map.Entry<UserEntity, List<EventEntity>> user : usersWithEvents.entrySet()) {
                user.getKey().setEvents(user.getValue());
                userEntities.add(user.getKey());
            }

            return userEntities;
        } else {
            return Collections.emptyList();
        }
    }

    public List<EventEntity> getEventsWithFiles(List<Object[]> resultSet) {
        if (!resultSet.isEmpty()) {
            List<EventEntity> events = new ArrayList<>();

            resultSet.forEach(entry -> {
                FileEntity fileEntity = new FileEntity();
                fileEntity.setId((Long) entry[5]);
                fileEntity.setFilePath((String) entry[6]);

                EventEntity eventEntity = new EventEntity();
                eventEntity.setId((Long) entry[0]);
                eventEntity.setCreated((Date) entry[1]);
                eventEntity.setEventType(EventType.valueOf((String) entry[2]));
                eventEntity.setFile(fileEntity);

                events.add(eventEntity);
            });
            return events;
        }
        return Collections.emptyList();
    }
}
