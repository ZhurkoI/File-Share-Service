package org.zhurko.fileshare.repository.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zhurko.fileshare.entity.FileEntity;
import org.zhurko.fileshare.repository.FileRepository;
import org.zhurko.fileshare.util.HibernateUtil;

import java.util.Collections;
import java.util.List;

public class FileRepositoryImpl implements FileRepository {
    @Override
    public FileEntity save(FileEntity file) {
        FileEntity savedFile;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(file);
            savedFile = session.get(FileEntity.class, file.getId());
            transaction.commit();
        }

        return savedFile;
    }

    @Override
    public FileEntity getById(Long id) {
        FileEntity file;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM FileEntity F LEFT JOIN FETCH F.events WHERE F.id = :id";
            file = session.createQuery(hql, FileEntity.class).setParameter("id", id).uniqueResult();
        }

        return file;
    }

    @Override
    public List<FileEntity> getAll() {
        return getAllFilesInternal();
    }

    @Override
    public FileEntity update(FileEntity editedFile) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            FileEntity file = session.get(FileEntity.class, editedFile.getId());
            if (file != null) {
                file.setFilePath(editedFile.getFilePath());
                file.setEvents(editedFile.getEvents());
                session.merge(file);
                transaction.commit();
                return this.getById(file.getId());
            } else {
                return null;
            }
        }
    }

    @Override
    public void deleteById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            FileEntity file = session.get(FileEntity.class, id);
            if (file != null) {
                session.remove(file);
            }
        }
    }

    private List<FileEntity> getAllFilesInternal() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM FileEntity F LEFT JOIN FETCH F.events ORDER BY F.id ASC";
            List<FileEntity> files = session.createQuery(hql, FileEntity.class).getResultList();

            if (!files.isEmpty()) {
                return files;
            } else {
                return Collections.emptyList();
            }
        }
    }
}
