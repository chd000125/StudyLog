package com.example.studygroupservice.repository;

import com.example.studygroupservice.entity.StudyGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {

    // 📌 이메일(ownerId) 기준으로 관리 중인 스터디 페이징 조회
    Page<StudyGroup> findByOwnerId(String ownerId, Pageable pageable);
}
