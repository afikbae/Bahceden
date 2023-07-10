package com.swifties.bahceden.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.swifties.bahceden.activities.ProducerOrderDetailsActivity;
import com.swifties.bahceden.R;
import com.swifties.bahceden.databinding.LayoutCustomerOrderBinding;
import com.swifties.bahceden.databinding.LayoutProducerOrdersItemBinding;
import com.swifties.bahceden.fragments.ProducerOrdersFragment;
import com.swifties.bahceden.models.Action;
import com.swifties.bahceden.models.Order;

import java.util.List;

public class ProducerOrderAdapter extends RecyclerView.Adapter<ProducerOrderAdapter.ViewHolder> {
    List<Order> orders;
    Context context;
    LayoutInflater inflater;
    ProducerOrdersFragment frag;

    public ProducerOrderAdapter(List<Order> orders, Context context, LayoutInflater inflater, ProducerOrdersFragment frag) {
        this.orders = orders;
        this.context = context;
        this.inflater = inflater;
        this.frag = frag;
    }

    @NonNull
    @Override
    public ProducerOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutProducerOrdersItemBinding binding = LayoutProducerOrdersItemBinding.inflate(inflater, parent, false);
        return new ProducerOrderAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProducerOrderAdapter.ViewHolder holder, int position) {
        Order order = orders.get(position);
        Picasso.get().load(order.getProduct().getImageURL().replace("localhost", "10.0.2.2"))
                .into(holder.binding.productImage);
        holder.binding.productName.setText(order.getProduct().getName());
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
        holder.binding.orderStatus.setText(order.getStatus().toString());
        holder.binding.orderStatus.setTextColor(color);
        holder.binding.changeStatusButton.setOnClickListener(v -> {
            holder.binding.changeStatusButtonsHolder.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.pop);
            holder.binding.changeStatusButtonsHolder.startAnimation(animation);
        });

        holder.binding.pendingButton.setOnClickListener(new StatusChangeListener(Order.OrderStatus.PENDING, order));
        holder.binding.ongoingButton.setOnClickListener(new StatusChangeListener(Order.OrderStatus.ONGOING, order));
        holder.binding.deliveredButton.setOnClickListener(new StatusChangeListener(Order.OrderStatus.DELIVERED, order));
        holder.binding.cancelledButton.setOnClickListener(new StatusChangeListener(Order.OrderStatus.CANCELLED, order));

        holder.binding.detailsButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ProducerOrderDetailsActivity.class);
            intent.putExtra("order_id", order.getId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if(orders == null) return 0;
        return orders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        LayoutProducerOrdersItemBinding binding;
        public ViewHolder(LayoutProducerOrdersItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private class StatusChangeListener implements View.OnClickListener
    {
        Order.OrderStatus status;
        Order order;

        public StatusChangeListener(Order.OrderStatus status, Order order) {
            this.status = status;
            this.order = order;
        }

        @Override
        public void onClick(View v) {
            if (order.getStatus() == status)
                return;
            new AlertDialog.Builder(context)
                    .setTitle("Confirmation")
                    .setMessage("Are you sure you want to change the status?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                        order.changeStatusTo(status, new Action() {
                            @Override
                            public void act() {
                                frag.setViews();
                            }
                        });

                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }
    }
}
