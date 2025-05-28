package com.capstone.backend.core.infrastructure.externalAPI.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ExternalApiRequestService {
    private final RestTemplate restTemplate;
    public <T, R> ResponseEntity<R> post(String url, T requestDto, Class<R> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<T> requestEntity = new HttpEntity<>(requestDto, headers);

        try {
            return restTemplate.postForEntity(url, requestEntity, responseType);
        } catch (RestClientException ex) {
            // 로깅, 재시도 등 처리 가능
            throw new RuntimeException("외부 API 요청 실패: " + ex.getMessage(), ex);
        }
    }
}
