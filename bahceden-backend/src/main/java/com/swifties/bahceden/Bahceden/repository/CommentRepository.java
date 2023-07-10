package com.swifties.bahceden.Bahceden.repository;

import com.swifties.bahceden.Bahceden.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("SELECT c.parent FROM Comment c WHERE c.id = :commentId AND c.parent IS NOT NULL")
    Comment findParentCommentByCommentId(@Param("commentId") int commentId);

    @Query("SELECT c FROM Comment c WHERE c.product = :productId")
    List<Comment> findCommentsByProductId(@Param("productId") int productId);

    @Query("SELECT c FROM Comment c JOIN Product p ON c.product = p.id WHERE p.producer.id = :producerId AND c.author > 0")
    List<Comment> findCommentForProducer(@Param("producerId") int producerId);

    @Query("SELECT c FROM Comment c WHERE c.parent = :commentId")
    List<Comment> findChildrenOfComment(@Param("commentId") int commentId);
}
