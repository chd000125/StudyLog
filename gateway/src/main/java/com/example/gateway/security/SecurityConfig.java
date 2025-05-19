//package com.example.gateway.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
//
//@EnableWebFluxSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
//        return http
//                .csrf(ServerHttpSecurity.CsrfSpec::disable)
//                .authorizeExchange(auth -> auth
//                        .pathMatchers("/auth/**", "/public/**").permitAll() // 인증 없이 통과
//                        .anyExchange().authenticated() // 나머지는 인증 필요
//                )
//                .addFilterAt(jwtAuthFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
//                .build();
//    }
//
//    public AuthenticationWebFilter jwtAuthFilter() {
//        JwtReactiveAuthenticationManager authManager = new JwtReactiveAuthenticationManager();
//        AuthenticationWebFilter filter = new AuthenticationWebFilter(authManager);
//        filter.setServerAuthenticationConverter(new JwtAuthenticationConverter());
//        return filter;
//    }
//}
