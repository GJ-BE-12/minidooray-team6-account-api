package com.nhnacademy.accountAPI.controller;

import com.nhnacademy.accountAPI.domain.entity.Member;
import com.nhnacademy.accountAPI.service.MemberSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class MemberSearchController {

    private MemberSearchService memberSearchService;

    // 1. 키워드를 통한 검색
    // GET /users?query={searchKeyword}
    @GetMapping(params = "query")
    public List<Member> search(@RequestParam String query) {
        return memberSearchService.search(query);
    }

    // 2. usernames 리스트를 통한 일괄 조회
    // GET /users?ids={id1},{id2},{id3}
    @GetMapping(params = "usernames")
    public List<Member> findBatch(@RequestParam List<String> usernames) {
        return memberSearchService.findBatchByUsernames(usernames);
    }

}
