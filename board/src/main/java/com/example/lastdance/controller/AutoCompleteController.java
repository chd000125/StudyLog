package com.example.lastdance.controller;

import com.example.lastdance.service.PostAutoCompleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/autocomplete") // 🔄 더 명확하게 경로 변경
@RequiredArgsConstructor
public class AutoCompleteController {

    private final PostAutoCompleteService postAutoCompleteService;

    // 🔍 GET /api/autocomplete/title?prefix=스터
    @GetMapping("/title")
    public ResponseEntity<List<String>> suggest(@RequestParam String prefix) {
        return ResponseEntity.ok(postAutoCompleteService.autocompleteTitle(prefix));
    }
}
