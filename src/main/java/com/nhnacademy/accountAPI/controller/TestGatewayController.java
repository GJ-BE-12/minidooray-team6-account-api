package com.nhnacademy.accountAPI.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequiredArgsConstructor
public class TestGatewayController {
    
    private final RestTemplate restTemplate;
    
    @Value("${api.task.url}")
    private String accountApiUrl;

    @GetMapping("/test/task-communication")
    public String testCommunication(Model model) {
        String result;
        
        try {
            String url = accountApiUrl + "/test/ping";
            
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            
            result = "통신 성공 (상태코드: " + response.getStatusCode() + "): " + response.getBody();
            
        } catch (Exception e) {
            result = " 통신 실패: " + e.getMessage();
        }
        
        model.addAttribute("testResult", result);
        return "testPage";
    }
}