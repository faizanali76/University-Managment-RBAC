package com.university.rbac.dto;

import lombok.Data;

@Data
public class TokenRefreshRequest {

    private String refreshToken;
}
