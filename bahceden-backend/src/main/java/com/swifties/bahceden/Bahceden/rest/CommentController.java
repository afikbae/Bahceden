package com.swifties.bahceden.Bahceden.rest;

import com.swifties.bahceden.Bahceden.entity.Category;
import com.swifties.bahceden.Bahceden.entity.Comment;
import com.swifties.bahceden.Bahceden.entity.Product;
import com.swifties.bahceden.Bahceden.service.CommentService;
import com.swifties.bahceden.Bahceden.service.ProducerService;
import com.swifties.bahceden.Bahceden.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {
    private CommentService commentService;
    private ProductService productService;

    @Autowired
    private CommentController(CommentService theCommentService, ProductService theProductService){
        commentService = theCommentService;
        productService = theProductService;
    }

    @GetMapping("/comments")
    public List<Comment> findAll(){
        return commentService.findAll();
    }

    @GetMapping("/comments/{commentId}")
    public Comment getComment(@PathVariable int commentId){

        Comment theComment = commentService.findById(commentId);

        if(theComment == null){
            throw new RuntimeException("Comment id did not found - " + commentId);
        }

        return theComment;
    }

    @PostMapping("/comments")
    public Comment addComment(@RequestBody Comment theComment){

        // also just in case they pass an id in JSON ... set id to 0
        // this is to force a save of new item ... instead of update

        theComment.setId(0);
        Comment dbComment = commentService.save(theComment);
        double newRating;

        if(theComment.getAuthor() != -1){
            Product product = productService.findById(theComment.getProduct());
            int size = commentService.findCommentsByProductId(theComment.getProduct()).size();
            if(size == 1){
                if(product.getRating() == null){
                    product.setRating(0.0);
                }
                newRating = theComment.getRating();
            }
            else{
                if(product.getRating() == null){
                    product.setRating(0.0);
                }
                newRating = (product.getRating() * (size - 1) + theComment.getRating()) / size;

            }
            product.setRating(newRating);
            productService.save(product);
        }
        return dbComment;
    }

    @PutMapping("/comments")
    public Comment updateComment(@RequestBody Comment theComment){
        Comment dbComment = commentService.save(theComment);
        return dbComment;
    }

    @DeleteMapping("/comments/{commentId}")
    public String deleteComment(@PathVariable int commentId){

        Comment tempComment = commentService.findById(commentId);

        // throw exception if null

        if(tempComment == null){
            throw new RuntimeException("Comment id not found - " + commentId);
        }

        commentService.deleteById(commentId);

        return "{\"id\":"+commentId+"}";
    }

    @GetMapping("/comments/{commentId}/comment")
    public Comment getParentCommentByCommentId(@PathVariable int commentId) {
        Comment comment = commentService.findParentCommentByCommentId(commentId);

        if (comment == null) {
            throw new RuntimeException("No comment found for parent ID - " + commentId);
        }

        return comment;
    }
}
