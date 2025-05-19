package com.example.studygroupservice.service;

import com.example.studygroupservice.dto.StudyCreateRequest;
import com.example.studygroupservice.entity.StudyDetail;
import com.example.studygroupservice.entity.StudyGroup;
import com.example.studygroupservice.repository.StudyDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyDetailService {

    private final StudyDetailRepository studyDetailRepository;

    public void createDetail(StudyCreateRequest request, StudyGroup group) {
        StudyDetail detail = StudyDetail.builder()
                .studyGroup(group)
                .category(request.getCategory())
                .location(request.getLocation())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .meetingType(request.getMeetingType())
                .meetingTime(request.getMeetingTime())
                .meetingDay(request.getMeetingDay())
                .requirements(request.getRequirements())
                .tools(request.getTools())
                .benefits(request.getBenefits())
                .tags(request.getTags())
                .build();

        studyDetailRepository.save(detail);
    }

    public StudyDetail getDetailByStudyId(Long studyId) {
        return studyDetailRepository.findByStudyGroupId(studyId);
    }
}
