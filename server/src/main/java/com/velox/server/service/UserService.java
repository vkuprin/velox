package com.velox.server.service;


import com.velox.server.models.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();
    void deleteUser(Long id);
    User getUser(Long id);

}
