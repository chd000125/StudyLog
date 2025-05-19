package com.example.lastdance.service;

import com.example.lastdance.dto.PostResponseDto;
import com.example.lastdance.dto.TagRequestDto;
import com.example.lastdance.entity.Board;
import com.example.lastdance.entity.Post;
import com.example.lastdance.entity.Tag;
import com.example.lastdance.repository.BoardRepository;
import com.example.lastdance.repository.PostRepository;
import com.example.lastdance.repository.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final BoardRepository boardRepository;
    private final TagRepository tagRepository;

    public Post create(Post post, Long boardId, List<String> tagNames) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));
        post.setBoard(board);

        List<Tag> tags = tagRepository.findAllByNameIn(tagNames);
        post.setTags(new HashSet<>(tags));

        return postRepository.save(post);
    }

    public Post update(Long id, Post updated) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        post.setTitle(updated.getTitle());
        post.setContent(updated.getContent());

        return postRepository.save(post);
    }

    public void delete(Long id) {
        postRepository.deleteById(id);
    }

    public List<PostResponseDto> getAllByBoard(Long boardId) {
        return postRepository.findAll().stream()
                .filter(post -> post.getBoard().getBId().equals(boardId))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public PostResponseDto getById(Long id) {
        return postRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }

    public Page<PostResponseDto> getPostsByTags(Set<String> tags, Pageable pageable) {
        // 태그로 게시글 조회
        Page<Post> postPage = postRepository.findByTags(tags, pageable);

        if (tags != null && !tags.isEmpty()) {
            // 태그가 있으면 태그로 게시글 조회
            postPage = postRepository.findByTags(tags, pageable);
        } else {
            // 태그가 없으면 모든 게시글 조회
            postPage = postRepository.findAllLatest(pageable);
        }

        // Post 엔티티를 PostResponseDto로 변환
        List<PostResponseDto> dtoList = postPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, postPage.getTotalElements());
    }

    public Page<PostResponseDto> getAllPaged(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    // boardId와 tags로 게시글 조회
    public Page<PostResponseDto> getPostsByBoard(Long boardId, Set<String> tags, Pageable pageable) {
        if (tags == null || tags.isEmpty()) {
            // 태그가 없으면 그냥 boardId로 필터링
            return postRepository.findByBoardId(boardId, pageable).map(this::convertToDto);
        } else {
            // 태그가 있으면 boardId와 태그를 모두 기준으로 필터링
            return postRepository.findByBoardIdAndTags(boardId, tags, pageable).map(this::convertToDto);
        }
    }

    private PostResponseDto convertToDto(Post post) {

        int commentCount = post.getComments().size();

        return PostResponseDto.builder()
                .id(post.getPId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorId(post.getAuthorId())
                .nickname(post.getNickname())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .viewCount(post.getViewCount())
                .boardId(post.getBoard().getBId())
                .commentCount(commentCount)
                .tags(convertTags(post.getTags()))
                .build();
    }

    private Set<TagRequestDto> convertTags(Set<Tag> tags) {
        return tags.stream()
                .map(tag -> TagRequestDto.builder()
                        .id(tag.getId())
                        .name(tag.getName())
                        .build())
                .collect(Collectors.toSet());
    }

    // Post → PostResponseDto 변환 메서드
    private PostResponseDto toDto(Post post) {
        return PostResponseDto.builder()
                .id(post.getPId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorId(post.getAuthorId())
                .nickname(post.getNickname())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .viewCount(post.getViewCount())
                .boardId(post.getBoard().getBId())
                .build();
    }

    @Transactional
    public PostResponseDto getByIdAndIncreaseView(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        post.setViewCount(post.getViewCount() + 1); // 조회수 증가
        return toDto(post); // 변경된 post를 DTO로 반환
    }

    public Page<PostResponseDto> getPostsByAuthor(String authorId, Pageable pageable) {
        return postRepository.findByAuthorId(authorId, pageable)
                .map(this::convertToDto);
    }

}