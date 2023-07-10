package com.swifties.bahceden.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.swifties.bahceden.R;
import com.swifties.bahceden.models.Comment;

import java.util.ArrayList;

public class CommentProducerViewAdapter extends RecyclerView.Adapter<CommentProducerViewAdapter.ViewHolder> {

    ArrayList<Comment> comments;
    Context context;

    public CommentProducerViewAdapter(ArrayList<Comment> comments, Context context) {
        this.comments = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_comment_producer_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.replyButton.setOnClickListener(v -> {
            holder.replyEdit.setVisibility(View.VISIBLE);
            holder.replyButton.setVisibility(View.GONE);
            Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.pop);
            holder.replyEdit.startAnimation(animation);
            holder.likeCount.setVisibility(View.GONE);
        });

        holder.replyEdit.setOnEditorActionListener((v, actionId, event) -> {
            holder.replyEdit.setVisibility(View.GONE);
            holder.replyButton.setVisibility(View.VISIBLE);
            holder.likeCount.setVisibility(View.VISIBLE);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return 31;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        EditText replyEdit;
        ImageView replyButton;
        TextView likeCount;
        public ViewHolder(View view) {
            super(view);

            replyEdit = view.findViewById(R.id.commentReplyEditText);
            replyButton = view.findViewById(R.id.commentReplyButton);
            likeCount = view.findViewById(R.id.likeCount);

            view.setOnClickListener(v ->
                    Toast.makeText(view.getContext(), "BaS:" + getBindingAdapterPosition(), Toast.LENGTH_SHORT).show());
        }
    }
}
