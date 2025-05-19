package com.example.studygroupservice.controller;

import com.example.studygroupservice.dto.StudyCreateRequest;
import com.example.studygroupservice.dto.StudyGroupSummary;
import com.example.studygroupservice.entity.StudyGroup;
import com.example.studygroupservice.service.StudyGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/group/study-groups")
@RequiredArgsConstructor
public class StudyGroupController {

    private final StudyGroupService studyGroupService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody StudyCreateRequest request) {
        studyGroupService.create(request);
        return ResponseEntity.ok("스터디 생성 완료");
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<StudyGroupSummary>> getPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(studyGroupService.getPagedSummary(page, size));
    }

    // ✅ 단건 요약 DTO 반환
    @GetMapping("/{id}")
    public ResponseEntity<StudyGroupSummary> getById(@PathVariable Long id) {
        return ResponseEntity.ok(studyGroupService.getSummaryById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudyGroup> update(
            @PathVariable Long id,
            @RequestBody StudyGroup group,
            @RequestParam Long userId
    ) {
        return ResponseEntity.ok(studyGroupService.update(id, group, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        studyGroupService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<StudyGroup> close(@PathVariable Long id) {
        return ResponseEntity.ok(studyGroupService.close(id));
    }

    @GetMapping("/managed")
    public ResponseEntity<Page<StudyGroupSummary>> getManagedGroups(
            @RequestParam String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(studyGroupService.getManagedGroupsByEmail(email, page, size));
    }

    @GetMapping("/joined")
    public ResponseEntity<Page<StudyGroupSummary>> getJoinedGroups(
            @RequestParam String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(studyGroupService.getJoinedGroupsByEmail(email, page, size));
    }


}
