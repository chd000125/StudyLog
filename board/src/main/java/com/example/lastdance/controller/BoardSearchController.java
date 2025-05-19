package com.example.lastdance.controller;

import com.example.lastdance.entity.BoardDocument;
import com.example.lastdance.service.BoardSearchElasticService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards/search")
@RequiredArgsConstructor
public class BoardSearchController {

    private final BoardSearchElasticService boardSearchElasticService;

    /**
     * 보드 category에 keyword가 포함된 항목을 검색합니다.
     *
     * @param keyword 검색어
     * @param page 페이지 번호 (기본값: 0)
     * @param size 페이지 크기 (기본값: 10)
     * @return 검색 결과 페이지
     */

    @GetMapping
    public ResponseEntity<Page<BoardDocument>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(boardSearchElasticService.searchElastic(keyword, pageable));
    }
}
