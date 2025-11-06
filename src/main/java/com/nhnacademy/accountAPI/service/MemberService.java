package com.nhnacademy.accountAPI.service;

import com.nhnacademy.accountAPI.domain.entity.Member;
import com.nhnacademy.accountAPI.domain.dto.MemberCreateCommand;
import com.nhnacademy.accountAPI.domain.entity.Role;
import com.nhnacademy.accountAPI.domain.entity.Status;
import com.nhnacademy.accountAPI.exception.MemberAlreadyExistsException;
import com.nhnacademy.accountAPI.exception.MemberNotFoundException;
import com.nhnacademy.accountAPI.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

// account.api의 역할: 회원 데이터 관리 + 상태 변경 + 내부 인증검증용 API를 제공
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 0. 인증 메서드
    public boolean matches(String username, String password) {
        Member member = memberRepository.findByUsername(username);
        if (Objects.isNull(member)) {
            throw new MemberNotFoundException("member not found.");
        }
        return passwordEncoder.matches(password, member.getPassword());
    }

    // 1. 회원가입
    public Member register(MemberCreateCommand memberCreateCommand) {
        if (memberRepository.existsByUsername(memberCreateCommand.getUsername())) {
            throw new MemberAlreadyExistsException(memberCreateCommand.getUsername() + " already exits");
        }
        String encodePassword = passwordEncoder.encode(memberCreateCommand.getPassword());
        Member member = new Member(memberCreateCommand, encodePassword);
        return memberRepository.save(member);
    }

    // 2. 멤버 조회
    public Member getByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    // 3. 회원 상태 수정
    public Member updateStatus(String username, Status status) {
        Member member = memberRepository.findByUsername(username);
        member.setStatus(status);
        memberRepository.save(member);
        return member;
    }

    // 4. 역할 수정
    public Member updateRole(String username, Role role) {
        Member member = memberRepository.findByUsername(username);
        member.setRole(role);
        memberRepository.save(member);
        return member;
    }

}
