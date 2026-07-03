package com.university.rbac.controller;


import com.university.rbac.dto.JwtResponse;
import com.university.rbac.dto.LoginRequest;
import com.university.rbac.dto.RegisterRequest;
import com.university.rbac.dto.TokenRefreshRequest;
import com.university.rbac.entity.RefreshToken;
import com.university.rbac.entity.User;
import com.university.rbac.repository.UserRepository;
import com.university.rbac.security.JwtUtils;
import com.university.rbac.service.AuthService;
import com.university.rbac.service.RefreshTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    public AuthController(AuthService authService, JwtUtils jwtUtils,
                          RefreshTokenService refreshTokenService, UserRepository userRepository){
        this.authService = authService;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
        this.userRepository = userRepository;
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request){
        String result = authService.registerUser(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        authService.loginUser(request);

        User user = userRepository.findByUsernameOrEmail(request.getIdentifier(), request.getIdentifier())
                .orElseThrow(()-> new RuntimeException("User not found"));

        String accessToken = jwtUtils.generateToken(user);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken.getToken(), user.getUsername()));

    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request){
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String newAccessToken = jwtUtils.generateToken(user);
                    return ResponseEntity.ok(new JwtResponse(newAccessToken, requestRefreshToken, user.getUsername()));
                })
                .orElseThrow(()-> new RuntimeException("Refresh Token is invalid"));
    }
}


