//package com.example.gateway.security;
//
//import org.springframework.security.authentication.ReactiveAuthenticationManager;
//import org.springframework.security.core.Authentication;
//import reactor.core.publisher.Mono;
//
//public class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager {
//
//    @Override
//    public Mono<Authentication> authenticate(Authentication authentication) {
//        // 여기선 이미 JwtAuthenticationConverter에서 인증 객체를 만들었기 때문에 그냥 넘김
//        return Mono.just(authentication);
//    }
//}
