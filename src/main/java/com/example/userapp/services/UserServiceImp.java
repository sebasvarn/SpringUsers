package com.example.userapp.services;

import com.example.userapp.entities.Role;
import com.example.userapp.entities.User;
import com.example.userapp.models.IUser;
import com.example.userapp.models.UserRequest;
import com.example.userapp.repositories.RoleRepository;
import com.example.userapp.repositories.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    private final PasswordEncoder passwordEncoder;

    public UserServiceImp(UserRepository userRepository, RoleRepository rolRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<User> findAll(Pageable pageable) {
        return this.userRepository.findAll(pageable).map(user -> {
            boolean admin = user.getRoles().stream().anyMatch(role -> "ROLE_ADMIN".equals(role.getName()));
            user.setAdmin(admin);
            return user;
        });
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return ((List<User>) userRepository.findAll()).stream().map(user -> {
            boolean admin = user.getRoles().stream().anyMatch(role -> "ROLE_ADMIN".equals(role.getName()));
            user.setAdmin(admin);
            return user;
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    @Override
    public User save(User user) {

        user.setRoles(getRoles(user));

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public Optional<User> update(UserRequest user, Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()) {
            User updatedUser = userOptional.get();
            updatedUser.setName(user.getName());
            updatedUser.setLastname(user.getLastname());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setUsername(user.getUsername());

            updatedUser.setRoles(getRoles(user));

            return Optional.of(userRepository.save(updatedUser));
        }
        return Optional.empty();
    }

    @Transactional
    @Override
    public void uploadPicture(MultipartFile file, User user) {
        if (!file.isEmpty()) {

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename().replace(" ","");
            Path uploads = Paths.get("uploads");
            Path pathFile = uploads.resolve(fileName).toAbsolutePath();

            try {
                Files.copy(file.getInputStream(), pathFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String lastPhoto = user.getPhoto();

            deletePhoto(lastPhoto);

            userRepository.save(user);
        }else {
            throw new RuntimeException("File is empty");
        }

    }

    private void deletePhoto(String photo) {
        if (photo != null && !photo.isEmpty()) {
            Path pathLastPhoto =
                    Paths.get("uploads").resolve(photo).toAbsolutePath();
            File fileLastPhoto = pathLastPhoto.toFile();
            if (fileLastPhoto.exists() && fileLastPhoto.canRead()) {
                fileLastPhoto.delete();
            }
        }
    }


    private List<Role> getRoles(IUser updatedUser) {
        List<Role> roles = new ArrayList<>();
        Optional<Role> roleUser = this.roleRepository.findByName("ROLE_USER");
        roleUser.ifPresent(roles::add);

        if(updatedUser.isAdmin()){
            Optional<Role> roleAdmin = this.roleRepository.findByName("ROLE_ADMIN");
            roleAdmin.ifPresent(roles::add);
        }
        return roles;

    }



    @Transactional
    @Override
    public void deleteById(Long id) {
        Optional<User> userOptional = findById(id);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            deletePhoto(user.getPhoto());
        }
        userRepository.deleteById(id);

    }

}
