package com.nhnacademy.accountAPI.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaConfig {} // @CreatedDate, @LastModifiedDate를 쓰고 있으니 반드시 활성화
