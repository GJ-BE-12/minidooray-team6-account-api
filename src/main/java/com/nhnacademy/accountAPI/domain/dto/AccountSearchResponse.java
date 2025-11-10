package com.nhnacademy.accountAPI.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountSearchResponse {
    private Long accountId; // ◀◀◀ [필수] 게이트웨이의 AccountDto.accountId와 매칭
    private String username;
    private String email;
}
