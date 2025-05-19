package com.example.studygroupservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class StudyMember {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "study_id", nullable = false)
    private StudyGroup study;

    private String userId;

    private String status; // "대기", "수락"

    private LocalDateTime appliedAt;

    @PrePersist
    public void onApply() {
        this.appliedAt = LocalDateTime.now();
        if (this.status == null) this.status = "대기";
    }
}
