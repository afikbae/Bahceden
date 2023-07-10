package com.swifties.bahceden.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.swifties.bahceden.R;
import com.swifties.bahceden.data.AuthUser;
import com.swifties.bahceden.data.RetrofitService;
import com.swifties.bahceden.data.apis.CommentApi;
import com.swifties.bahceden.databinding.LayoutCommentCustomerViewBinding;
import com.swifties.bahceden.models.Comment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentCustomerViewAdapter extends RecyclerView.Adapter<CommentCustomerViewAdapter.ViewHolder> {
    List<Comment> comments;
    LayoutInflater inflater;
    Context context;
    public CommentCustomerViewAdapter(List<Comment> comments, LayoutInflater inflater, Context context) {
        this.comments = comments;
        this.inflater = inflater;
        this.context = context;
    }
    @NonNull
    @Override
    public CommentCustomerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentCustomerViewAdapter.ViewHolder(LayoutCommentCustomerViewBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentCustomerViewAdapter.ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        if (comment.getAuthor().getProfileImageURL() != null)
            Picasso.get().load(comment.getAuthor().getProfileImageURL().replace("localhost", "10.0.2.2")).into(holder.binding.commentImage);
        holder.binding.commentName.setText(comment.getAuthor().getName());
        holder.binding.commentMessage.setText(comment.getMessage());
        holder.binding.likeCount.setText(String.valueOf(comment.getCountOfLikes()));
        holder.binding.rating.setText(comment.getRatingGiven() + "");
        if (comment.getChildComment() == null)
        {
            holder.binding.commentReplyHolder.setVisibility(View.GONE);
        }
        else
        {
            holder.binding.commentReplyAuthor.setText(comment.getChildComment().getProduct().getProducer().getName());
            holder.binding.commentReply.setText(comment.getChildComment().getMessage());
        }
        if (comment.getAuthor().equals(AuthUser.getCustomer()))
        {
            holder.binding.commentDelete.setVisibility(View.VISIBLE);
            holder.binding.commentDelete.setOnClickListener(v -> {
                comments.remove(comment);
                notifyItemRemoved(position);
                RetrofitService.getApi(CommentApi.class).deleteCommentById(comment.getId()).enqueue(new Callback<Comment>() {
                    @Override
                    public void onResponse(Call<Comment> call, Response<Comment> response) {

                    }

                    @Override
                    public void onFailure(Call<Comment> call, Throwable t) {
                        throw new RuntimeException(t);
                    }
                });
            });
        }
        else
        {
            holder.binding.commentDelete.setVisibility(View.GONE);
        }
        RetrofitService.getApi(CommentApi.class).getLike(AuthUser.getCustomer().getId(), comment.getId()).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                if (Boolean.TRUE.equals(response.body())) {
                    holder.binding.likeButtonHeart.setImageDrawable(context.getDrawable(R.drawable.ic_favorite));
                    holder.isLiked = true;
                }

                holder.binding.likeButton.setOnClickListener(v -> {
                    if (holder.isLiked)
                    {
                        RetrofitService.getApi(CommentApi.class).removeLike(AuthUser.getCustomer().getId(), comment.getId()).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                holder.binding.likeButtonHeart.setImageDrawable(context.getDrawable(R.drawable.ic_unfavorite));
                                comment.setCountOfLikes(comment.getCountOfLikes()-1);
                                holder.binding.likeCount.setText(String.valueOf(comment.getCountOfLikes()));
                                holder.isLiked = false;
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
                    }
                    else
                    {
                        RetrofitService.getApi(CommentApi.class).addLike(AuthUser.getCustomer().getId(), comment.getId()).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                holder.binding.likeButtonHeart.setImageDrawable(context.getDrawable(R.drawable.ic_favorite));
                                comment.setCountOfLikes(comment.getCountOfLikes()+1);
                                holder.binding.likeCount.setText(String.valueOf(comment.getCountOfLikes()));
                                holder.isLiked = true;
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
                    }
                });
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public List<Comment> getComments() {
        return comments;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LayoutCommentCustomerViewBinding binding;
        boolean isLiked = false;
        public ViewHolder(LayoutCommentCustomerViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
