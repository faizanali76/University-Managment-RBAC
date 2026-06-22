package com.university.rbac.repository;

import com.university.rbac.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByRollNo(String rollNo);
    boolean existsByRollNo(String rollNo);
}

