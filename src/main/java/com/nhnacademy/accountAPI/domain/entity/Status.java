package com.nhnacademy.accountAPI.domain.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
    //가입, 탈퇴, 휴면
    ACTIVE, DELETED, DORMANT;

    @JsonCreator
    public static Status formString(String s) {
        return Status.valueOf(s.toUpperCase());
    }

    @JsonValue
    public String toJson() {
        return this.name().toLowerCase();
    }

}
