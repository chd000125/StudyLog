package com.example.lastdance.dto;

import com.example.lastdance.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class PostRequestDto {

    private String title;
    private String content;
    private String authorId;
    private String nickname;
    private List<String> tagNames;  // 태그 이름 리스트 추가

    // PostRequestDto를 Post 엔티티로 변환하는 메서드
    public Post toPost() {
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .authorId(this.authorId)
                .nickname(this.nickname)
                .build();
    }
}
