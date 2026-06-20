package com.university.rbac.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/test")

public class TestController {

    @GetMapping("/welcome")
    public ResponseEntity<String> getWelcomeMessage(){
        return ResponseEntity.ok("It's University Managment API Testpoint");
    }

    @GetMapping("/student")
    @PreAuthorize("hasRole('STUDENT')")
    public String studentDashboard(){
        return "Access Granted: Welcome to the Student Dashboard Portal";
    }

    @GetMapping("/professor")
    @PreAuthorize("hasRole('PROFESSOR')")
    public String professorDashboard(){
        return "Access Granted: Welcome to Professor Dashboard";
    }

    @GetMapping("/director")
    @PreAuthorize("hasRole('DIRECTOR')")
    public String directorDashboard() {
        return "Access Granted: Welcome to the Director Central Administrative Panel";
    }




}
