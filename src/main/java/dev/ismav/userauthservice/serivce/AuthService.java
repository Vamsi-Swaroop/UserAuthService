package dev.ismav.userauthservice.serivce;
import dev.ismav.userauthservice.pojos.UserToken;
import dev.ismav.userauthservice.repos.SessionRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import org.springframework.data.util.Pair;

import dev.ismav.userauthservice.Exceptions.UserAlreadyExistException;
import dev.ismav.userauthservice.Exceptions.UserNotRegisteredException;
import dev.ismav.userauthservice.Exceptions.IncorrectPasswordException;
import dev.ismav.userauthservice.models.Session;
import io.jsonwebtoken.security.MacAlgorithm;
import io.jsonwebtoken.Jwts;
import dev.ismav.userauthservice.models.Role;
import dev.ismav.userauthservice.models.State;
import dev.ismav.userauthservice.models.User;
import dev.ismav.userauthservice.repos.RoleRepo;
import dev.ismav.userauthservice.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;

@Service
public class AuthService implements IAuthService{

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private SessionRepo sessionRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SecretKey secretKey;

    //BCryptpasswordEncoder encode() adds random salt and cost factor to the password
    /*
    Random salt means a random string
    - CostFactor means Hashing. More CostFactor means more Hashing thus more security
    - This will(can) not be decoded to original password

    In login() we need to compare rawPassword and encoded password

    matches(rawPassword, encodedPassword) -> returns true or false

    How does macthes() work??
    Step-1: Extracts the costFactor and salt from the password
    Step-2: ReHashes the rawPassword with same salt and Cost Fatcor
    Step-3:Compares both passwords

     */

    @Override
    public User signup(String name, String email, String password){

        Optional<User>optionalUser = userRepo.findByEmail(email);
        if(optionalUser.isPresent()){
            throw new UserAlreadyExistException("User already exists, try with a different email");
        }
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(bCryptPasswordEncoder.encode(password));
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
    public UserToken login(String email, String password){
        Optional<User> userOptional = userRepo.findByEmail(email);
        if(userOptional.isEmpty()){
            throw new UserNotRegisteredException("User is not registered.Please register first");
        }
        User user = userOptional.get();
        if(bCryptPasswordEncoder.matches(password,user.getPassword())){
            HashMap<String,Object> payload = new HashMap<>();
            Long nowInMillis = System.currentTimeMillis();
            payload.put("iat",nowInMillis);
            payload.put("exp", nowInMillis+600000);
            payload.put("userId", userOptional.get().getId());
            payload.put("iss","god");
            payload.put("scope", user.getRoles());

            /*till here payload is generated

             */
//            MacAlgorithm algorithm = Jwts.SIG.HS256;
//            SecretKey secretKey = algorithm.key().build();
            String token = Jwts.builder().claims(payload).
                            signWith(secretKey).compact();
            /*
            Creating a  new session for the logged in user
             */
            Session session = new Session();
            session.setToken(token);
            session.setUser(user);
            session.setState(State.ACTIVE);
            sessionRepo.save(session);

            /*
            We also want to return the token back to the client
             */
            return new UserToken(user,token);
        }
        else{
            throw new IncorrectPasswordException("Password is incorrect");
        }
    }
    @Override
    public boolean validateToken(String token) {

        Optional<Session> optionalSession = sessionRepo.findByToken(token);
        if(optionalSession.isEmpty()){
            return false;
        }
        JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
        Claims claims = jwtParser.parseSignedClaims(token).getPayload();
        Long expiryTime =(Long) claims.get("exp");
        Long nowInMills = System.currentTimeMillis();
        if(nowInMills> expiryTime){
            Session session = optionalSession.get();
            session.setState(State.INACTIVE);
            sessionRepo.save(session);
            return false;
        }
        return true;
    }
}
