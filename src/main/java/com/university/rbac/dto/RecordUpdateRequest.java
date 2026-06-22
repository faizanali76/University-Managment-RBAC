package com.university.rbac.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecordUpdateRequest {
    private String rollNo;
    private Double finalGpa;
    private Double averageAttendance;
}