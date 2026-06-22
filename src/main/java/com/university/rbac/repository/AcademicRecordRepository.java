package com.university.rbac.repository;

import com.university.rbac.entity.AcademicRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AcademicRecordRepository extends JpaRepository<AcademicRecord, Long> {
    Optional<AcademicRecord> findByStudentRollNo(String rollNo);
}
