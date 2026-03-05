package dev.ismav.userauthservice.repos;

import dev.ismav.userauthservice.models.Role;
import dev.ismav.userauthservice.models.User;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role,Long> {
    @Override
    Optional<Role> findById(Long aLong);
    Optional<Role> findByName(String name);
}
