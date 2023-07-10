package com.swifties.bahceden.Bahceden.service;

import com.swifties.bahceden.Bahceden.DTO.CommentDTO;
import com.swifties.bahceden.Bahceden.entity.Comment;

import java.util.List;
import java.util.Set;

public interface CommentService {
    List<Comment> findAll();

    Comment findById(int theId);

    Comment findParentCommentByCommentId(int commentId);

    Comment save(Comment theComment);

    void deleteById(int theId);

    List<CommentDTO> findCommentsByProductId(int productId);

    Comment putLikeCount(int commentId, int likeCount);
}
