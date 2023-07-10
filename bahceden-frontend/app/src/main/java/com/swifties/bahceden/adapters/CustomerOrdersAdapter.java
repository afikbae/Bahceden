package com.swifties.bahceden.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.swifties.bahceden.R;
import com.swifties.bahceden.databinding.LayoutCustomerOrderBinding;
import com.swifties.bahceden.models.Order;

import java.util.List;

public class CustomerOrdersAdapter extends RecyclerView.Adapter<CustomerOrdersAdapter.ViewHolder> {

    List<Order> orders;
    Context context;
    LayoutInflater inflater;

    public CustomerOrdersAdapter(List<Order> orders, Context context, LayoutInflater inflater) {
        this.orders = orders;
        this.context = context;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public CustomerOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutCustomerOrderBinding binding = LayoutCustomerOrderBinding.inflate(inflater, viewGroup, false);
        return new CustomerOrdersAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orders.get(position);
        Picasso.get().load(order.getProduct().getImageURL().replace("localhost", "10.0.2.2")).into(holder.binding.productImage);
        holder.binding.orderNumber.setText(String.format(context.getResources().getText(R.string.orderWithId).toString(), order.getId()));
        holder.binding.orderDate.setText(order.getDateOfPurchase());
        holder.binding.totalAmount.setText(String.format(context.getResources().getText(R.string.totalCost).toString(), order.getTotalPrice()));
        int color = context.getColor(R.color.black);
        switch (order.getStatus())
        {
            case PENDING:
                color = context.getColor(R.color.orange);
                break;
            case ONGOING:
                color = context.getColor(R.color.eggplant_pink);
                break;
            case DELIVERED:
                color = context.getColor(R.color.plus_green);
                break;
            case CANCELLED:
                color = context.getColor(R.color.minus_red);
                break;
        }
        holder.binding.orderStatus.setText(String.format(context.getResources().getText(R.string.status).toString(), order.getStatus()));
        holder.binding.orderStatus.setTextColor(color);

        holder.binding.productName.setText(order.getProduct().getName());
        holder.binding.productPrice.setText(String.format(context.getResources().getText(R.string.turkish_lira).toString(), order.getProduct().getPricePerUnit()));
        holder.binding.quantity.setText(order.getAmount() + " " + String.valueOf(order.getProduct().getUnitType()));
        holder.binding.shippingAddress.setText(order.getDeliveryAddress().getFullAddress());
    }


    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        LayoutCustomerOrderBinding binding;
        public ViewHolder(LayoutCustomerOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
