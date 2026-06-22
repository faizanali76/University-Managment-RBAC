package com.university.rbac.service;

import com.university.rbac.dto.LoginRequest;
import com.university.rbac.dto.RegisterRequest;
import com.university.rbac.entity.Role;
import com.university.rbac.entity.User;
import com.university.rbac.repository.RoleRepository;
import com.university.rbac.repository.UserRepository;
import com.university.rbac.security.JwtUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, JwtUtils jwtUtils){
        this.userRepository= userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder =  new BCryptPasswordEncoder();
    }

    // registration

    public String registerUser(RegisterRequest request){
        if(userRepository.findByUsernameOrEmail(request.getUsername(), "").isPresent()){
            throw new RuntimeException("Username is already Taken");
        }

        if(userRepository.findByUsernameOrEmail("", request.getEmail()).isPresent()){
            throw new RuntimeException("Email Already Exist");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        String hashedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(hashedPassword);

        Set<Role> databaseRoles = new HashSet<>();
        if(request.getRoles()!=null){
            for(String roleName : request.getRoles()){
                Role role = roleRepository.findByName(roleName)
                        .orElseThrow(()-> new RuntimeException("Role not found" + roleName));
                        databaseRoles.add(role);
            }
        }
        user.setRoles(databaseRoles);

        userRepository.save(user);
        return "User Registered successfully";
    }

    
    public String loginUser(LoginRequest request){
        User user = userRepository.findByUsernameOrEmail(request.getIdentifier(), request.getIdentifier())
                .orElseThrow(()-> new RuntimeException("Invalid Username or Email"));

        boolean isPasswordMatch = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(!isPasswordMatch){
            throw new RuntimeException("Invalid password credentials");
        }


        String token = jwtUtils.generateToken(user);
        return token;
    }
}

