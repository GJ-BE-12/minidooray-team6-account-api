package com.nhnacademy.accountAPI.controller;

import com.nhnacademy.accountAPI.domain.entity.Member;
import com.nhnacademy.accountAPI.domain.entity.Status;
import com.nhnacademy.accountAPI.service.MemberSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class MemberSearchController {

    private final MemberSearchService memberSearchService;

    // 1. 키워드를 통한 검색
    // GET /users?query={searchKeyword}
//    @GetMapping(params = "query")
//    public List<Member> search(@RequestParam String query) {
//        return memberSearchService.search(query);
//    }

    // 전체 데이터가 아닌 username, email만 출력되게 수정
    @GetMapping(params = "query")
    public List<Map<String, String>> search(@RequestParam String query) {
        return memberSearchService.searchKeyword(query).stream()
                .filter(member -> member.getStatus() != Status.WITHDRAWN) // 데이터 노출 차단 - 탈퇴 계정 배제
                .map(member -> Map.of(
                        "username", member.getUsername(),
                        "email", member.getEmail()
                ))
                .toList();
    }

    // 2. usernames 리스트를 통한 일괄 조회
    // GET /users?ids={id1},{id2},{id3}
//    @GetMapping(params = "usernames")
//    public List<Member> findBatch(@RequestParam List<String> usernames) {
//        return memberSearchService.findBatchByUsernames(usernames);
//    }
    @GetMapping(params = "usernames")
    public List<Map<String, String>> findBatch(@RequestParam List<String> usernames) {
        return memberSearchService.findBatchByUsernames(usernames).stream()
                .filter(member -> member.getStatus() != Status.WITHDRAWN)
                .map(member -> Map.of(
                        "username", member.getUsername(),
                        "email", member.getEmail()
                ))
                .toList();
    }

}
