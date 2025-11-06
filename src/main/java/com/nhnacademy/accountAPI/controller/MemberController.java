package com.nhnacademy.accountAPI.controller;

import com.nhnacademy.accountAPI.domain.dto.LoginRequest;
import com.nhnacademy.accountAPI.domain.dto.MemberCreateCommand;
import com.nhnacademy.accountAPI.domain.dto.StatusRequest;
import com.nhnacademy.accountAPI.domain.entity.Member;
import com.nhnacademy.accountAPI.exception.LoginFailException;
import com.nhnacademy.accountAPI.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 1. 회원가입
    @PostMapping("/register")
    public ResponseEntity<Member> registerMember(@Valid @RequestBody MemberCreateCommand memberCreateCommand) {
        Member member = memberService.register(memberCreateCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(member);
    }


    // 2. 로그인
    @PostMapping("/login")
    public Member login(@Valid @RequestBody LoginRequest request) {
        if (memberService.matches(request.getUsername(), request.getPassword())) {
            return memberService.getByUsername(request.getUsername());
        }
        throw new LoginFailException(request.getUsername()+": login fail");
    }


    // 3. 멤버 (단건) 조회
    @GetMapping("/{username}")
    public Member getMember(@PathVariable String username) {
        Member member = memberService.getByUsername(username);
        if (Objects.isNull(member)) {
            throw new IllegalArgumentException("Member not found with ID: " + username);
        }
        return member;
    }

    // 4. 상태 변경
    @PatchMapping("/{username}/status")
    public Member updateStatus(@PathVariable String username, @Valid @RequestBody StatusRequest request) {
        return memberService.updateStatus(username, request.getStatus());
    }

}


