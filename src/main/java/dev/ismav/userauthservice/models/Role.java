package dev.ismav.userauthservice.models;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;

@Entity
public class Role extends BaseModel{
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    /*
    Can add access and permissions
     */

}
