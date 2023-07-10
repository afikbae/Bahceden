package com.swifties.bahceden.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.swifties.bahceden.adapters.CustomerOrdersAdapter;
import com.swifties.bahceden.data.AuthUser;
import com.swifties.bahceden.databinding.ActivityCustomerOrdersBinding;
import com.swifties.bahceden.models.Action;
import com.swifties.bahceden.models.Order;

import java.util.stream.Collectors;

public class CustomerOrdersActivity extends AppCompatActivity {
    ActivityCustomerOrdersBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AuthUser.getInstance().updateUser(new Action() {
            @Override
            public void act() {
                binding.customerOrdersBackButton.setOnClickListener(backView -> CustomerOrdersActivity.super.onBackPressed());

                RecyclerView ordersRV = binding.ordersProfileRV;
                ordersRV.setHasFixedSize(true);
                ordersRV.setLayoutManager(new LinearLayoutManager(CustomerOrdersActivity.this));
                ordersRV.setAdapter(new CustomerOrdersAdapter(AuthUser.getCustomer()
                        .getOrders()
                        .stream()
                        .filter(o -> o.getStatus() != Order.OrderStatus.IN_CART).collect(Collectors.toList()),
                        CustomerOrdersActivity.this, getLayoutInflater()));

            }
        });
    }
}
