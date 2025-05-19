package com.example.lastdance.repository;

import com.example.lastdance.entity.PostDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PostSearchRepository extends ElasticsearchRepository<PostDocument, String> {
    Page<PostDocument> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);
}