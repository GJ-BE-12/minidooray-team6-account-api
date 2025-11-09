package com.nhnacademy.accountAPI.service;

import com.nhnacademy.accountAPI.domain.entity.Member;
import com.nhnacademy.accountAPI.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MemberSearchService {
    private final MemberRepository memberRepository;

    public MemberSearchService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 1. 키워드를 통한 검색
    // GET /users?query={searchKeyword}
    public List<Member> searchKeyword(String searchKeyword) {
        String keyword = (searchKeyword == null) ? "" : searchKeyword.trim();
        return memberRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword);
    }

    // 2. usernames 리스트를 통한 일괄 조회
    // GET /users?ids={id1},{id2},{id3}
    public List<Member> findBatchByUsernames(List<String> usernames) {
        if (usernames == null || usernames.isEmpty()) return List.of();
        return memberRepository.findByUsernameIn(usernames);
    }
}
