package com.example.lastdance.service;

import com.example.lastdance.entity.BoardDocument;
import com.example.lastdance.repository.BoardSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardSearchElasticService {

    private final BoardSearchRepository boardSearchRepository;

    /**
     * 엘라스틱서치에서 category 필드에 keyword가 포함된 보드를 검색합니다.
     *
     * @param keyword 검색어 (category에 포함된)
     * @param pageable 페이징 정보
     * @return 검색 결과 (BoardDocument Page)
     */
    public Page<BoardDocument> searchElastic(String keyword, Pageable pageable) {
        return boardSearchRepository.findByCategoryContaining(keyword, pageable);
    }
}
