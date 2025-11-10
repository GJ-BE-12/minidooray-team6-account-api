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

    // 1. 회원가입
    @Transactional
    public void register(AccountRegisterRequest requset) {
        if (memberRepository.existsByUsername(requset.getUsername())){
            throw new MemberAlreadyExistsException("username exists");
        }
        if (memberRepository.existsByEmail(requset.getEmail())){
            throw new MemberAlreadyExistsException("email exists");
        }
        String encoded = passwordEncoder.encode(requset.getPassword());
        memberRepository.saveAndFlush(new Member(requset, encoded));
    }

    // 2. 로그인
    @Transactional
    public Optional<Member> login(String username, String password) {
        return memberRepository.findByUsername(username)
                .filter(member -> member.getStatus() != Status.WITHDRAWN) // 로그인 시 status = "탈퇴"이면 로그인 불가
                .filter(member -> passwordEncoder.matches(password, member.getPassword()))
                .map(member -> {
                    if (member.getStatus() == Status.DORMANT){
                        member.setStatus(Status.ACTIVE); // 휴면 해제
                    }
                    member.setLastLoginAt(LocalDateTime.now()); // 성공 시점에만 갱신
                    return member;
                });
    }

    // 3. 로그아웃
    @Transactional
    public void logout(String username) {}

    // 4. 멤버 단건 조회 (비밀번호 제외)
    @Transactional(readOnly = true)
    public AccountDto getProfile(String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new MemberNotFoundException("member not found"));
        return new AccountDto(member.getId(), member.getUsername(), member.getEmail(), String.valueOf(member.getStatus()), member.getCreatedAt(), member.getLastLoginAt());
    }


    // 5-1. 회원 정보 수정 (이메일 변경)
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

    // 5-2. 회원 정보 수정 (비밀번호 변경)
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

    // 6. 회원탈퇴
    @Transactional
    public void delete(String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new MemberNotFoundException("member not found"));
        member.setStatus(Status.WITHDRAWN);
    }

    // 테스트용) 회원 상태 변경
    @Transactional
    public void updateStatus(String username, Status status){
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new MemberNotFoundException("member not found"));
        member.setStatus(status);
    }

    // 보조) username을 통해 id(X-USER-ID) 가져오기
    @Transactional(readOnly = true)
    public Long getIdByUsername(String username) {
        return memberRepository.findByUsername(username)
                .map(Member::getId)
                .orElseThrow(() -> new MemberNotFoundException("member not found"));
    }

}
