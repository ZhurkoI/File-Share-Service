package org.zhurko.fileshare.service;

import org.zhurko.fileshare.model.User;
import org.zhurko.fileshare.repository.UserRepository;

import java.util.List;

public class UserService {

    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User save(User user) {
        return userRepo.save(user);
    }

    public User getById(Long id) {
        return userRepo.getById(id);
    }

    public List<User> getAll() {
        return userRepo.getAll();
    }

    public User update(User user) {
        return userRepo.update(user);
    }

    public void deleteById(Long id) {
        userRepo.deleteById(id);
    }
}
