package com.example.lastdance.repository;

import com.example.lastdance.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    // ✅ 정확한 필드명 일치 (소문자 uEmail, pId 사용)
    @Query("SELECT b FROM Bookmark b WHERE b.uEmail = :uEmail AND b.pId = :pId")
    List<Bookmark> findAllByUEmailAndPId(@Param("uEmail") String uEmail, @Param("pId") Long pId);

    @Query("SELECT b FROM Bookmark b WHERE b.uEmail = :uEmail")
    List<Bookmark> findAllByUEmail(@Param("uEmail") String uEmail);

    @Modifying
    @Query("DELETE FROM Bookmark b WHERE b.uEmail = :uEmail AND b.pId = :pId")
    void deleteAllByUEmailAndPId(@Param("uEmail") String uEmail, @Param("pId") Long pId);
}

