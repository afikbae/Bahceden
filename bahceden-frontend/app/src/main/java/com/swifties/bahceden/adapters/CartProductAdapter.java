package com.swifties.bahceden.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.swifties.bahceden.R;
import com.swifties.bahceden.activities.CustomerViewProductActivity;
import com.swifties.bahceden.data.AuthUser;
import com.swifties.bahceden.data.RetrofitService;
import com.swifties.bahceden.data.apis.OrderApi;
import com.swifties.bahceden.models.Order;
import com.swifties.bahceden.models.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.ViewHolder> {

    List<Order> cart;
    Context context;
    TextView totalPriceText;

    public CartProductAdapter(List<Order> cart, Context context, TextView totalPriceText) {
        this.cart = cart;
        this.context = context;
        this.totalPriceText = totalPriceText;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_customer_cart_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order cartItem = cart.get(position);
        Product product = cartItem.getProduct();
        Picasso.get()
                .load(product.getImageURL().replace("localhost", "10.0.2.2"))
                .into(holder.cartProductImage);
        holder.cartProductName.setText(product.getName());
        holder.cartProductPrice.setText(String.format(context.getString(R.string.turkish_lira), String.valueOf(cartItem.getTotalPrice())));
        holder.cartProductAmount.setText(String.valueOf(cartItem.getAmount()));

        holder.cartProductDecrement.setOnClickListener(v -> {
            if (!cartItem.offsetAmountBy(-1))
            {
                cart.remove(holder.getBindingAdapterPosition());
                AuthUser.getCustomer().deleteFromCart(cartItem.getId());
                notifyItemRemoved(holder.getBindingAdapterPosition());
                RetrofitService.getApi(OrderApi.class).deleteById(cartItem.getId()).enqueue(new Callback<Order>() {
                    @Override
                    public void onResponse(Call<Order> call, Response<Order> response) {

                    }

                    @Override
                    public void onFailure(Call<Order> call, Throwable t) {
                        throw new RuntimeException(t);
                    }
                });
            }
            else
            {
                notifyItemChanged(holder.getBindingAdapterPosition());
            }
            totalPriceText.setText(String.format(context.getString(R.string.turkish_lira), String.valueOf(cart.stream().map(Order::getTotalPrice).reduce(0.0, Double::sum))));
        });

        holder.cartProductIncrement.setOnClickListener(v -> {
            cartItem.offsetAmountBy(1);
            totalPriceText.setText(String.format(context.getString(R.string.turkish_lira), String.valueOf(cart.stream().map(Order::getTotalPrice).reduce(0.0, Double::sum))));
            notifyItemChanged(holder.getBindingAdapterPosition());
        });

        holder.cartProductDelete.setOnClickListener(v -> {
            cart.remove(holder.getBindingAdapterPosition());
            AuthUser.getCustomer().deleteFromCart(cartItem.getId());
            totalPriceText.setText(String.format(context.getString(R.string.turkish_lira), String.valueOf(cart.stream().map(Order::getTotalPrice).reduce(0.0, Double::sum))));
            RetrofitService.getApi(OrderApi.class).deleteById(cartItem.getId()).enqueue(new Callback<Order>() {
                @Override
                public void onResponse(Call<Order> call, Response<Order> response) {
                    if (response == null)
                    {
                        throw new RuntimeException("order delete response is null");
                    }
                }

                @Override
                public void onFailure(Call<Order> call, Throwable t) {
                    throw new RuntimeException(t);
                }
            });
            notifyItemRemoved(holder.getBindingAdapterPosition());
        });

        // takes the user to the product page of the order
        holder.cartProductImage.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), CustomerViewProductActivity.class);
            intent.putExtra("product_id", product.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cart.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView cartProductImage, cartProductDelete;
        TextView cartProductName, cartProductPrice, cartProductAmount;
        AppCompatButton cartProductDecrement, cartProductIncrement;

        public ViewHolder(View view) {
            super(view);

            // product information
            cartProductImage = view.findViewById(R.id.cartProductImage);
            cartProductName = view.findViewById(R.id.cartProductName);
            cartProductPrice = view.findViewById(R.id.cartProductPrice);
            cartProductAmount = view.findViewById(R.id.cartProductAmount);

            // buttons
            cartProductDecrement = view.findViewById(R.id.cartProductDecrement);
            cartProductIncrement = view.findViewById(R.id.cartProductIncrement);
            cartProductDelete = view.findViewById(R.id.cartProductDelete);
        }
    }


}
