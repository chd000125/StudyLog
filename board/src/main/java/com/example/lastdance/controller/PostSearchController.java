package com.example.lastdance.controller;

import com.example.lastdance.entity.PostDocument;
import com.example.lastdance.repository.PostSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/search")
@RequiredArgsConstructor
public class PostSearchController {

    private final PostSearchRepository postSearchRepository;

    @GetMapping
    public Page<PostDocument> search(@RequestParam String keyword,
                                     @RequestParam(defaultValue = "0")int page,
                                     @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postSearchRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
    }
}
