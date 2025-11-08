package com.nhnacademy.accountAPI.service;

import com.nhnacademy.accountAPI.domain.dto.AccountDto;
import com.nhnacademy.accountAPI.domain.dto.AccountRegisterRequest;
import com.nhnacademy.accountAPI.domain.dto.AccountUpdateRequest;
import com.nhnacademy.accountAPI.domain.dto.PasswordUpdateRequest;
import com.nhnacademy.accountAPI.domain.entity.Member;
import com.nhnacademy.accountAPI.domain.entity.Status;
import com.nhnacademy.accountAPI.exception.MemberAlreadyExistsException;
import com.nhnacademy.accountAPI.exception.MemberNotFoundException;
import com.nhnacademy.accountAPI.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;


// account.api의 역할: 회원 데이터 관리 + 상태 변경 + 내부 인증검증용 API를 제공
@Service
@RequiredArgsConstructor
public class MemberService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 0. 인증 메서드
    private boolean matches(String raw, String password) {
        return passwordEncoder.matches(raw, password);
    }

    // 1. 회원가입
    @Transactional
    public void register(AccountRegisterRequest requset) {
        if (memberRepository.existsByUsername(requset.username())){
            throw new MemberAlreadyExistsException("username exists");
        }
        if (memberRepository.existsByEmail(requset.email())){
            throw new MemberAlreadyExistsException("email exists");
        }
        String encoded = passwordEncoder.encode(requset.password());
        memberRepository.saveAndFlush(new Member(requset, encoded));
    }

    // 2. 로그인
    @Transactional
    public Optional<String> login(String username, String password) {
        return memberRepository.findByUsername(username)
                .filter(member -> passwordEncoder.matches(password, member.getPassword()))
                .map(member -> { member.setLastLoginAt(LocalDateTime.now()); // 성공 시점에만 갱신
                                         return member.getUsername(); // userId 반환
                });
    }

    // 3. 멤버 단건 조회 (비밀번호 제외)
    @Transactional(readOnly = true)
    public AccountDto getProfile(String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new MemberNotFoundException("member not found"));
        return new AccountDto(member.getUsername(), member.getEmail(), member.getStatus(), member.getCreatedAt(), member.getLastLoginAt());
    }

    // 4-1. 회원 정보 수정 (이메일 변경)
    @Transactional
    public void updateEmail(String username, AccountUpdateRequest request) {
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new MemberNotFoundException("member not found"));
        if (request.email() == null || request.email().isBlank() || request.email().equals(member.getEmail())) {
            return; // 바뀐 게 없으면 그냥 종료(200 OK)
        }
        if (memberRepository.existsByEmailAndUsernameNot(request.email(), username)) {
            throw new MemberAlreadyExistsException("email already exists");
        }
        member.setEmail(request.email());
    }

    // 4-2. 회원 정보 수정 (비밀번호 변경)
    @Transactional
    public void updatePassword(String username, PasswordUpdateRequest request) {
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new MemberNotFoundException("member not found"));
        if (!passwordEncoder.matches(request.currentPassword(), member.getPassword())){
            throw new IllegalArgumentException("current password invalid");
        }
        if (!request.newPassword().equals(request.confirmPassword())){
            throw new IllegalArgumentException("password confirm mismatch");
        }
        member.setPassword(passwordEncoder.encode(request.newPassword()));
    }

    // 5. 회원탈퇴
    @Transactional
    public void delete(String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new MemberNotFoundException("member not found"));
        member.setStatus(Status.WITHDRAWN);
    }

}
