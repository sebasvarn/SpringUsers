package com.example.userapp.services;

import com.example.userapp.entities.Role;
import com.example.userapp.entities.User;
import com.example.userapp.models.UserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


public interface UserService {
    List<User> findAll();
    Page<User> findAll(Pageable pageable);
    Optional<User> findById(Long id);
    User save(User user);
    Optional<User> update(UserRequest user, Long id);
    void uploadPicture(MultipartFile file, User user);
    void deleteById(Long id);
}
