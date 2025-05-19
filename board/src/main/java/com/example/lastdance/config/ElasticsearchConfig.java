//package com.example.lastdance.config;
//
//import co.elastic.clients.elasticsearch.ElasticsearchClient; // 여긴 그대로
//import co.elastic.clients.transport.rest_client.RestClientTransport;
//import co.elastic.clients.json.jackson.JacksonJsonpMapper;
//import org.apache.http.HttpHost;
//import org.elasticsearch.client.RestClient;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class ElasticsearchConfig { // ✅ 클래스 이름 변경
//
//    @Bean
//    public ElasticsearchClient elasticsearchClient() {
//        RestClient restClient = RestClient.builder(
//                new HttpHost("localhost", 8787) // 일반적으로 ES는 9200 포트
//        ).build();
//
//        RestClientTransport transport = new RestClientTransport(
//                restClient, new JacksonJsonpMapper());
//
//        return new ElasticsearchClient(transport);
//    }
//}
