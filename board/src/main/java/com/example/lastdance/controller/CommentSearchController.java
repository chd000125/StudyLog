package com.example.lastdance.controller;

import com.example.lastdance.entity.BoardDocument;
import com.example.lastdance.entity.CommentDocument;
import com.example.lastdance.service.CommentSearchElasticService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments/search")
@RequiredArgsConstructor
public class CommentSearchController {

    private final CommentSearchElasticService commentSearchElasticService;

    @GetMapping
    public ResponseEntity<Page<CommentDocument>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(commentSearchElasticService.searchByContent(keyword, pageable));
    }
}
