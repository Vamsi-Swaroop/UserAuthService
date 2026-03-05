package dev.ismav.userauthservice.serivce;

import dev.ismav.userauthservice.Exceptions.UserAlreadyExistException;
import dev.ismav.userauthservice.Exceptions.UserNotRegisteredException;
import dev.ismav.userauthservice.Exceptions.IncorrectPasswordException;

import dev.ismav.userauthservice.models.Role;
import dev.ismav.userauthservice.models.State;
import dev.ismav.userauthservice.models.User;
import dev.ismav.userauthservice.repos.RoleRepo;
import dev.ismav.userauthservice.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService implements IAuthService{

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Override
    public User signup(String name, String email, String password){

        Optional<User>optionalUser = userRepo.findByEmail(email);
        if(optionalUser.isPresent()){
            throw new UserAlreadyExistException("User already exists, try with a different email");
        }
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(password);
        user.setCreatedAt(new Date());
        user.setLastUpdatedAt(new Date());

        Role role;
        Optional<Role> optionalRole= roleRepo.findByName("DEFAULT");
        if(optionalRole.isEmpty()){
            role = new Role();
            role.setName("DEFAULT");
            role.setCreatedAt(new Date());
            role.setLastUpdatedAt(new Date());
            role.setState(State.ACTIVE);
            roleRepo.save(role);
        }
        else{
            role = optionalRole.get();
        }
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);
        return userRepo.save(user);


    }

    @Override
    public User login(String email, String password){
        Optional<User> userOptional = userRepo.findByEmail(email);
        if(userOptional.isEmpty()){
            throw new UserNotRegisteredException("User is not registered.Please register first");
        }
        User user = userOptional.get();
        if(password.equals(user.getPassword())){
            return user;
        }
        else{
            throw new IncorrectPasswordException("Password is incorrect");
        }

    }
}
