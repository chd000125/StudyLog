package com.example.studygroupservice.service;

import com.example.studygroupservice.entity.StudyGroup;
import com.example.studygroupservice.repository.StudyGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyGroupAdminService {

    private final StudyGroupRepository studyGroupRepository;

    public void delete(Long id) {
        studyGroupRepository.deleteById(id);
    }

}