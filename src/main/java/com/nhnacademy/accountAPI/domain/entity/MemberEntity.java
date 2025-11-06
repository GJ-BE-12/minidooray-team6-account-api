package com.nhnacademy.accountAPI.domain.entity;

import com.nhnacademy.accountAPI.domain.dto.MemberCreateCommand;
import lombok.Getter;

// 내부 저장 도메인
@Getter
public class MemberEntity {
    private String username;
    private String email;
    private String password;

    public MemberEntity(MemberCreateCommand memberCreateCommand, String encodedPassword) {
        this.username = memberCreateCommand.getUsername();
        this.email = memberCreateCommand.getEmail();
        this.password = encodedPassword;
    }
}
