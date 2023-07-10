package com.swifties.bahceden.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.swifties.bahceden.R;
import com.swifties.bahceden.models.Cart;
import com.swifties.bahceden.models.Order;
import com.swifties.bahceden.models.Product;

import java.util.List;

public class CheckOutAdapter extends RecyclerView.Adapter<CheckOutAdapter.ViewHolder> {

    List<Order> cart;
    Context context;

    public CheckOutAdapter(List<Order> cart, Context context) {
        this.cart = cart;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_customer_check_out_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order checkoutItem = cart.get(position);
        Product product = checkoutItem.getProduct();
        Picasso.get()
                .load(product.getImageURL().replace("localhost", "10.0.2.2"))
                .into(holder.checkoutProductImage);
        holder.checkoutProductName.setText(product.getName());
        holder.checkoutProductAmount.setText(String.valueOf(checkoutItem.getAmount()));
        holder.checkoutProductPrice.setText(String.valueOf(checkoutItem.getTotalPrice()));
    }

    @Override
    public int getItemCount() {
        return cart.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView checkoutProductImage;
        TextView checkoutProductName, checkoutProductPrice, checkoutProductAmount;

        public ViewHolder(View view) {
            super(view);

            checkoutProductName = view.findViewById(R.id.checkoutProductName);
            checkoutProductImage = view.findViewById(R.id.checkoutProductImage);
            checkoutProductPrice = view.findViewById(R.id.checkoutProductPrice);
            checkoutProductAmount = view.findViewById(R.id.checkoutProductAmount);


        }
    }
}
