package com.swifties.bahceden.Bahceden.service;

import com.swifties.bahceden.Bahceden.DTO.CommentDTO;
import com.swifties.bahceden.Bahceden.DTO.CustomerMainInfoDTO;
import com.swifties.bahceden.Bahceden.entity.Comment;
import com.swifties.bahceden.Bahceden.repository.CommentRepository;
import com.swifties.bahceden.Bahceden.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService{
    private CommentRepository commentRepository;
    private CustomerService customerService;
    private ModelMapper modelMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository theCommentRepository, CustomerService customerService, ModelMapper modelMapper){
        commentRepository = theCommentRepository;
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public Comment findById(int theId) {
        Optional<Comment> result = commentRepository.findById(theId);

        Comment theComment = null;

        if(result.isPresent()){
            theComment = result.get();
        }
        else{
            throw new RuntimeException("Did not find comment id - " + theId);
        }

        return theComment;
    }

    @Override
    public Comment findParentCommentByCommentId(int commentId) {
       return commentRepository.findParentCommentByCommentId(commentId);
   }

    @Override
    public Comment save(Comment theComment) {
        return commentRepository.save(theComment);
    }

    @Override
    public void deleteById(int theId) {
        commentRepository.deleteById(theId);
    }

    @Override
    public List<CommentDTO> findCommentsByProductId(int productId) {
        List<CommentDTO> res =  commentRepository.findCommentsByProductId(productId).stream().map(
                comment -> {
                    CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);

                    if (comment.getAuthor() == -1)
                        commentDTO.setAuthor(null);
                    else
                        commentDTO.setAuthor(modelMapper.map(customerService.findById(comment.getAuthor()), CustomerMainInfoDTO.class));
                    return commentDTO;
                }).collect(Collectors.toList());

        return res;
    }

    @Override
    public Comment putLikeCount(int commentId, int likeCount) {
        Optional<Comment> comment = commentRepository.findById(commentId);

        if (comment.isPresent())
        {
            comment.get().setCountOfLikes(likeCount);
            return commentRepository.save(comment.get());
        }

        return null;
    }
}
