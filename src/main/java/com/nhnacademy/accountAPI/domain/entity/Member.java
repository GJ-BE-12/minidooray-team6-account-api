package com.nhnacademy.accountAPI.domain.entity;

import com.nhnacademy.accountAPI.domain.dto.AccountRegisterRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "account")
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true, length=100)
    private String username;

    @Column(nullable=false, unique=true, length=255)
    private String email;

    @Column(nullable=false, length=255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Status status;

    @CreatedDate
    @Column(name="created_at", nullable=false, updatable=false)
    private LocalDateTime createdAt;

    // ✅ 로그인 성공 시에만 명시적으로 갱신할 필드이므로 Auditing/PreUpdate 제거
    @Column(name="last_login_at")
    private LocalDateTime lastLoginAt;

    protected Member() {}
    public Member(AccountRegisterRequest req, String encodedPassword) {
        this.username = req.username();
        this.email = req.email();
        this.password = encodedPassword;
        this.status = Status.ACTIVE;
    }

    @PrePersist
    void prePersist() {
        if (status == null) status = Status.ACTIVE;
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (lastLoginAt == null) lastLoginAt = createdAt; // 로그인 때 갱신
    }
}

