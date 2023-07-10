package com.swifties.bahceden.data.apis;

import com.swifties.bahceden.models.Comment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CommentApi {

    /**
     * Retrieves all the comments in the database
     *
     * @return a list of all the comments in the database
     * @apiNote probably will be deleted further in development
     */
    @GET("comments")
    Call<List<Comment>> getAllComments();

    /**
     * Retrieves the comment with the given id (if it exists)
     * from the backend
     *
     * @param commentId the id of the comment
     * @return the comment with the given id
     */
    @GET("comments/{commentId}")
    Call<Comment> getCommentById(@Path("commentId") int commentId);

    /**
     * Saves a comment to the database
     *
     * @param comment the comment to be saved
     * @return the comment that was saved
     */
    @POST("comments")
    Call<Comment> saveComment(@Body Comment comment);

    @DELETE("comments/{commentId}")
    Call<Comment> deleteCommentById(@Path("commentId") int id);

    @PUT("comments/{commentId}")
    Call<Comment> putLikeCount(@Path("commentId") int id, @Query("likes") int likeCount);

    @GET("producers/{producerId}/comments")
    Call<List<Comment>> getProducersComments(@Path("producerId") int id);

    @POST("customers/likes")
    Call<Void> addLike(@Query("customerId") int customerId, @Query("commentId") int commentId);

    @DELETE("customers/likes")
    Call<Void> removeLike(@Query("customerId") int customerId, @Query("commentId") int commentId);

    @GET("customers/likes")
    Call<Boolean> getLike(@Query("customerId") int customerId, @Query("commentId") int commentId);
}
