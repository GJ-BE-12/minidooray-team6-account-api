package com.nhnacademy.accountAPI.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nhnacademy.accountAPI.domain.dto.MemberCreateCommand;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "account")
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    @JsonProperty("accountId")
    private long accountId;

    @NotNull
    @Length(min = 1, max = 50)
    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    @NotNull
    @Length(min = 1, max = 100)
    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @NotNull
    @Length(min = 1, max = 255)
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private Status status;

    @Column(name = "provider", nullable = false, length = 20)
    private String provider; // 기본값 "local"

    @Column(name = "provider_id", length = 100)
    private String providerId;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20) // ★ DDL에 추가 필요
    private Role role;

    protected Member() {}

    public Member(MemberCreateCommand memberCreateCommand, String encodedPassword) {
        this.username = memberCreateCommand.getUsername();
        this.email = memberCreateCommand.getEmail();
        this.password = encodedPassword;
        this.status = Status.ACTIVE;
        this.provider = "local";
        this.role = Role.USER;
    }

    @PrePersist
    void prePersist() {
        if (provider == null) provider = "local";
        if (status == null) status = Status.ACTIVE;
        if (role == null) role = Role.USER;
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (updatedAt == null) updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
