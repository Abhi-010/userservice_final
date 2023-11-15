package com.example.userservice.controller;

import com.example.userservice.dto.SetUserRoleRequestDto;
import com.example.userservice.dto.UserDto;
import com.example.userservice.models.User;
import com.example.userservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<UserDto> getUserDetails(@PathVariable("id") Long userId){
//        UserDto userDto = userService.getUserDetails(userId);
//        return new ResponseEntity<>(userDto, HttpStatus.OK);
//    }

    @PostMapping("/{id}")
    public ResponseEntity<UserDto> setUserRoles(@PathVariable("id") Long userId,
                                                @RequestBody SetUserRoleRequestDto requestDto){
        UserDto userDto =  userService.setUserRoles(userId,requestDto.getRoleId());
        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }
}
