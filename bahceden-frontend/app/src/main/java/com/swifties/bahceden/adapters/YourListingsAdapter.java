package com.swifties.bahceden.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.swifties.bahceden.activities.ProducerEditProductActivity;
import com.swifties.bahceden.databinding.LayoutProducerHomeListingItemBinding;
import com.swifties.bahceden.models.Product;

import java.util.ArrayList;
import java.util.List;

public class YourListingsAdapter extends RecyclerView.Adapter<YourListingsAdapter.ProducerViewHolder> {
    List<Product> listings;
    Context context;

    public YourListingsAdapter(List<Product> listings, Context context) {
        this.listings = listings;
        this.context = context;
    }

    @NonNull
    @Override
    public ProducerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutProducerHomeListingItemBinding binding = LayoutProducerHomeListingItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ProducerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull YourListingsAdapter.ProducerViewHolder holder, int position) {
        Product product = listings.get(position);

        Picasso.get()
                .load(product.getImageURL().replace("localhost", "10.0.2.2"))
                .into(holder.binding.producerHomeListingsProductImage);
        holder.binding.homeListingsProductName.setText(product.getName());
        holder.binding.price.setText(String.valueOf(product.getPricePerUnit()));
        holder.binding.listingRemaining.setText(String.valueOf(product.getAmountInStock()));
        holder.binding.rating.setText(String.valueOf(product.getRating()));
        holder.binding.editListingButton.setOnClickListener(editView -> {
            Intent intent = new Intent(editView.getContext(), ProducerEditProductActivity.class);
            intent.putExtra("product_id", product.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    public static class ProducerViewHolder extends RecyclerView.ViewHolder {
        private final LayoutProducerHomeListingItemBinding binding;
        public ProducerViewHolder(LayoutProducerHomeListingItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
