package com.example.userapp.services;

import com.example.userapp.entities.User;

import java.util.List;
import java.util.Optional;


public interface UserService {
    List<User> findAll();
    Optional<User> findById(Long id);
    User save(User user);
    void deleteById(Long id);
}
