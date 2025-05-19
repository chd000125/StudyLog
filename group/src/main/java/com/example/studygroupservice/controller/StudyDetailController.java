package com.example.studygroupservice.controller;

import com.example.studygroupservice.entity.StudyDetail;
import com.example.studygroupservice.service.StudyDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/group/study-details")
@RequiredArgsConstructor
public class StudyDetailController {

    private final StudyDetailService studyDetailService;

    // 필요 시 스터디 상세 정보 단건 조회
    @GetMapping("/{studyId}")
    public ResponseEntity<StudyDetail> getDetail(@PathVariable Long studyId) {
        return ResponseEntity.ok(studyDetailService.getDetailByStudyId(studyId));
    }
}
