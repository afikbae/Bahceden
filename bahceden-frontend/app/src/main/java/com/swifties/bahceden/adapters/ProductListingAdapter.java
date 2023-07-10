package com.swifties.bahceden.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.swifties.bahceden.R;
import com.swifties.bahceden.activities.CustomerViewProductActivity;
import com.swifties.bahceden.data.AuthUser;
import com.swifties.bahceden.databinding.LayoutItemBinding;

import com.swifties.bahceden.models.Product;

import java.util.List;

public class ProductListingAdapter extends RecyclerView.Adapter<ProductListingAdapter.ViewHolder> {

    List<Product> products;
    Context context;
    LayoutInflater inflater;
    public ProductListingAdapter(List<Product> products, Context context, LayoutInflater inflater) {
        this.products = products;
        this.context = context;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutItemBinding binding = LayoutItemBinding.inflate(inflater, viewGroup, false);
        return new ProductListingAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(holder.getBindingAdapterPosition());
        holder.binding.itemLayoutItemName.setText(product.getName());
        holder.binding.itemLayoutPriceText.setText(String.format(context.getString(R.string.turkish_lira), String.valueOf(product.getPricePerUnit())));
        holder.binding.itemLayoutProducerNameText.setText(product.getProducer().getName());
        if (AuthUser.getCustomer().getFavoriteProducts().contains(product))
        {
            holder.binding.itemLayoutItemLiked.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite));
        }
        else
        {
            holder.binding.itemLayoutItemLiked.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_unfavorite));

        }
        Picasso.get()
            .load(product.getImageURL().replace("localhost", "10.0.2.2"))
                .into(holder.binding.itemLayoutItemImage);
        holder.binding.itemLayoutItemLiked.setOnClickListener(v -> {
            if (AuthUser.getCustomer().removeFavProduct(product))
            {
                holder.binding.itemLayoutItemLiked.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_unfavorite));
            }
            else
            {
                AuthUser.getCustomer().addNewFavProduct(product);
                holder.binding.itemLayoutItemLiked.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite));

            }
        });

        holder.binding.itemLayoutAddToCart.setOnClickListener(v -> {
            AuthUser.getCustomer().addNewOrder(product, 1);
        });

        holder.binding.getRoot().setOnClickListener(v ->
        {
            Intent intent = new Intent(context, CustomerViewProductActivity.class);
            intent.putExtra("product_id", product.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return products == null ? 0 : products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        LayoutItemBinding binding;

        public ViewHolder(LayoutItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
