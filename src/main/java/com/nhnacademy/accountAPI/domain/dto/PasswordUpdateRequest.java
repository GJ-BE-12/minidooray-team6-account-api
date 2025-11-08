package com.nhnacademy.accountAPI.domain.dto;

public record PasswordUpdateRequest(
        String currentPassword,
        String newPassword,
        String confirmPassword) {}