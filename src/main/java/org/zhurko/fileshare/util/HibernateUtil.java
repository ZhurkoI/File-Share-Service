package org.zhurko.fileshare.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private HibernateUtil() {
    }

    private static SessionFactory sessionFactory = null;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                sessionFactory = new Configuration().configure().buildSessionFactory();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}
