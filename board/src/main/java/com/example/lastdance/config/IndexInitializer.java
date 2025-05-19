package com.example.lastdance.config;

import com.example.lastdance.service.IndexService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IndexInitializer {

    private final IndexService indexService;

    @PostConstruct
    public void init() {
        indexService.indexAll();
        System.out.println("🔥 Elasticsearch 초기 색인 완료");
    }
}