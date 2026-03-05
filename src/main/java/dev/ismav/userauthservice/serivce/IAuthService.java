package dev.ismav.userauthservice.serivce;

import dev.ismav.userauthservice.models.User;

public interface IAuthService {
    User signup(String name, String email, String password);
    User login(String email, String password);
}
