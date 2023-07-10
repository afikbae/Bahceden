package com.swifties.bahceden.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.swifties.bahceden.R;
import com.swifties.bahceden.adapters.CommentCustomerViewAdapter;
import com.swifties.bahceden.adapters.ProductListingAdapter;
import com.swifties.bahceden.data.AuthUser;
import com.swifties.bahceden.data.RetrofitService;
import com.swifties.bahceden.data.apis.CommentApi;
import com.swifties.bahceden.data.apis.ProductApi;
import com.swifties.bahceden.data.serializers.CommentSerializer;
import com.swifties.bahceden.databinding.ActivityCustomerViewProductBinding;
import com.swifties.bahceden.models.Comment;
import com.swifties.bahceden.models.Order;
import com.swifties.bahceden.models.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerViewProductActivity extends AppCompatActivity {

    Intent intent;
    Product product;
    int productID;
    int productCount = 1;
    ActivityCustomerViewProductBinding binding;
    List<Product> similarItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerViewProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LinearLayout starsLayout = binding.stars;
        int totalStars = starsLayout.getChildCount();

        for (int i = 0; i < totalStars; i++) {
            final int starIndex = i;
            View star = starsLayout.getChildAt(i);
            star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setRating(starIndex + 1);
                }
            });
        }

        // Setting up productApi

        // Getting product from the backend
        intent = getIntent();
        productID = intent.getIntExtra("product_id", 0);

        RetrofitService.getApi(ProductApi.class).getProductByIdWithDetailedComments(productID).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                // Getting product & finding appropriate fields
                product = response.body();

                Optional<Order> orderOptional = AuthUser.getCustomer().getOrders()
                        .stream()
                        .filter(o -> o.getStatus() == Order.OrderStatus.IN_CART)
                        .filter(o -> o.getProduct().getId() == productID).findFirst();
                Optional<Product> favProductOptional = AuthUser.getCustomer().getFavoriteProducts()
                        .stream().filter(p -> p.getId() == productID)
                        .findFirst();

                if (orderOptional.isPresent())
                {
                    productCount = orderOptional.get().getAmount();
                }
                if (favProductOptional.isPresent())
                {
                    binding.favButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite));
                }

                // Setting appropriate fields to the product's information
                setViews();
            }

            @Override
            public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
                Toast.makeText(CustomerViewProductActivity.this, "Didn't work for some reason", Toast.LENGTH_SHORT).show();
                Log.d("debug_purposes", t.getMessage());
            }
        });

        RetrofitService.getApi(ProductApi.class).getSimilarProducts(productID).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                similarItems = response.body();
                setViews();
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }
    boolean calledOnce = true;
    private void setViews() {
        if (calledOnce)
        {
            calledOnce = false;
            return;
        }
        binding.customerViewProductItemName.setText(product.getName());
        binding.customerViewProductDescriptionText.setText(product.getDescription());
        binding.customerViewProductRatingText.setText(String.valueOf(product.getRating()));
        binding.producerName.setText(product.getProducer().getName());
        binding.producerName.setOnClickListener(v -> {
            Intent intent = new Intent(this, CustomerViewProducerActivity.class);
            intent.putExtra("producer_id", product.getProducer().getId());
            this.startActivity(intent);
        });
        binding.productCount.setText(String.valueOf(productCount));
        Picasso.get().load(product.getImageURL().replace("localhost", "10.0.2.2")).into(binding.productImage);
        binding.favButton.setOnClickListener(v -> {
            if (AuthUser.getCustomer().removeFavProduct(product)) {
                binding.favButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_unfavorite));
            } else {
                AuthUser.getCustomer().addNewFavProduct(product);
                binding.favButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite));
            }
        });
        binding.decrement.setOnClickListener(v -> {
            if (productCount > 0)
                productCount--;
            binding.productCount.setText(String.valueOf(productCount));
            binding.totalPrice.setText(String.format(getString(R.string.turkish_lira), String.valueOf(product.getPricePerUnit() * productCount)));
        });
        binding.increment.setOnClickListener(v -> {
            productCount++;
            binding.productCount.setText(String.valueOf(productCount));
            binding.totalPrice.setText(String.format(getString(R.string.turkish_lira), String.valueOf(product.getPricePerUnit() * productCount)));
        });
        binding.addToCart.setOnClickListener(v -> {
            AuthUser.getCustomer().addNewOrder(product, productCount);
            CustomerViewProductActivity.super.onBackPressed();
        });
        binding.customerViewProductBackButton.setOnClickListener(v -> CustomerViewProductActivity.super.onBackPressed());
        binding.totalPrice.setText(String.format(getString(R.string.turkish_lira), String.valueOf(product.getPricePerUnit() * productCount)));


        binding.commentItems.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.commentItems.setAdapter(new CommentCustomerViewAdapter(product.getComments().stream().filter(c -> c.getParent() == null).collect(Collectors.toList()), getLayoutInflater(), this));
        binding.itemSimilarItems.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.itemSimilarItems.setAdapter(new ProductListingAdapter(similarItems, this, getLayoutInflater()));
        binding.newCommentButton.setOnClickListener(v -> {
            binding.newCommentLayout.setVisibility(binding.newCommentLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            if (binding.newCommentLayout.getVisibility() == View.GONE)
                return;
            Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.pop);
            binding.newCommentLayout.startAnimation(animation);
        });
        binding.submitComment.setOnClickListener(v -> {
            String message = binding.messageEditText.getText().toString();
            Comment c = new Comment();
            c.setMessage(message);
            c.setAuthor(AuthUser.getCustomer());
            c.setProduct(product);
            c.setCountOfLikes(0);
            c.setRatingGiven(ratingGiven);
            RetrofitService.getApi(CommentApi.class).saveComment(c).enqueue(new Callback<Comment>() {
                @Override
                public void onResponse(Call<Comment> call, Response<Comment> response) {
                    c.setId(response.body().getId());
                    product.getComments().add(c);
                    ((CommentCustomerViewAdapter)binding.commentItems.getAdapter()).getComments().add(c);
                    binding.commentItems.getAdapter().notifyItemInserted(binding.commentItems.getAdapter().getItemCount());
                    binding.messageEditText.setText("");
                    RetrofitService.getApi(ProductApi.class).getProductByIdWithDetailedComments(productID).enqueue(new Callback<Product>() {
                        @Override
                        public void onResponse(Call<Product> call, Response<Product> response) {
                            try {
                                binding.customerViewProductRatingText.setText(response.body().getRating() + "");
                            } catch (Exception ignored) {};
                        }

                        @Override
                        public void onFailure(Call<Product> call, Throwable t) {

                        }
                    });
                    }

                @Override
                public void onFailure(Call<Comment> call, Throwable t) {
                    throw new RuntimeException(t);
                }
            });

            binding.newCommentLayout.setVisibility(View.GONE);
        });
    }

    int ratingGiven = 0;
    private void setRating(int rating) {
        ratingGiven = rating;
        LinearLayout starsLayout = binding.stars;
        int totalStars = starsLayout.getChildCount();

        for (int i = 0; i < totalStars; i++) {
            AppCompatImageButton star = (AppCompatImageButton) starsLayout.getChildAt(i);
            if (i < rating) {
                star.setImageDrawable(getDrawable(R.drawable.ic_star));
            } else {
                star.setImageDrawable(getDrawable(R.drawable.ic_empty_star));
            }
        }
    }
}