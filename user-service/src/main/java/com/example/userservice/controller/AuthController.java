package com.example.userservice.controller;

import com.example.userservice.dto.*;
import com.example.userservice.models.SessionStatus;
import com.example.userservice.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthService authService;
    public AuthController(AuthService userService){
        this.authService = userService;
    }

    @RequestMapping("/world")
    public String helloWorld(){
        return "Hello World from user service application..";
    }

   @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignupRequestDto signupRequestDto){
        UserDto userDto =
                authService.signup(signupRequestDto.getEmail(),signupRequestDto.getPassword());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDto){
        return authService.login(loginRequestDto.getEmail(),loginRequestDto.getPassword());
    }

    @PostMapping("/logout")
    public ResponseEntity<UserDto> logout(@RequestBody LogoutRequestDto logoutRequestDto){
        return authService.logout(logoutRequestDto.getToken(),logoutRequestDto.getUserid());
    }

    @PostMapping("/validate")
    public ResponseEntity<SessionStatus> validateToke(ValidateTokenDto validateTokenDto){
        SessionStatus sessionStatus =
                authService.validateToken(validateTokenDto.getToken(),validateTokenDto.getUserid());
        return new ResponseEntity<>(sessionStatus,HttpStatus.OK);
    }
}
