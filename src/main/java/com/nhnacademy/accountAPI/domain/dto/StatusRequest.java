package com.nhnacademy.accountAPI.domain.dto;

import com.nhnacademy.accountAPI.domain.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// 상태 설정 dto
public class StatusRequest {
    private String username;
    private Status status;
}
