package com.example.studygroupservice.controller;

import com.example.studygroupservice.entity.StudyGroup;
import com.example.studygroupservice.service.StudyGroupAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/group/admin/study-groups")
@RequiredArgsConstructor
public class StudyGroupAdmin {

    private final StudyGroupAdminService adminService;

    // ✅ 스터디 그룹 삭제 (관리자용)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudyGroup(
            @PathVariable Long id
    ) {
        adminService.delete(id);
        return ResponseEntity.ok("스터디 그룹이 삭제되었습니다.");
    }
}
