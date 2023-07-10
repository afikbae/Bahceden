package com.swifties.bahceden.Bahceden.repository;

import com.swifties.bahceden.Bahceden.entity.LikedComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LikedCommentRepository extends JpaRepository<LikedComment, Integer> {

    @Query("SELECT lc FROM LikedComment lc WHERE lc.customerId = :customerId AND lc.commentId = :commentId")
    LikedComment findLikedCommentByIds(@Param("customerId") int customerId, @Param("commentId") int commentId);

    @Query("SELECT lc FROM LikedComment lc WHERE lc.commentId = :commentId")
    List<LikedComment> findLikedCommentsById(@Param("commentId") int commentId);
}
