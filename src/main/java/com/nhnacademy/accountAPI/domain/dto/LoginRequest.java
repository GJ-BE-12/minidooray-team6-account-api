package com.nhnacademy.accountAPI.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// 로그인 dto
public class LoginRequest {
    private String username;
    private String password;
}