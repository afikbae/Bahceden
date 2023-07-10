package com.swifties.bahceden.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.swifties.bahceden.R;
import com.swifties.bahceden.activities.CustomerViewProducerActivity;
import com.swifties.bahceden.activities.CustomerViewProductActivity;
import com.swifties.bahceden.data.AuthUser;
import com.swifties.bahceden.databinding.LayoutCustomerFavoritesItemBinding;
import com.swifties.bahceden.databinding.LayoutProducerItemBinding;
import com.swifties.bahceden.models.Producer;
import com.swifties.bahceden.models.Product;

import java.util.List;

public class SearchResultsAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{



    boolean isProduct = false;
    private List<Product> products;
    private List<Producer> producers;
    Context context;
    LayoutInflater inflater;


    private SearchResultsAdapter() {
        // Private constructor
    }

    public static SearchResultsAdapter fromProductList(List<Product> list, Context context, LayoutInflater inflater) {
        SearchResultsAdapter instance = new SearchResultsAdapter();
        instance.products = list;
        instance.isProduct = true;
        instance.inflater = inflater;
        instance.context = context;
        return instance;
    }

    public static SearchResultsAdapter fromProducerList(List<Producer> list, Context context, LayoutInflater inflater) {
        SearchResultsAdapter instance = new SearchResultsAdapter();
        instance.producers = list;
        instance.inflater = inflater;
        instance.context = context;
        return instance;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(!isProduct){
            LayoutProducerItemBinding binding = LayoutProducerItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ProducerViewHolder(binding);
        } else{
            LayoutCustomerFavoritesItemBinding binding = LayoutCustomerFavoritesItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ProductViewHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof ProductViewHolder) {
            ProductViewHolder holder = (ProductViewHolder) viewHolder;
            Product product = products.get(position);
            Picasso.get()
                    .load(product.getImageURL().replace("localhost", "10.0.2.2"))
                    .into(holder.binding.cartProductImage);
//            holder.binding.city.setText(product.getProducer().getCity());
            holder.binding.price.setText(String.format(context.getString(R.string.turkish_lira), String.valueOf(product.getPricePerUnit())));
            holder.binding.favoritesProductName.setText(product.getName());
            holder.binding.rating.setText(String.valueOf(product.getRating()));
            if (AuthUser.getCustomer().getFavoriteProducts().contains(product))
            {
                holder.binding.favButton.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite));
            }
            else
            {
                holder.binding.favButton.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_unfavorite));
            }

            holder.binding.favButton.setOnClickListener(v -> {
                if (AuthUser.getCustomer().removeFavProduct(product))
                {
                    holder.binding.favButton.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_unfavorite));
                }
                else
                {
                    AuthUser.getCustomer().addNewFavProduct(product);
                    holder.binding.favButton.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite));
                }
            });
            holder.binding.getRoot().setOnClickListener(v -> {
                Intent intent = new Intent(context, CustomerViewProductActivity.class);
                intent.putExtra("product_id", product.getId());
                context.startActivity(intent);
            });
        }
        else {
            ProducerViewHolder holder = (ProducerViewHolder) viewHolder;
            Producer producer = producers.get(position);
            Picasso.get()
                    .load(producer.getBackgroundImageURL().replace("localhost", "10.0.2.2"))
                    .into(holder.binding.producerBgImage);
            holder.binding.layoutProducerItemName.setText(producer.getName());
            holder.binding.rating.setText(String.valueOf(producer.getRating()));
            if (AuthUser.getCustomer().getFavoriteProducers().contains(producer))
            {
                holder.binding.itemLayoutItemLiked.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite));
            }
            else
            {
                holder.binding.itemLayoutItemLiked.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_unfavorite));
            }




            holder.binding.itemLayoutItemLiked.setOnClickListener(v -> {
                if (AuthUser.getCustomer().removeFavProducer(producer))
                {
                    holder.binding.itemLayoutItemLiked.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_unfavorite));
                    notifyItemRemoved(position);
                }
                else
                {
                    AuthUser.getCustomer().addNewFavProducer(producer);
                    holder.binding.itemLayoutItemLiked.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite));
                }
            });
            holder.binding.getRoot().setOnClickListener(v -> {
                Intent intent = new Intent(context, CustomerViewProducerActivity.class);
                intent.putExtra("producer_id", producer.getId());
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        if(products != null) return products.size();
        return producers.size();
    }


    public static class ProducerViewHolder extends RecyclerView.ViewHolder {
        private final LayoutProducerItemBinding binding;

        public ProducerViewHolder(LayoutProducerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        private final LayoutCustomerFavoritesItemBinding binding;

        public ProductViewHolder(LayoutCustomerFavoritesItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
