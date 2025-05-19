package com.example.studygroupservice.service;

import com.example.studygroupservice.dto.StudyCreateRequest;
import com.example.studygroupservice.entity.CurriculumWeek;
import com.example.studygroupservice.entity.StudyGroup;
import com.example.studygroupservice.repository.CurriculumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CurriculumService {

    private final CurriculumRepository curriculumRepository;

    public void createCurriculum(List<StudyCreateRequest.CurriculumDto> curriculumDtoList, StudyGroup group) {
        List<CurriculumWeek> weeks = curriculumDtoList.stream()
                .map(dto -> CurriculumWeek.builder()
                        .week(dto.getWeek())
                        .title(dto.getTitle())
                        .topics(dto.getTopics())
                        .studyGroup(group)
                        .build())
                .collect(Collectors.toList());

        curriculumRepository.saveAll(weeks);
    }

    public Page<CurriculumWeek> getCurriculumByStudyId(Long studyId, Pageable pageable) {
        return curriculumRepository.findByStudyGroupId(studyId, pageable);
    }
}

