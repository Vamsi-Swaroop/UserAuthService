package dev.ismav.userauthservice.repos;

import dev.ismav.userauthservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    @Override
    Optional<User> findById(Long aLong);
    Optional<User> findByEmail(String email);
}
