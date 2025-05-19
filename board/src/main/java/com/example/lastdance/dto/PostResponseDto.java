package com.example.lastdance.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
public class PostResponseDto {

    private Long id;
    private String title;
    private String content;
    private String authorId;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long boardId;
    private Integer viewCount;
    private Integer commentCount;  // 댓글 수 필드 추가
    private Set<TagRequestDto> tags;

    // 기존 생성자 그대로 두고, commentCount를 추가한 생성자도 정의 (롬복의 @Builder가 이미 있음)
    public PostResponseDto(Long id, String title, String content, String authorId, String nickname,
                           LocalDateTime createdAt, LocalDateTime updatedAt, Long boardId,
                           Integer viewCount, Integer commentCount, Set<TagRequestDto> tags) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.boardId = boardId;
        this.viewCount = viewCount;
        this.commentCount = commentCount;  // 댓글 수 필드 초기화
        this.tags = tags;
    }
}