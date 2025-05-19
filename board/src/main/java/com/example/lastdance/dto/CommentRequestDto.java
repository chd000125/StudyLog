package com.example.lastdance.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
    private String authorId; // 이메일
    private String content;
    private String authorName;
}
