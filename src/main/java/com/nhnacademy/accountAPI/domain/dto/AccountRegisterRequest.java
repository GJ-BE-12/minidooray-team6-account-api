package com.nhnacademy.accountAPI.domain.dto;

public record AccountRegisterRequest(
        String username,
        String email,
        String password) {}