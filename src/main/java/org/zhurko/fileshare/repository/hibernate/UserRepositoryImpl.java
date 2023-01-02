package org.zhurko.fileshare.repository.hibernate;

import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zhurko.fileshare.entity.UserEntity;
import org.zhurko.fileshare.repository.UserRepository;
import org.zhurko.fileshare.util.HibernateUtil;

import java.util.List;
import java.util.stream.Collectors;

public class UserRepositoryImpl implements UserRepository {

    @Override
    public UserEntity save(UserEntity user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        } catch (PersistenceException ex) {
            return null;
        }

        return this.getById(user.getId());
    }

    @Override
    public UserEntity getById(Long id) {
        UserEntity user;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String sql = "SELECT {U.*}, {E.*}, {F.*} " +
                    "FROM user AS U " +
                    "LEFT JOIN event AS E ON U.id = E.user_id " +
                    "LEFT JOIN file AS F ON E.file_id = F.id " +
                    "WHERE U.id = :id";
            user = session.createNativeQuery(sql, UserEntity.class, "U")
                    .addJoin("E", "U.events")
                    .addJoin("F", "E.file")
                    .setParameter("id", id)
                    .getSingleResultOrNull();
        }

        if (user != null) {
            return user;
        } else {
            throw new IllegalArgumentException("User with id=" + id + " was not found");
        }
    }

    @Override
    public List<UserEntity> getAll() {
        return getAllUsersInternal();
    }

    @Override
    public UserEntity update(UserEntity editedUser) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            UserEntity user = session.get(UserEntity.class, editedUser.getId());
            if (user != null) {
                user.setFirstName(editedUser.getFirstName());
                user.setLastName(editedUser.getLastName());
                session.merge(user);
                transaction.commit();
                return this.getById(user.getId());
            } else {
                return null;
            }
        }
    }

    @Override
    public void deleteById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            UserEntity user = session.get(UserEntity.class, id);
            if (user != null) {
                session.remove(user);
            }
            transaction.commit();
        }
    }

    private List<UserEntity> getAllUsersInternal() {
        List<UserEntity> users;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String sql = "SELECT {U.*}, {E.*}, {F.*} " +
                    "FROM user AS U " +
                    "LEFT JOIN event AS E ON U.id = E.user_id " +
                    "LEFT JOIN file AS F ON E.file_id = F.id";
            users = session.createNativeQuery(sql, UserEntity.class, "U")
                    .addJoin("E", "U.events")
                    .addJoin("F", "E.file")
                    .getResultStream()
                    .distinct()
                    .collect(Collectors.toList());
        }

        return users;
    }
}
