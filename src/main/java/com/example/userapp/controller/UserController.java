package com.example.userapp.controller;

import com.example.userapp.entities.User;
import com.example.userapp.models.UserRequest;
import com.example.userapp.repositories.UserRepository;
import com.example.userapp.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;



    @GetMapping("/page/{page}")
    public Page<User> getPage(@PathVariable Integer page) {
        Pageable pageable = PageRequest.of(page, 3);
        return userService.findAll(pageable);
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return validation(bindingResult);
        }

        Optional<User> userOptional = userService.update(user,id);
        if(userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return validation(bindingResult);
        }
        User userCreated = userService.save(user);
        return ResponseEntity.ok(userCreated);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<User> userOptional = userService.findById(id);
        if(userOptional.isPresent()) {
            userService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/upload/img/{photo:.+}")
    public ResponseEntity<Resource> showPhoto(@PathVariable String photo) throws MalformedURLException {
        Path pathFile = Paths.get("uploads").resolve(photo).toAbsolutePath();

        Resource resource = new UrlResource(pathFile.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("Could not read file: " + pathFile);
        }
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
        return new ResponseEntity<Resource>(resource, HttpStatus.OK);
    }


    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> upload(@Valid @RequestParam("file") MultipartFile file, @RequestParam Long id) {
        Optional<User> userOptional = userService.findById(id);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            userService.uploadPicture(file, user);
            return ResponseEntity.ok(user); // Return the updated user object
        } else {
            return ResponseEntity.notFound().build(); // Handle user not found case
        }
    }

    public ResponseEntity<Map<String, String>> validation(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(fieldError -> {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }


}
