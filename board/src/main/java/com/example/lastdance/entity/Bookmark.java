package com.example.lastdance.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bookmark")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "m_id")
    private Long mId;

    @Column(name = "u_email", nullable = false)
    private String uEmail;

    @Column(name = "p_id", nullable = false)
    private Long pId;
}
