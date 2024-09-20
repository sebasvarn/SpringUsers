package com.example.userapp.services;

import com.example.userapp.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface UserService {
    List<User> findAll();
    Page<User> findAll(Pageable pageable);
    Optional<User> findById(Long id);
    User save(User user);
    void deleteById(Long id);

}
