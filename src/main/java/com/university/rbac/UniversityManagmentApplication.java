package com.university.rbac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class UniversityManagmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniversityManagmentApplication.class, args);
    }

}
