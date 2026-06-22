package com.university.rbac.controller;


import com.university.rbac.dto.RecordUpdateRequest;
import com.university.rbac.dto.StudentCreationRequest;
import com.university.rbac.entity.AcademicRecord;
import com.university.rbac.service.ManagmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/managment")
public class ManagmentController {

    private final ManagmentService managmentService;

    ManagmentController(ManagmentService managmentService){
        this.managmentService = managmentService;
    }

    //create student
    @PostMapping("/student")
    @PreAuthorize("hasRole('DIRECTOR')")
    public ResponseEntity<String> onboardStudent(@RequestBody StudentCreationRequest request){
        String message = managmentService.obBoardNewStudent(request);
        return ResponseEntity.ok(message);
    }

    //update record
    @PutMapping("/records")
    @PreAuthorize("hasAnyRole('PROFESSOR', 'DIRECTOR')")
    public ResponseEntity<String> updateStudentRecord(@RequestBody RecordUpdateRequest request){
        String message = managmentService.updateStudentRecord(request);
        return ResponseEntity.ok(message);
    }

    // reset record
    @PutMapping("/records/reset")
    @PreAuthorize("hasRole('DIRECTOR')")
    public ResponseEntity<String> resetAcademicRecord(@RequestParam String rollNo){
        String message = managmentService.clearAcademicRecord(rollNo);
        return ResponseEntity.ok(message);
    }

    //view student
    @GetMapping("/my-card")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<AcademicRecord> getMyAcademicCard(){
        String loggedInRollNo = SecurityContextHolder.getContext().getAuthentication().getName();

        AcademicRecord card = managmentService.getStudentPersonalCard(loggedInRollNo);
        return ResponseEntity.ok(card);

    }


    //delete student

    @DeleteMapping("students/{id}")
    @PreAuthorize("hasRole('DIRECTOR')")
    public ResponseEntity<String> purgeStudentAccount(@PathVariable Long id) {
        String message = managmentService.deleteStudent(id);
        return ResponseEntity.ok(message);
    }


    @GetMapping("/records/all")
    @PreAuthorize("hasAnyRole('DIRECTOR', 'PROFESSOR')")
    public ResponseEntity<List<AcademicRecord>> fetchAllRecords() {
        List<AcademicRecord> collection = managmentService.getAllAcademicRecord();
        return ResponseEntity.ok(collection);
    }
}
