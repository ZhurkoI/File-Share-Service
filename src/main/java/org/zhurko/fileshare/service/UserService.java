package org.zhurko.fileshare.service;

import org.zhurko.fileshare.entity.UserEntity;
import org.zhurko.fileshare.repository.UserRepository;

import java.util.List;

public class UserService {

    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public UserEntity save(UserEntity user) {
        return userRepo.save(user);
    }

    public UserEntity getById(Long id) {
        return userRepo.getById(id);
    }

    public List<UserEntity> getAll() {
        return userRepo.getAll();
    }

    public UserEntity update(UserEntity user) {
        return userRepo.update(user);
    }

    public void deleteById(Long id) {
        userRepo.deleteById(id);
    }
}
