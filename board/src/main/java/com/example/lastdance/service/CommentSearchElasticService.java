package com.example.lastdance.service;

import com.example.lastdance.entity.CommentDocument;
import com.example.lastdance.repository.CommentSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentSearchElasticService {

    private final CommentSearchRepository commentSearchRepository;

    public Page<CommentDocument> searchByContent(String keyword, Pageable pageable) {
        return commentSearchRepository.findByContentContaining(keyword, pageable);
    }
}
