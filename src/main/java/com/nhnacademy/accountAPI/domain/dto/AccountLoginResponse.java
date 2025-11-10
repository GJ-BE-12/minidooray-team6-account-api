package com.nhnacademy.accountAPI.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountLoginResponse {
    private Long id;
    private String username; //-> account-api가 반환해줄 사용자 Id
}