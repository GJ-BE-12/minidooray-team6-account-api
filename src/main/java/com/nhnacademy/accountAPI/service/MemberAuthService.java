package com.nhnacademy.accountAPI.service;

import com.nhnacademy.accountAPI.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberAuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberAuthService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 0. 인증 메서드
    @Transactional(readOnly = true)
    public boolean matches(String username, String password) {
        return memberRepository.findByUsername(username)
                .map(member -> passwordEncoder.matches(password, member.getPassword()))
                .orElse(false);
    }

}
