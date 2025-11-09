package com.nhnacademy.accountAPI.controller;

import com.nhnacademy.accountAPI.domain.dto.*;
import com.nhnacademy.accountAPI.service.MemberAuthService;
import com.nhnacademy.accountAPI.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 1. 회원가입
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody AccountRegisterRequest request) {
        memberService.register(request);
        return ResponseEntity.status(201).build();
    }

    // 2. 로그인 (성공 시 lastLoginAt 갱신 + username만 반환)
    @PostMapping("/login")
    public ResponseEntity<AccountLoginResponse> login(@Valid @RequestBody AccountLoginRequest request) {
        return memberService.login(request.username(), request.password())
                .map(username -> ResponseEntity.ok(new AccountLoginResponse(username)))
                .orElseGet(() -> ResponseEntity.status(401).build());
    }

    // 3. 멤버 단건 조회 (비밀번호 제외)
    @GetMapping("/{userId}")
    public ResponseEntity<AccountDto> get(@PathVariable String userId) {
        return ResponseEntity.ok(memberService.getProfile(userId));
    }

    // 4-1. 회원 정보 수정 (email) — X-USER-ID 검증
    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateEmail(@PathVariable String userId, @RequestHeader("X-USER-ID") String headerUserId, @Valid @RequestBody AccountUpdateRequest request) {
        if (!userId.equals(headerUserId)) {
            return ResponseEntity.status(403).build(); // 본인 아님
        }
        memberService.updateEmail(userId, request);
        return ResponseEntity.ok().build(); // 200 OK, body 없음
    }

    // 4-2. 회원 정보 수정 (비밀번호 변경) — X-USER-ID 검증
    @PutMapping("/{userId}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable String userId, @RequestHeader("X-USER-ID") String headerUserId, @Valid @RequestBody PasswordUpdateRequest request) {
        if (!userId.equals(headerUserId)) {
            return ResponseEntity.status(403).build();
        }
        memberService.updatePassword(userId, request);
        return ResponseEntity.ok().build();
    }

    // 5. 회원 탈퇴 — X-USER-ID 검증
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable String userId, @RequestHeader("X-USER-ID") String headerUserId) {
        if (!userId.equals(headerUserId)) {
            return ResponseEntity.status(403).build();
        }
        memberService.delete(userId);
        return ResponseEntity.noContent().build();
    }
}


