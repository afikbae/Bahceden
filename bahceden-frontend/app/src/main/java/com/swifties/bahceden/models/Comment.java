package com.swifties.bahceden.models;

public class Comment {
    private User author;
    private Product product;
    private String message;
    private Comment parent;
    private int countOfLikes;
    private int id;
    private Comment childComment;
    private int ratingGiven;

    public int getRatingGiven() {
        return ratingGiven;
    }

    public void setRatingGiven(int ratingGiven) {
        this.ratingGiven = ratingGiven;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Comment getParent() {
        return parent;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
    }

    public int getCountOfLikes() {
        return countOfLikes;
    }

    public void setCountOfLikes(int countOfLikes) {
        this.countOfLikes = countOfLikes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Comment getChildComment() {
        return childComment;
    }

    public void setChildComment(Comment childComment) {
        this.childComment = childComment;
        childComment.setParent(this);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "author=" + author +
                ", message='" + message + '\'' +
                ", parent=" + parent +
                ", countOfLikes=" + countOfLikes +
                ", id=" + id +
                '}';
    }
}
