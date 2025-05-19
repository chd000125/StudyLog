package com.example.studygroupservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class StudyGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ownerId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer maxMember;
    private Integer currentMember;

    private String status; // 모집중, 마감 등

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = "모집중";
        if (this.currentMember == null) this.currentMember = 0;
    }

    // 연관관계
    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyMember> members = new ArrayList<>();

    @OneToOne(mappedBy = "studyGroup", cascade = CascadeType.ALL)
    private StudyDetail detail;

    @OneToMany(mappedBy = "studyGroup", cascade = CascadeType.ALL)
    private List<CurriculumWeek> curriculum = new ArrayList<>();
}
