package com.university.rbac.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentCreationRequest {
    private String name;
    private String rollNo;
    private Integer semester;
    private String email;
    private String password;

}
