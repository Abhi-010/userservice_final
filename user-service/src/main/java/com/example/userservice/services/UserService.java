package com.example.userservice.services;

import com.example.userservice.dto.UserDto;
import com.example.userservice.models.Role;
import com.example.userservice.models.User;
import com.example.userservice.repository.RoleRepository;
import com.example.userservice.repository.UserRepository;
import org.hibernate.boot.model.process.internal.UserTypeResolution;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public UserDto getUserDetails(Long userId){
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()){
            return null;
        }
        User user = optionalUser.get();
        return UserDto.from(user);
    }

    public UserDto setUserRoles(Long userId, Long roleId){
        return null;
        //Optional<User> userOptional = userRepository.findById(userId);
//        Role role = roleRepository.findAllByIdIn(roleId);
//
//        if (userOptional.isEmpty()) {
//            return null;
//        }
//
//        User user = userOptional.get();
//        Set<Role> roleList = new HashSet<>();
//        roleList.add(role);
//
//        user.setRoles(Set.copyOf(roleList));
//
//        User savedUser = userRepository.save(user);
//
//        return UserDto.from(savedUser);

//        Optional<User> userOptional = userRepository.findById(userId);
//        if(userOptional.isEmpty()){
//            return null;
//        }
//        User user = userOptional.get();
//
//        for(int i = 0 ; i < roleIds.size() ; i++){
//            Long roleId = roleIds.get(i);
//            Optional<Role> roleOptional = roleRepository.findById(roleId);
//            if(!roleOptional.isEmpty()){
//                user.getRoles().add(roleOptional.get());
//            }
//        }
//        userRepository.save(user);
//        return new ResponseEntity<>(UserDto.from(user), HttpStatus.OK);

    }

}
