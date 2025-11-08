package com.nhnacademy.accountAPI.repository;

import com.nhnacademy.accountAPI.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 데이터베이스 내의 존재 여부
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // 멤버 단건 조회 (로그인/조회)
    Optional<Member> findByUsername(String username);
    Optional<Member> findByEmail(String email);

    // 이메일 중복 검사 시, 자기 자신은 제외하고 검사
    boolean existsByEmailAndUsernameNot(String email, String username);

    // GET /users?ids={id1},{id2},{id3}
    // Task.api에서 받는 username 목록으로 사용자 일괄 조회
    List<Member> findByUsernameIn(Collection<String> usernames);
//    SELECT *
//    FROM member
//    WHERE username IN (:usernames);

    // GET /users?query={searchKeyword}
    // username이나 email에 특정 키워드가 포함된 모든 회원을 찾아서, 페이지 단위로 가져옴
    List<Member> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(String usernameKeyword, String emailKeyword);
//    SELECT *
//    FROM member
//    WHERE LOWER(username) LIKE LOWER(CONCAT('%', :usernameKeyword, '%'))
//    OR LOWER(email) LIKE LOWER(CONCAT('%', :emailKeyword, '%'));

}
