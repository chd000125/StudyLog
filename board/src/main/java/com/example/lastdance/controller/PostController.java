package com.example.lastdance.controller;

import com.example.lastdance.dto.PostRequestDto;
import com.example.lastdance.entity.Post;
import com.example.lastdance.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.lastdance.dto.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api/boards/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/paged")
    public ResponseEntity<Page<PostResponseDto>> getAllPaged(Pageable pageable) {
        return ResponseEntity.ok(postService.getAllPaged(pageable));
    }

    @PostMapping("/{boardId}")
    public ResponseEntity<Post> create(@PathVariable Long boardId,
                                       @RequestBody PostRequestDto postRequestDto) {
        // PostRequestDto에는 Post 정보와 태그 이름 리스트가 포함됩니다.
        return ResponseEntity.ok(postService.create(
                postRequestDto.toPost(),  // PostRequestDto를 Post로 변환
                boardId,
                postRequestDto.getTagNames()  // 태그 이름 리스트 전달
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> update(@PathVariable Long id, @RequestBody Post post) {
        return ResponseEntity.ok(postService.update(id, post));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllByBoard(@RequestParam Long boardId) {
        return ResponseEntity.ok(postService.getAllByBoard(boardId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<PostResponseDto>> getAllPosts(
            @RequestParam(required = false) Set<String> tags,
            Pageable pageable) {
        return ResponseEntity.ok(postService.getPostsByTags(tags, pageable));
    }

    @GetMapping("/by-board/{boardId}")
    public ResponseEntity<Page<PostResponseDto>> getPostsByBoard(
            @PathVariable Long boardId,
            @RequestParam(value = "tags", required = false) String tags, // 태그를 쿼리 파라미터로 받음
            Pageable pageable
    ) {
        // tags 파라미터가 있으면 쉼표로 구분하여 Set<String>으로 변환
        Set<String> tagSet = tags != null ? new HashSet<>(Set.of(tags.split(","))) : null;

        // 태그와 boardId로 게시글을 필터링하여 반환
        return ResponseEntity.ok(postService.getPostsByBoard(boardId, tagSet, pageable));
    }

    @GetMapping("/{id}/view")
    public ResponseEntity<PostResponseDto> getByIdAndIncreaseView(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getByIdAndIncreaseView(id));
    }

    @GetMapping("/by-author")
    public ResponseEntity<Page<PostResponseDto>> getPostsByAuthor(
            @RequestParam String email,
            Pageable pageable
    ) {
        return ResponseEntity.ok(postService.getPostsByAuthor(email, pageable));
    }



}