package org.zhurko.fileshare.repository.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zhurko.fileshare.entity.EventEntity;
import org.zhurko.fileshare.repository.EventRepository;
import org.zhurko.fileshare.util.HibernateResultSetMapper;
import org.zhurko.fileshare.util.HibernateUtil;

import java.util.List;

public class EventRepositoryImpl implements EventRepository {

    private final HibernateResultSetMapper resultSetMapper = new HibernateResultSetMapper();

    @Override
    public EventEntity save(EventEntity event) {
        EventEntity savedEvent;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            savedEvent = session.merge(event);
            transaction.commit();
        }

        return savedEvent;
    }

    @Override
    public EventEntity getById(Long id) {
        EventEntity event;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            event = session.get(EventEntity.class, id);
        }
        return event;
    }

    @Override
    public List<EventEntity> getAll() {
        return getAllEventsInternal();
    }

    private List<EventEntity> getAllEventsInternal() {
        List<EventEntity> events;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            events = session.createQuery("FROM EventEntity", EventEntity.class).getResultList();
        }
        return events;
    }

    @Override
    public EventEntity update(EventEntity editedEvent) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            EventEntity event = session.get(EventEntity.class, editedEvent.getId());
            if (event != null) {
                event.setCreated(editedEvent.getCreated());
                event.setEventType(editedEvent.getEventType());
                session.merge(event);
                transaction.commit();
                return this.getById(event.getId());
            } else {
                return null;
            }
        }
    }

    @Override
    public void deleteById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            EventEntity event = session.get(EventEntity.class, id);
            if (event != null) {
                session.remove(event);
            }
        }
    }

    @Override
    public List<EventEntity> getByUserId(Long id) {
        List<Object[]> resultSet;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String sql = "SELECT E.id, E.created, E.type, E.file_id, E.user_id, F.id, F.filepath " +
                    "FROM event AS E JOIN file AS F ON E.file_id = F.id " +
                    "WHERE E.user_id = :userId";
            resultSet = session.createNativeQuery(sql).setParameter("userId", id).list();
        }

        return resultSetMapper.getEventsWithFiles(resultSet);
    }
}