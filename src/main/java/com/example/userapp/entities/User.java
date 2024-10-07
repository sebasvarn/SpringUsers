package com.example.userapp.entities;

import com.example.userapp.models.IUser;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements IUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String lastname;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    @Size(min = 6, max = 20)
    private String username;
    @NotBlank
    private String password;

    private String photo;

    @Override
    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @Transient
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean admin;

    @JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
    @ManyToMany()
    @JoinTable(
        name = "user_roles",
        joinColumns = {@JoinColumn(name = "user_id")},
        inverseJoinColumns = @JoinColumn(name="role_id"),
        uniqueConstraints = { @UniqueConstraint(columnNames = {"user_id", "role_id"})}
    )
    private List<Role> roles;

    public User(List<Role> roles) {
        this.roles = roles;
    }

    public User() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
