package com.velox.server.service.impl;

import com.velox.server.exception.NoSuchUserException;
import com.velox.server.models.User;
import com.velox.server.repository.UserRepository;
import com.velox.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + email));
    }

    @Override
    public void verifyEmail(String email) {
        User user = findByEmail(email);
        user.setEmailVerified(true);
        userRepository.save(user);
    }

    @Override
    public void changePassword(String email, String password) {
        User user = findByEmail(email);
        user.setPassword(password);
        userRepository.save(user);
    }

    @Override
    public void updatePassword(User user, String oldPass, String newPass) {
        if (!user.getPassword().equals(encoder.encode(oldPass))) {
            throw new BadCredentialsException("Wrong password");
        }
        user.setPassword(encoder.encode(newPass));
        userRepository.save(user);
    }

    @Override
    public User updateUser(User oldUser, User newUser) {
        if (!ObjectUtils.isEmpty(newUser.getFirstName()))
            oldUser.setFirstName(newUser.getFirstName());
        if (!ObjectUtils.isEmpty(newUser.getLastName()))
            oldUser.setLastName(newUser.getLastName());
        if (!ObjectUtils.isEmpty(newUser.getCountry()))
            oldUser.setCountry(newUser.getCountry());
        if (!ObjectUtils.isEmpty(newUser.getDob()))
            oldUser.setDob(newUser.getDob());
        if (!ObjectUtils.isEmpty(newUser.getRoles()))
            oldUser.setRoles(newUser.getRoles());
        if (!ObjectUtils.isEmpty(newUser.getUsername()))
            oldUser.setUsername(newUser.getUsername());
        if (newUser.getEmailVerified())
            oldUser.setEmailVerified(newUser.getEmailVerified());

        userRepository.save(oldUser);

        return oldUser;
    }

    @Override
    public void suspend(long id, boolean suspend) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setSuspend(suspend);
            userRepository.save(user);
        } else {
            throw new NoSuchUserException(id);
        }

    }


}
