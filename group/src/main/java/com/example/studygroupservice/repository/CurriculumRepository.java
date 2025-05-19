package com.example.studygroupservice.repository;

import com.example.studygroupservice.entity.CurriculumWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CurriculumRepository extends JpaRepository<CurriculumWeek, Long> {
    Page<CurriculumWeek> findByStudyGroupId(Long studyId, Pageable pageable);
}
