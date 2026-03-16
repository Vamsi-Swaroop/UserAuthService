package dev.ismav.userauthservice.serivce;

import dev.ismav.userauthservice.pojos.UserToken;
import org.springframework.data.util.Pair;
import dev.ismav.userauthservice.models.User;

public interface IAuthService {
    User signup(String name, String email, String password);
    UserToken login(String email, String password);
}
