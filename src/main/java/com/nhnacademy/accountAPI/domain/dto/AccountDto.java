package com.nhnacademy.accountAPI.domain.dto;

import com.nhnacademy.accountAPI.domain.entity.Status;

import java.time.LocalDateTime;

public record AccountDto(
        String username,
        String email,
        Status status,
        LocalDateTime createdAt,
        LocalDateTime lastLoginAt) {}