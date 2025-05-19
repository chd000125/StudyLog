package com.example.lastdance.repository;

import com.example.lastdance.entity.CommentDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CommentSearchRepository extends ElasticsearchRepository<CommentDocument, String> {
    Page<CommentDocument> findByContentContaining(String keyword, Pageable pageable);
    Page<CommentDocument> findByPostId(Long postId, Pageable pageable);
}
