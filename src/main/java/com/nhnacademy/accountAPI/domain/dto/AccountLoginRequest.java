package com.nhnacademy.accountAPI.domain.dto;

public record AccountLoginRequest(
        String username,
        String password) {}