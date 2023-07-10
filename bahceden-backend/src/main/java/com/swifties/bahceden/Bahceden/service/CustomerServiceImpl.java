package com.swifties.bahceden.Bahceden.service;


import com.swifties.bahceden.Bahceden.entity.*;
import com.swifties.bahceden.Bahceden.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService{

    private CustomerRepository customerRepository;
    private FavoriteProductRepository favoriteProductRepository;
    private FavoriteProducerRepository favoriteProducerRepository;
    private LikedCommentRepository likedCommentRepository;
    private CommentRepository commentRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, FavoriteProductRepository favoriteProductRepository, FavoriteProducerRepository favoriteProducerRepository, LikedCommentRepository likedCommentRepository, CommentRepository commentRepository) {
        this.customerRepository = customerRepository;
        this.favoriteProductRepository = favoriteProductRepository;
        this.favoriteProducerRepository = favoriteProducerRepository;
        this.likedCommentRepository = likedCommentRepository;
        this.commentRepository = commentRepository;
    }



    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer findById(int theId) {
        Optional<Customer> result = customerRepository.findById(theId);

        Customer theCustomer = null;

        if(result.isPresent()){
            theCustomer = result.get();
        }
        else{
            throw new RuntimeException("Did not find customer id - " + theId);
        }

        return theCustomer;
    }


    @Override
    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public Customer findCustomerByOrderId(int orderId) {
        return customerRepository.findCustomerByOrderId(orderId);
    }

   @Override
    public Customer findCustomerByCommentId(int commentId) {
        return customerRepository.findCustomerByCommentId(commentId);
    }

    @Override
    public Customer save(Customer theCustomer) {
        return customerRepository.save(theCustomer);
    }

    @Override
    public void deleteById(int theId) {
        customerRepository.deleteById(theId);
    }

    @Override
    public FavoriteProduct addFavoriteProduct(int customerId, int productId) {
        FavoriteProduct favoriteProduct = new FavoriteProduct(0 , customerId, productId);
        return favoriteProductRepository.save(favoriteProduct);
    }

    @Override
    public String removeFavoriteProduct(int customerId, int productId) {
        FavoriteProduct favoriteProduct = favoriteProductRepository.findFavoriteProductByIds(customerId, productId);

        if (favoriteProduct == null)
            return "Favorite Product DNE";
        else {
            favoriteProductRepository.delete(favoriteProduct);
            return "Successful Delete";
        }
    }

    @Override
    public FavoriteProducer addFavoriteProducer(int customerId, int producerId) {
        FavoriteProducer favoriteProducer = new FavoriteProducer(0, customerId, producerId);
        return favoriteProducerRepository.save(favoriteProducer);
    }

    @Override
    public String removeFavoriteProducer(int customerId, int producerId) {
        FavoriteProducer favoriteProducer = favoriteProducerRepository.findFavoriteProducerByIds(customerId, producerId);

        if (favoriteProducer == null)
            return "Favorite Producer DNE";
        else {
            favoriteProducerRepository.delete(favoriteProducer);
            return "Successful Deletion";
        }
    }

    @Override
    public void addLike(int customerId, int commentId) {
        if (likedCommentRepository.findLikedCommentByIds(customerId, commentId) != null)
            return;

        likedCommentRepository.save(new LikedComment(customerId, commentId));

        int newCountOfLikes = likedCommentRepository.findLikedCommentsById(commentId).size();

        Optional<Comment> opt =  commentRepository.findById(commentId);

        if (opt.isPresent()) {
            Comment comment = opt.get();
            comment.setCountOfLikes(newCountOfLikes);
            commentRepository.save(comment);
        } else {
            throw new RuntimeException("Comment DNE");
        }
    }

    @Override
    public void removeLike(int customerId, int commentId) {
        LikedComment likedComment = likedCommentRepository.findLikedCommentByIds(customerId, commentId);
        if ( likedComment == null)
            return;

        likedCommentRepository.delete(likedComment);

        int newCountOfLikes = likedCommentRepository.findLikedCommentsById(commentId).size();

        Optional<Comment> opt =  commentRepository.findById(commentId);

        if (opt.isPresent()) {
            Comment comment = opt.get();
            comment.setCountOfLikes(newCountOfLikes);
            commentRepository.save(comment);
        } else {
            throw new RuntimeException("Comment DNE");
        }
    }

    @Override
    public boolean getLike(int customerId, int commentId) {
        return likedCommentRepository.findLikedCommentByIds(customerId, commentId) != null;
    }
}
