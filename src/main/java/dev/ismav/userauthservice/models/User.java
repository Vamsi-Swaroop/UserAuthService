package dev.ismav.userauthservice.models;

import dev.ismav.userauthservice.DTOs.UserDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

import java.util.List;

@Entity
public class User extends BaseModel {
    private String email;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String password;
    private String name;

    @ManyToMany
    private List<Role> roles;

    public UserDTO convertToUserDTO(){
        UserDTO userDTO = new UserDTO();
        userDTO.setName(this.getName());
        userDTO.setEmail(this.getEmail());
        userDTO.setName(this.getName());
        userDTO.setId(this.getId());
        return userDTO;
    }

    public void setRoles(List<Role> roles) {
        roles=this.roles;
    }
}
