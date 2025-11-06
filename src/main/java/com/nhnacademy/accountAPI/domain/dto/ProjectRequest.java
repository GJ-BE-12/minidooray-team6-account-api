package com.nhnacademy.accountAPI.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// 프로젝트 생성 dto
public class ProjectRequest {

    private long projectMemberId;
    private long projecrId;
    private long accountId;
    private LocalDateTime createdAt;

}
