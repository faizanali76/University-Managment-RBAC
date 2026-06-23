package com.university.rbac.service;

import com.university.rbac.dto.RecordUpdateRequest;
import com.university.rbac.dto.StudentCreationRequest;
import com.university.rbac.entity.AcademicRecord;
import com.university.rbac.entity.Role;
import com.university.rbac.entity.Student;
import com.university.rbac.entity.User;
import com.university.rbac.repository.AcademicRecordRepository;
import com.university.rbac.repository.RoleRepository;
import com.university.rbac.repository.StudentRepository;
import com.university.rbac.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ManagmentService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final AcademicRecordRepository recordRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public ManagmentService(UserRepository userRepository, StudentRepository studentRepository,
                             AcademicRecordRepository recordRepository, RoleRepository roleRepository,
                             PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.recordRepository = recordRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    //create student

    @Transactional
    public String obBoardNewStudent(StudentCreationRequest request){
        if(studentRepository.existsByRollNo(request.getRollNo())){
            throw new RuntimeException("Roll No with same student Already Exist");
        }
        //setting user for student
        User userAccount = new User();
        userAccount.setUsername(request.getRollNo());
        userAccount.setEmail(request.getEmail());
        userAccount.setPassword(passwordEncoder.encode(request.getPassword()));

        Role role = roleRepository.findByName("ROLE_STUDENT")
                .orElseThrow(()-> new RuntimeException("ROLE_STUDENT not initialized in DB"));
        userAccount.setRoles(Collections.singleton(role));

        // student profile

        Student studentProfile = new Student();

        studentProfile.setName(request.getName());
        studentProfile.setRollNo(request.getRollNo());
        studentProfile.setSemester(request.getSemester());
        studentProfile.setUser(userAccount);

        Student savedStudent = studentRepository.save(studentProfile);

        AcademicRecord blankRecord = new AcademicRecord();
        blankRecord.setFinalGpa(0.0);
        blankRecord.setAverageAttendance(0.0);
        blankRecord.setStudent(savedStudent);
        recordRepository.save(blankRecord);

        return "Student onboarding complete for Roll Number: " + request.getRollNo();
    }


    //view student card
    public AcademicRecord getStudentPersonalCard(String rollNo) {
        return recordRepository.findByStudentRollNo(rollNo)
                .orElseThrow(() -> new RuntimeException("No registered academic record card link found!"));
    }


    public List<AcademicRecord> getAllAcademicRecord(){
        return recordRepository.findAll();
    }

    //Update record

    public String updateStudentRecord(RecordUpdateRequest request) {
        AcademicRecord record = recordRepository.findByStudentRollNo(request.getRollNo())
                .orElseThrow(() -> new RuntimeException("No academic record found for this roll number!"));

        record.setFinalGpa(request.getFinalGpa());
        record.setAverageAttendance(request.getAverageAttendance());

        recordRepository.save(record);
        return "Grades successfully modified for: " + request.getRollNo();
    }



    // delete record

    public String clearAcademicRecord(String rollNo){
        AcademicRecord record = recordRepository.findByStudentRollNo(rollNo)
                .orElseThrow(()-> new RuntimeException("Academic Record Not Found!"));

            record.setFinalGpa(0.0);
            record.setAverageAttendance(0.0);
            recordRepository.save(record);

        return "Academic metrics reset to zero. Student account profile remains intact.";

    }


    @Transactional
    public String deleteStudent(Long id){
        Student student = studentRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Student ID not found"));

        AcademicRecord record = recordRepository.findByStudentRollNo(student.getRollNo()).orElse(null);

        if(record!=null){
            recordRepository.delete(record);
        }

        studentRepository.delete(student);
        return "Student Data and and profile deleted";
    }
}
