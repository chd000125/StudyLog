package com.example.lastdance.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.example.lastdance.entity.PostDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostAutoCompleteService {

    private final ElasticsearchClient elasticsearchClient;

    public List<String> autocompleteTitle(String prefix) {
        try {
            SearchResponse<PostDocument> response = elasticsearchClient.search(s -> s
                            .index("posts_autocomplete")
                            .query(q -> q
                                    .match(m -> m
                                            .field("titleAutocomplete")
                                            .query(prefix)
                                    )
                            )
                            .size(10),
                    PostDocument.class
            );

            return response.hits().hits().stream()
                    .map(hit -> hit.source().getTitleAutocomplete())  // ✅ 여기 수정
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw new RuntimeException("자동완성 실패", e);
        }
    }

}