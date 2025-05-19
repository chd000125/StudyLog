package com.example.lastdance.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
public class CommentResponseDto {

    private Long postId;
    private String authorId;
    private String authorName;
    private String content;
    private LocalDateTime createdAt;

}