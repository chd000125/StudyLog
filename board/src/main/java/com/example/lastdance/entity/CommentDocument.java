package com.example.lastdance.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "comments")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class CommentDocument {

    @Id
    private String id;

    private String content;
    private String nickname;
    private String createdAt;
    private Long postId;
}
