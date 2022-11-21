package org.zhurko.fileshare.repository;

import java.util.List;

public interface GenericRepository<T, ID> {

    T save(T t);

    T getById(ID id);

    List<T> getAll();

    T update(T t);

    void deleteById(ID id);
}
