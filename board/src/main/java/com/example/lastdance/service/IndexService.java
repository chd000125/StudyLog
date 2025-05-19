package com.example.lastdance.service;

import com.example.lastdance.entity.*;
import com.example.lastdance.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.suggest.Completion;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IndexService {

    private final PostRepository postRepository;
    private final PostSearchRepository postSearchRepository;
    private final BoardRepository boardRepository;
    private final BoardSearchRepository boardSearchRepository;
    private final CommentRepository commentRepository;
    private final CommentSearchRepository commentSearchRepository;

    public void indexAll() {

        // 🔹 1. 게시글 인덱싱
        List<Post> posts = postRepository.findAll();

        List<PostDocument> postDocs = posts.stream().map(post ->{
            return PostDocument.builder()
                    .id(post.getPId().toString())
                    .title(post.getTitle())
                    .titleAutocomplete(post.getTitle())  // 🔹 여기에 자동완성 필드
                    .content(post.getContent())
                    .nickname(post.getNickname())
                    .createdAt(post.getCreatedAt().toString())
                    .build();
        }).toList();
        postSearchRepository.saveAll(postDocs);


        // 🔹 2. 게시판 인덱싱
        List<Board> boards = boardRepository.findAll();

        List<BoardDocument> boardDocs = boards.stream().map(board ->{
            return BoardDocument.builder()
                    .id(board.getBId().toString())
                    .category(board.getCategory())
                    .build();
        }).toList();
        boardSearchRepository.saveAll(boardDocs);

        // 🔹 2. 댓글 인덱싱
        List<Comment> comments = commentRepository.findAll();

        List<CommentDocument> commentDocs = comments.stream().map(c ->
                CommentDocument.builder()
                        .id(c.getCId().toString())
                        .content(c.getContent())
                        .nickname("사용자" + c.getAuthorId()) // 필요시 authorId로 닉네임 불러오기
                        .createdAt(c.getCreatedAt().toString())
                        .postId(c.getPost().getPId())
                        .build()
        ).toList();

        commentSearchRepository.saveAll(commentDocs);
    }
}
