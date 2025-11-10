package com.nhnacademy.accountAPI.controller;

import com.nhnacademy.accountAPI.domain.dto.*;
import com.nhnacademy.accountAPI.domain.entity.Member;
import com.nhnacademy.accountAPI.domain.entity.Status;
import com.nhnacademy.accountAPI.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
        Optional<Member> memberOptional = memberService.login(request.getUsername(), request.getPassword());
        return memberOptional
                .map(member -> {
                    AccountLoginResponse response = new AccountLoginResponse(member.getId(), member.getUsername());
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> ResponseEntity.status(401).build());
    }

    // 3. 로그아웃 — X-USER-ID 검증 (게이트웨이가 세션/토큰 폐기)
    @PostMapping("/{userId}/logout")
    public ResponseEntity<Void> logout(@PathVariable("userId") String username,
                                       @RequestHeader("X-USER-ID") Long headerUserId) {
        Long targetUserId = memberService.getIdByUsername(username);
        if (!targetUserId.equals(headerUserId)) {
            return ResponseEntity.status(403).build(); // 본인 아님
        }
        memberService.logout(username);
        return ResponseEntity.noContent().build(); // 204
    }

    // 4. 멤버 단건 조회 (비밀번호 제외)
    @GetMapping("/{userId}")
    public ResponseEntity<AccountDto> get(@PathVariable("userId") String username) {
        return ResponseEntity.ok(memberService.getProfile(username));
    }

    // 5-1. 회원 정보 수정 (email) — X-USER-ID 검증
    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateEmail(@PathVariable("userId") String username, @RequestHeader("X-USER-ID") Long headerUserId, @Valid @RequestBody AccountUpdateRequest request) {
        Long targetUserId = memberService.getIdByUsername(username); // username → DB에서 id 조회
        if (!targetUserId.equals(headerUserId)) {
            return ResponseEntity.status(403).build(); // 본인 아님
        }
        memberService.updateEmail(username, request);
        return ResponseEntity.ok().build(); // 200 OK, body 없음
    }

    // 5-2. 회원 정보 수정 (비밀번호 변경) — X-USER-ID 검증
    @PutMapping("/{userId}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable("userId") String username, @RequestHeader("X-USER-ID") Long headerUserId, @Valid @RequestBody PasswordUpdateRequest request) {
        Long targetUserId = memberService.getIdByUsername(username);
        if (!targetUserId.equals(headerUserId)) {
            return ResponseEntity.status(403).build(); // 본인 아님
        }
        memberService.updatePassword(username, request);
        return ResponseEntity.ok().build();
    }

    // 6. 회원 탈퇴 — X-USER-ID 검증
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable("userId") String username, @RequestHeader("X-USER-ID") Long headerUserId) {
        Long targetUserId = memberService.getIdByUsername(username);
        if (!targetUserId.equals(headerUserId)) {
            return ResponseEntity.status(403).build(); // 본인 아님
        }
        memberService.delete(username);
        return ResponseEntity.noContent().build();
    }

    // 테스트용) 회원 상태 변경
    @PutMapping("/{userId}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable("userId") String username, @RequestHeader(value = "X-USER-ID", required = false) String headerUserId, @RequestParam("status") Status status) {
        memberService.updateStatus(username, status);
        return ResponseEntity.noContent().build();
    }

}


