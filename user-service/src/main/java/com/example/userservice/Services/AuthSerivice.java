package com.example.userservice.Services;

import com.example.userservice.Exceptions.UserAlreadyExistsException;
import com.example.userservice.Exceptions.UserNotFoundException;
import com.example.userservice.Exceptions.WrongPasswordException;
import com.example.userservice.Repository.SessionRepo;
import com.example.userservice.Repository.UserRepo;
import com.example.userservice.models.Session;
import com.example.userservice.models.User;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import com.example.userservice.Exceptions.UserAlreadyExistsException;
import com.example.userservice.Exceptions.UserNotFoundException;
import com.example.userservice.Exceptions.WrongPasswordException;

@Service
public class AuthSerivice {
    private UserRepo userRepo;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private SecretKey key =Keys.hmacShaKeyFor(
            "thisisthesecretkeyjbhjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjkjhhkjhkkkkkkkkkkj"
            .getBytes(StandardCharsets.UTF_8));
    private SessionRepo sessionRepo;

    public AuthSerivice(UserRepo userRepo,BCryptPasswordEncoder bCryptPasswordEncoder,
                        SessionRepo sessionRepo){
        this.userRepo=userRepo;
        this.bCryptPasswordEncoder=bCryptPasswordEncoder;
        this.sessionRepo=sessionRepo;
    }
    public boolean signUp(String email,String password) throws UserAlreadyExistsException{
        if(userRepo.findByEmail(email).isPresent()){
            throw new UserAlreadyExistsException("user with email:" + email + "already exists");
        }
        User user=new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepo.save(user);
        return true;
    }
    public String Login(String email,String password) throws UserNotFoundException, WrongPasswordException {
        Optional<User> userOptional=userRepo.findByEmail(email);

        if(userOptional.isEmpty()){
            throw new UserNotFoundException("user with email:" + email + "not found!");
        }
        boolean matches=bCryptPasswordEncoder.matches(
                password,
                userOptional.get().getPassword());
        if(matches){
            String token=createJwtToken(userOptional.get().getId(),
                    new ArrayList<>(),
                    userOptional.get().getPassword());

            Session session=new Session();
            session.setToken(token);
            session.setUser(userOptional.get());


            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH,30);
            Date datePlus30Days=calendar.getTime();
            session.setExpiringAt(datePlus30Days);

            sessionRepo.save(session);

            return token;
        }
        else{
            throw new WrongPasswordException("Wrong password !");
        }
    }
    public boolean validate(String token){
        try{
            Jws<Claims> claims= Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            Date expiry=claims.getPayload().getExpiration();
            Long userId=claims.getPayload().get("user_id",Long.class);
        }
        catch(Exception e){
            return false;
        }
        return true;
    }
    private String createJwtToken(Long userId, List<String> roles, String email){
        Map<String,Object> dataInJwt=new HashMap<>();
        dataInJwt.put("user_id",userId);
        dataInJwt.put("roles",roles);
        dataInJwt.put("email",email);

        Calendar calendar=Calendar.getInstance();
        Date currentDate=calendar.getTime();

        calendar.add(calendar.DAY_OF_MONTH,30);
        Date datePlus30Days=calendar.getTime();

        String token=Jwts.builder()
                .claims(dataInJwt)
                .expiration(datePlus30Days)
                .issuedAt(new Date())
                .signWith(key)
                .compact();

        return token;
    }


}
