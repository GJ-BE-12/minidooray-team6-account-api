package com.nhnacademy.accountAPI.domain.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
    //가입, 휴면, 탈퇴
    ACTIVE, DORMANT, WITHDRAWN;

    @JsonCreator
    public static Status formString(String s) {
        return Status.valueOf(s.toUpperCase());
    }

    @JsonValue
    public String toJson() {
        return this.name().toLowerCase();
    }

}
