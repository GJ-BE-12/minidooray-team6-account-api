package com.nhnacademy.accountAPI.controller;

import com.nhnacademy.accountAPI.domain.entity.Member;
import com.nhnacademy.accountAPI.domain.entity.Role;
import com.nhnacademy.accountAPI.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/members")
public class MemberInternalController {

    private final MemberService memberService;

    public MemberInternalController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 1) 역할 단건 조회 (Task가 질의)
    @GetMapping("/{username}/role")
    public ResponseEntity<RoleResponse> getRole(@PathVariable String username) {
        Member m = memberService.getByUsername(username);
        return ResponseEntity.ok(new RoleResponse(m.getUsername(), m.getRole().name()));
    }

    // 2) 역할 보유 여부 확인 (Task가 권한 체크할 때)
    @PostMapping("/role-check")
    public ResponseEntity<RoleCheckResult> hasRole(@Valid @RequestBody RoleCheckRequest req) {
        Member m = memberService.getByUsername(req.username());
        boolean ok = m.getRole().name().equalsIgnoreCase(req.requiredRole());
        return ResponseEntity.ok(new RoleCheckResult(m.getUsername(), req.requiredRole(), ok));
    }

    // 3) 역할 변경 (Task가 워크플로우로 역할 승격/강등 지시 가능)
    @PatchMapping("/{username}/role")
    public ResponseEntity<RoleResponse> updateRole(@PathVariable String username,
                                                   @Valid @RequestBody RoleUpdateRequest req) {
        Member updated = memberService.updateRole(username, Role.valueOf(req.newRole().toUpperCase()));
        return ResponseEntity.ok(new RoleResponse(updated.getUsername(), updated.getRole().name()));
    }

    public record RoleResponse(String username, String role) {}
    public record RoleCheckRequest(String username, String requiredRole) {}
    public record RoleCheckResult(String username, String requiredRole, boolean hasRole) {}
    public record RoleUpdateRequest(String newRole) {}
}
