package org.zhurko.fileshare.repository.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zhurko.fileshare.model.User;
import org.zhurko.fileshare.repository.UserRepository;
import org.zhurko.fileshare.util.HibernateUtil;

import java.util.Collections;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    @Override
    public User save(User user) {
        User savedUser = null;
        Session session = null;
        Transaction transaction = null;

        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.beginTransaction();
        session.persist(user);
        savedUser = session.get(User.class, user.getId());
        transaction.commit();
        session.close();

        return savedUser;
    }

    @Override
    public User getById(Long id) {
        User user = null;
        Session session = null;
        Transaction transaction = null;

        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.beginTransaction();
        user = session.get(User.class, id);
        transaction.commit();
        session.close();

        return user;
    }

    @Override
    public List<User> getAll() {
        return getAllUsersInternal();
    }

    @Override
    public User update(User editedUser) {
        Session session = null;
        Transaction transaction = null;

        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.beginTransaction();

        User user = session.get(User.class, editedUser.getId());
        user.setFirstName(editedUser.getFirstName());
        user.setLastName(editedUser.getLastName());
        session.merge(user);
        transaction.commit();
        session.close();

        return this.getById(user.getId());
    }

    @Override
    public void deleteById(Long id) {
        Session session = null;
        Transaction transaction = null;

        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.beginTransaction();
        User user = session.get(User.class, id);
        if (user != null) {
            session.remove(user);
        }
        transaction.commit();
        session.close();
    }

    private List<User> getAllUsersInternal() {
        List<User> users = null;
        Session session = null;
        Transaction transaction = null;

        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.beginTransaction();
        users = session.createQuery("FROM User", User.class).getResultList();
        transaction.commit();
        session.close();

        if (users.isEmpty()) {
            return Collections.emptyList();
        } else {
            return users;
        }
    }
}
