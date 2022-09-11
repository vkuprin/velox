package com.velox.server.service;


import com.velox.server.models.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();
    void deleteUser(Long id);
    User getUser(Long id);
    void save(User user);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    User findByUsername(String username);

}
