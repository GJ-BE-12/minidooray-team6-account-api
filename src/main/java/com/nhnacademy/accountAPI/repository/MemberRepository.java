package com.nhnacademy.accountAPI.repository;

import com.nhnacademy.accountAPI.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 데이터베이스 내의 존재 여부
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // 멤버 단건 조회 (로그인/조회)
    Member findByUsername(String username);
    Member findByEmail(String email);

}
