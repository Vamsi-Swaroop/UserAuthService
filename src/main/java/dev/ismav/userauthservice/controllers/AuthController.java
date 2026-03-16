package dev.ismav.userauthservice.controllers;

import dev.ismav.userauthservice.DTOs.LoginRequestDTO;
import dev.ismav.userauthservice.DTOs.SignUpRequestDTO;
import dev.ismav.userauthservice.DTOs.UserDTO;
import dev.ismav.userauthservice.DTOs.ValidateTokenDTO;
import dev.ismav.userauthservice.models.User;
import dev.ismav.userauthservice.pojos.UserToken;
import dev.ismav.userauthservice.serivce.IAuthService;
import org.antlr.v4.runtime.misc.MultiMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.util.Pair;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;


    @PostMapping("/signup")
    ResponseEntity<UserDTO> signup(@RequestBody SignUpRequestDTO signupRequestDTO){
        System.out.println("Signup API called with name: " + signupRequestDTO.getName() +
                ", email: " + signupRequestDTO.getEmail());
        try {
            User user = authService.signup(signupRequestDTO.getName(),
                    signupRequestDTO.getEmail(),
                    signupRequestDTO.getPassword());

            UserDTO userDTO = user.convertToUserDTO();

            return new ResponseEntity<>(userDTO, HttpStatus.CREATED);

        }catch (Exception e){
            return null;
        }

    }

    @PostMapping("/login")
    ResponseEntity<UserDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){
        try{

            UserToken userToken = authService.login(
                    loginRequestDTO.getEmail(),
                    loginRequestDTO.getPassword()
            );

//            User user = result.getFirst();
//            String token = result.getSecond();

//            UserDTO userDTO = user.convertToUserDTO();

            /*
            How do we return the token in response?
            We will add the token in Headers part in response Entity

            --Multimap is used to represent headers and
            the key names here should be against which we will add this token

            The token that we are returning back to the client should be in the
            form of COOKIES in the frontend
             */
            MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
            headers.add(HttpHeaders.COOKIE, userToken.getToken());

            //Since directly passing of headers is ddeprecated, we will capitalise another implementation
            HttpHeaders httpHeaders = new HttpHeaders(headers);
            return new ResponseEntity<>(userToken.getUser().convertToUserDTO(), httpHeaders,HttpStatus.OK);

        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/validateToken")
    public ResponseEntity<String> validateToken(@RequestBody ValidateTokenDTO validateTokenDTO){
        boolean result = authService.validateToken(validateTokenDTO.getToken());
        if(!result)
            return new ResponseEntity<>("Please login again",HttpStatus.FORBIDDEN);
        else
            return new ResponseEntity<>("Token is valid", HttpStatus.OK);

    }



}