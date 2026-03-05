package dev.ismav.userauthservice.controllers;

import dev.ismav.userauthservice.DTOs.LoginRequestDTO;
import dev.ismav.userauthservice.DTOs.SignUpRequestDTO;
import dev.ismav.userauthservice.DTOs.UserDTO;
import dev.ismav.userauthservice.models.User;
import dev.ismav.userauthservice.serivce.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    /*
    Building :
    1.Sign Up API:
        -Post Type API
            Request Type
                |_name
                |_Email
                |_Password
     - Return Type is Response Entity
                            |
                           UserDTO
                             |_name
                             |_Email
            200 if registration is successful,else error code
    2.LogIn (Get)

        -Request Type: GetMapping

     */
    @Autowired
    private IAuthService authService;

    @PostMapping("/signup")
    ResponseEntity<UserDTO> signUp(@RequestBody SignUpRequestDTO signUpRequestDTO){
        try{
            User user = authService.signup(signUpRequestDTO.getName(),signUpRequestDTO.getEmail(),
                    signUpRequestDTO.getEmail());
            UserDTO userDTO = user.convertToUserDTO();
            return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
        }catch(Exception e){
            return null;
        }
    }

    ResponseEntity<UserDTO> login(LoginRequestDTO loginRequestDTO){
        try{
            User user = authService.login(loginRequestDTO.getEmail(),
                    loginRequestDTO.getPassword());
            UserDTO userDTO = user.convertToUserDTO();
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        }catch(Exception e){
            return null;
        }
    }

}
