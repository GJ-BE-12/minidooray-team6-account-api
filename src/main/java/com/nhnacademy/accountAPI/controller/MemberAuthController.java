package com.nhnacademy.accountAPI.controller;

import com.nhnacademy.accountAPI.domain.dto.AccountLoginRequest;
import com.nhnacademy.accountAPI.service.MemberAuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class MemberAuthController {

    private final MemberAuthService memberAuthService;

    public MemberAuthController(MemberAuthService memberAuthService) {
        this.memberAuthService = memberAuthService;
    }

    // 1.비밀번호 대조 성공 여부 (게이트웨이에 boolean으로 전송)
    @PostMapping("/auth/check")
    public ResponseEntity<AuthCheckResponse> checkPassword(@RequestBody @Valid AccountLoginRequest request) {
        boolean ok = memberAuthService.matches(request.username(), request.password());
        return ok ? ResponseEntity.ok(new AuthCheckResponse(true, "OK"))
                : ResponseEntity.status(401).body(new AuthCheckResponse(false, "INVALID_CREDENTIALS"));
    }

    // Account-API가 비교 후 결과를 돌려줄 때 필요
    public record AuthCheckResponse(boolean valid, String reason) {}
}

