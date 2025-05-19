package com.example.lastdance.controller;

import com.example.lastdance.entity.Bookmark;
import com.example.lastdance.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    // 상태 확인 API (별 켜졌는지 여부)
    @GetMapping("/status")
    public ResponseEntity<Boolean> isBookmarked(
            @RequestParam String uEmail,
            @RequestParam Long postId
    ) {
        boolean result = bookmarkService.isBookmarked(uEmail, postId);
        return ResponseEntity.ok(result);
    }

    // 북마크 추가 (import)
    @PostMapping("/import")
    public ResponseEntity<Void> importBookmark(
            @RequestParam String uEmail,
            @RequestParam Long postId
    ) {
        System.out.println("🔥 uEmail: " + uEmail);
        System.out.println("🔥 postId: " + postId);
        bookmarkService.importBookmark(uEmail, postId);
        return ResponseEntity.ok().build();
    }

    // 북마크 삭제 (delete)
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteBookmark(
            @RequestParam String uEmail,
            @RequestParam Long postId
    ) {
        bookmarkService.deleteBookmark(uEmail, postId);
        return ResponseEntity.ok().build();
    }

    // 유저의 전체 북마크 목록
    @GetMapping
    public ResponseEntity<List<Bookmark>> getUserBookmarks(@RequestParam String uEmail) {
        return ResponseEntity.ok(bookmarkService.getUserBookmarks(uEmail));
    }
}