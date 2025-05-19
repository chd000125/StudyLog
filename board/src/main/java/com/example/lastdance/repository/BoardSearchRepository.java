package com.example.lastdance.repository;


import com.example.lastdance.entity.BoardDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BoardSearchRepository extends ElasticsearchRepository<BoardDocument, String> {
    // 카테고리 검색 (예: '공지' 포함 등)
    Page<BoardDocument> findByCategoryContaining(String keyword, Pageable pageable);
}
