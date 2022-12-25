package org.zhurko.fileshare.repository;

import org.zhurko.fileshare.entity.EventEntity;

import java.util.List;

public interface EventRepository extends GenericRepository<EventEntity, Long> {

    List<EventEntity> getByUserId(Long id);
}
