package com.nhnacademy.accountAPI.controller;

import com.nhnacademy.accountAPI.domain.dto.AccountSearchResponse;
import com.nhnacademy.accountAPI.domain.entity.Member;
import com.nhnacademy.accountAPI.domain.entity.Status;
import com.nhnacademy.accountAPI.service.MemberSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class MemberSearchController {

    private final MemberSearchService memberSearchService;

    // 1. 키워드를 통한 검색
    // GET /users?query={searchKeyword}
    // 전체 데이터가 아닌 username, email만 출력되게 수정
    @GetMapping(params = "query")
    public List<AccountSearchResponse> search(@RequestParam String query) {
        return memberSearchService.searchKeyword(query).stream()
                .filter(member -> member.getStatus() != Status.WITHDRAWN)
                .map(member -> new AccountSearchResponse(
                        member.getId(),
                        member.getUsername(),
                        member.getEmail()
                ))
                .toList();
    }

    // 2. id 리스트를 통한 일괄 조회
    // GET /users?ids={id1},{id2},{id3}
    // @GetMapping(params = "ids")로 해서 엔드포인드 localhost:8081/users?ids=1,2,3 로 하면 안되나?
//    GET http://localhost:8081/users?query=im
//    GET http://localhost:8081/users/batch?ids=1,2,3
    @GetMapping("/batch")
    public List<AccountSearchResponse> findBatch(@RequestParam List<Long> ids) {
        return memberSearchService.findBatchByIds(ids).stream()
                .filter(member -> member.getStatus() != Status.WITHDRAWN)
                .map(member -> new AccountSearchResponse(
                        member.getId(),
                        member.getUsername(),
                        member.getEmail()
                ))
                .toList();
    }

}
