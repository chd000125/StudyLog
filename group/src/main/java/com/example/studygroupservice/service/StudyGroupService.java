package com.example.studygroupservice.service;

import com.example.studygroupservice.dto.StudyCreateRequest;
import com.example.studygroupservice.dto.StudyGroupSummary;
import com.example.studygroupservice.entity.StudyGroup;
import com.example.studygroupservice.entity.StudyMember;
import com.example.studygroupservice.repository.StudyGroupRepository;
import com.example.studygroupservice.repository.StudyMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyGroupService {

    private final StudyGroupRepository studyGroupRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final StudyDetailService studyDetailService;
    private final CurriculumService curriculumService;

    @Transactional
    public void create(StudyCreateRequest request) {
        StudyGroup group = StudyGroup.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .ownerId(String.valueOf(request.getUserId()))
                .maxMember(request.getMaxMembers())
                .currentMember(1)
                .status("모집중")
                .build();
        studyGroupRepository.save(group);

        studyDetailService.createDetail(request, group);
        curriculumService.createCurriculum(request.getCurriculum(), group);

        StudyMember master = StudyMember.builder()
                .study(group)
                .userId(request.getUserId())
                .status("마스터")
                .build();
        studyMemberRepository.save(master);
    }

    public Page<StudyGroupSummary> getPagedSummary(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return studyGroupRepository.findAll(pageable)
                .map(this::toSummary);
    }

    public StudyGroup getById(Long id) {
        return studyGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("스터디 그룹이 없습니다. ID: " + id));
    }

    public StudyGroupSummary getSummaryById(Long id) {
        StudyGroup group = getById(id);
        return toSummary(group);
    }

    public StudyGroup update(Long id, StudyGroup updated, Long userId) {
        StudyGroup group = getById(id);
        if (!group.getOwnerId().equals(String.valueOf(userId))) {
            throw new IllegalArgumentException("해당 글을 수정할 권한이 없습니다.");
        }

        group.setTitle(updated.getTitle());
        group.setDescription(updated.getDescription());
        group.setMaxMember(updated.getMaxMember());
        group.setStatus(updated.getStatus());

        return studyGroupRepository.save(group);
    }

    public void delete(Long id, Long userId) {
        StudyGroup group = getById(id);
        if (!group.getOwnerId().equals(String.valueOf(userId))) {
            throw new IllegalArgumentException("해당 글을 삭제할 권한이 없습니다.");
        }

        studyGroupRepository.deleteById(id);
    }

    public StudyGroup close(Long id) {
        StudyGroup group = getById(id);
        group.setStatus("마감");
        return studyGroupRepository.save(group);
    }

    public Page<StudyGroupSummary> getManagedGroupsByEmail(String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return studyGroupRepository.findByOwnerId(email, pageable)
                .map(this::toSummary);
    }


    public Page<StudyGroupSummary> getJoinedGroupsByEmail(String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("appliedAt").descending());
        Page<StudyMember> members = studyMemberRepository.findByUserIdAndStatus(email, "수락", pageable);
        return members.map(member -> toSummary(member.getStudy()));
    }

    private StudyGroupSummary toSummary(StudyGroup group) {
        var detail = group.getDetail();
        return StudyGroupSummary.builder()
                .id(group.getId())
                .title(group.getTitle())
                .ownerId(group.getOwnerId())
                .description(group.getDescription())
                .category(detail != null ? detail.getCategory() : null)
                .location(detail != null ? detail.getLocation() : null)
                .currentMember(group.getCurrentMember())
                .maxMember(group.getMaxMember())
                .startDate(detail != null ? detail.getStartDate() : null)
                .meetingDay(detail != null ? detail.getMeetingDay() : null)
                .meetingTime(detail != null ? detail.getMeetingTime() : null)
                .status(group.getStatus()) // ✅ 상태 필드 추가
                .build();
    }
}
