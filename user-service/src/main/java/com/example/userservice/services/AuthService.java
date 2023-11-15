package com.example.userservice.services;

import com.example.userservice.dto.SignupRequestDto;
import com.example.userservice.dto.UserDto;
import com.example.userservice.models.Session;
import com.example.userservice.models.SessionStatus;
import com.example.userservice.models.User;
import com.example.userservice.repository.SessionRepository;
import com.example.userservice.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.bind.annotation.PostMapping;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

@Service
public class AuthService {

    private UserRepository userRepository;
    private SessionRepository sessionRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthService(UserRepository userRepository,SessionRepository sessionRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public UserDto signup(String email, String password){
        User user = new User();
        user.setEmail(email);
        //user.setPassword(password);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        User savedUser = userRepository.save(user);
        return UserDto.from(savedUser);

    }


    public ResponseEntity<UserDto> login(String email, String password){
        Optional<User> userOptional = userRepository.findByEmail(email);

        if(userOptional.isEmpty()){
            return null;
        }
        User user = userOptional.get();
//        if(!user.getPassword().equals(password)){
//            return null;
//        }
        if(!bCryptPasswordEncoder.matches(password,user.getPassword())){
            throw new RuntimeException("Wrong username password");
        }
        // Instead of having 30 random characters as password, we will have JWT token
        // JWT tokens are self validated.

        //String token = RandomStringUtils.randomAlphanumeric(30);

        // Algorithm HS256 We want to use.
        MacAlgorithm alg = Jwts.SIG.HS256;
        //this is the key we are creating using this algo.
        SecretKey key = alg.key().build();

        //json string = payload.
        // user_id
        // email
        // roles

//        String message = "{\n" +
//                "  \"email\": \"naman@scaler.com\",\n" +
//                "  \"roles\": [\"mentor\",\"ta\"]\n" +
//                "}";
        Map<String, Object> jsonForJwt = new HashMap<>();
        jsonForJwt.put("email",user.getEmail());
        jsonForJwt.put("roles", user.getRoles());
        jsonForJwt.put("createdAt", new Date());
        jsonForJwt.put("expiryAt", new Date(LocalDate.now().plusDays(3).toEpochDay()));

        String token = Jwts.builder()
                .claims(jsonForJwt)
                .signWith(key,alg)
                .compact();

        //byte[] content = message.getBytes(StandardCharsets.UTF_8);

        //create a compact JWT
        //String jws = Jwts.builder().content(content, "text/plain").signWith(key, alg).compact();




        Session session = new Session();
        session.setSessionStatus(SessionStatus.ACTIVE);
        //session.setToken(token);
        session.setToken(token);
        session.setUser(user);
        sessionRepository.save(session);

        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        MultiValueMapAdapter<String, String> headers =
                new MultiValueMapAdapter<>(new HashMap<>());
        headers.add(HttpHeaders.SET_COOKIE, "auth-token:" + token);

        ResponseEntity<UserDto> response = new ResponseEntity<>(userDto,
                headers, HttpStatus.OK);
        return response;
    }

    public ResponseEntity<UserDto> logout(String token, Long userid){
        Optional<Session> sessionOptional =
                sessionRepository.findByTokenAndUser_Id(token,userid);
        if(sessionOptional.isEmpty()){
            return null;
        }
        Session session = sessionOptional.get();
        session.setSessionStatus(SessionStatus.ENDED);
        sessionRepository.save(session);
        return ResponseEntity.ok().build();


        //eyJjdHkiOiJ0ZXh0L3BsYWluIiwiYWxnIjoiSFMyNTYifQ.
        // ewogICJlbWFpbCI6ICJuYW1hbkBzY2FsZXIuY29tIiwKICAicm9sZXMiOiBbIm1lbnRvciIsInRhIl0KfQ.
        // fp2Qm8twoVVgWwAp3YvVUrV-_HTLi9z4A6O3XBlhces
        


    }

    public SessionStatus validateToken(String token, Long userId){
        Optional<Session> sessionOptional =
                sessionRepository.findByTokenAndUser_Id(token, userId);
        if (sessionOptional.isEmpty()) {
            return null;
        }
        Session session = sessionOptional.get();
        if(session.getSessionStatus() == SessionStatus.ACTIVE){
            return SessionStatus.ACTIVE;
        }

        return SessionStatus.ENDED;
    }
}
