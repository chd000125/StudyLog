package com.example.lastdance.service;

import com.example.lastdance.entity.Bookmark;
import com.example.lastdance.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    public boolean isBookmarked(String uEmail, Long postId) {
        return !bookmarkRepository.findAllByUEmailAndPId(uEmail, postId).isEmpty();
    }

    public void importBookmark(String uEmail, Long postId) {
        // 북마크 추가
        Bookmark bookmark = Bookmark.builder()
                .uEmail(uEmail)
                .pId(postId)
                .build();
        bookmarkRepository.save(bookmark);
    }

    @Transactional // ✅ 여기 추가!
    public void deleteBookmark(String uEmail, Long postId) {
        // 북마크 모두 삭제
        bookmarkRepository.deleteAllByUEmailAndPId(uEmail, postId);
    }

    public List<Bookmark> getUserBookmarks(String uEmail) {
        return bookmarkRepository.findAllByUEmail(uEmail);
    }
}
