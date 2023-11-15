package com.example.userservice.controller;

import com.example.userservice.dto.CreateRoleRequestDto;
import com.example.userservice.models.Role;
import com.example.userservice.services.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
public class RoleController {
    private RoleService roleService;
    public RoleController(RoleService roleService){
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody CreateRoleRequestDto createRoleRequestDto){
        Role role = roleService.createRole(createRoleRequestDto.getName());
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

}
