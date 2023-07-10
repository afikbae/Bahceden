package com.swifties.bahceden.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.swifties.bahceden.activities.CustomerViewProducerActivity;
import com.swifties.bahceden.data.AuthUser;
import com.swifties.bahceden.databinding.LayoutProducerItemBinding;
import com.swifties.bahceden.models.Producer;

import java.util.List;

public class FavDukkanAdapter extends RecyclerView.Adapter<FavDukkanAdapter.ViewHolder> {

    List<Producer> producers;
    Context context;
    LayoutInflater inflater;

    public FavDukkanAdapter(List<Producer> producers, Context context, LayoutInflater inflater) {
        this.producers = producers;
        this.context = context;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public FavDukkanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutProducerItemBinding binding = LayoutProducerItemBinding.inflate(inflater, viewGroup, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavDukkanAdapter.ViewHolder holder, int position) {
        Producer producer = producers.get(position);
        Picasso.get().load(producer.getBackgroundImageURL().replace("localhost", "10.0.2.2")).into(holder.binding.producerBgImage);
        holder.binding.layoutProducerItemName.setText(producer.getName());
        holder.binding.rating.setText(String.valueOf(producer.getRating()));
        holder.binding.itemLayoutItemLiked.setOnClickListener(v -> {
            if (AuthUser.getCustomer().removeFavProducer(producer))
            {
                notifyItemRemoved(position);
            }
        });
        holder.binding.getRoot().setOnClickListener(v -> {
            Intent intent = new Intent(context, CustomerViewProducerActivity.class);
            intent.putExtra("producer_id", producer.getId());
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return producers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        LayoutProducerItemBinding binding;
        public ViewHolder(LayoutProducerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
