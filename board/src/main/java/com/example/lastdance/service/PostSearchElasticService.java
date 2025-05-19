package com.example.lastdance.service;

import com.example.lastdance.entity.PostDocument;
import com.example.lastdance.repository.PostSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // 생성자 주입을 자동으로 생성해줌
public class PostSearchElasticService {

    private final PostSearchRepository postSearchRepository;

    /**
     * 엘라스틱서치에서 title 또는 content에 keyword가 포함된 게시글을 검색합니다.
     *
     * @param keyword 검색어 (title 또는 content에 포함되어야 함)
     * @param pageable 페이징 정보를 담고 있는 객체
     * @return 검색 결과 페이지 (PostDocument 목록)
     */

    public Page<PostDocument> searchElastic(String keyword, Pageable pageable) {
        // findByTitleContainingOrContentContaining은 PostSearchRepository에서 정의됨
        return postSearchRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
    }
}