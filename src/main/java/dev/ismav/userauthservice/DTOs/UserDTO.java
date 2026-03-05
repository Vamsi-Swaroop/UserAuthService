package dev.ismav.userauthservice.DTOs;

import dev.ismav.userauthservice.models.Role;

import java.util.List;

public class UserDTO {
    private String email;
    private String name;
    private Long id;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    private List<Role> roles;
}
