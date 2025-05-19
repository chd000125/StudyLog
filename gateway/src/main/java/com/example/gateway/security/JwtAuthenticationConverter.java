//package com.example.gateway.security;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.JwtException;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpHeaders;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//public class JwtAuthenticationConverter implements ServerAuthenticationConverter {
//
//    private String secret = "dGhpc19pc19hX3Zlcnlfc2VjdXJlX3Rlc3Rfc2VjcmV0X2tleQ==";
//
//    @Override
//    public Mono<Authentication> convert(ServerWebExchange exchange) {
//        String token = resolveToken(exchange);
//        if (token == null) return Mono.empty();
//
//        try {
//            Claims claims = Jwts.parser()
//                    .setSigningKey(secret)
//                    .parseClaimsJws(token)
//                    .getBody();
//
//            String email = claims.getSubject();
//            return Mono.just(new UsernamePasswordAuthenticationToken(email, null, null));
//        } catch (JwtException e) {
//            return Mono.error(new BadCredentialsException("Invalid JWT"));
//        }
//    }
//
//    private String resolveToken(ServerWebExchange exchange) {
//        String bearer = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//        if (bearer != null && bearer.startsWith("Bearer ")) {
//            return bearer.substring(7);
//        }
//        return null;
//    }
//}
