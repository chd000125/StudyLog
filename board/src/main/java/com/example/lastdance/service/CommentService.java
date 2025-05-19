package com.example.lastdance.service;

import com.example.lastdance.dto.CommentRequestDto;
import com.example.lastdance.dto.CommentResponseDto;
import com.example.lastdance.entity.Comment;
import com.example.lastdance.entity.Post;
import com.example.lastdance.repository.CommentRepository;
import com.example.lastdance.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * CommentService는 댓글 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * 댓글 생성, 수정, 삭제, 조회 기능을 제공합니다.
 */
@Service
@RequiredArgsConstructor
public class CommentService {

    // 댓글 Repository 의존성 주입
    private final CommentRepository commentRepository;

    // 게시글 Repository 의존성 주입 (댓글은 게시글과 연관됨)
    private final PostRepository postRepository;


    public Comment create(CommentRequestDto dto, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        Comment comment = Comment.builder()
                .authorId(dto.getAuthorId())  // 이메일 저장
                .authorName(dto.getAuthorName())
                .content(dto.getContent())
                .post(post)
                .build();

        return commentRepository.save(comment);
    }


    /**
     * 댓글 수정 로직.
     * ID로 기존 댓글을 찾아서 내용만 수정합니다.
     *
     * @param id 수정할 댓글 ID
     * @param updated 수정된 내용이 포함된 댓글 객체
     * @return 수정 후 저장된 댓글 객체
     */
    public Comment update(Long id, Comment updated) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        comment.setContent(updated.getContent());  // 내용만 수정
        return commentRepository.save(comment);
    }

    /**
     * 댓글 삭제 로직.
     *
     * @param id 삭제할 댓글 ID
     */
    public void delete(Long id) {
        commentRepository.deleteById(id);
    }

    /**
     * 특정 게시글(postId)에 대한 모든 댓글을 조회하고,
     * CommentResponseDto 형태로 변환하여 반환합니다.
     *
     * @param postId 조회할 게시글 ID
     * @return 댓글 DTO 리스트
     */
    public List<CommentResponseDto> getByPostId(Long postId) {
        return commentRepository.findAllByPost_pId(postId).stream()
                .map(c -> CommentResponseDto.builder()
                        .postId(c.getPost().getPId())
                        .authorId(c.getAuthorId())
                        .authorName(c.getAuthorName())
                        .content(c.getContent())
                        .createdAt(c.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
