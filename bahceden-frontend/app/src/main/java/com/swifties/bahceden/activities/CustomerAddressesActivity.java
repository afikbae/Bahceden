package com.swifties.bahceden.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.swifties.bahceden.adapters.YourAddressesAdapter;
import com.swifties.bahceden.data.AuthUser;
import com.swifties.bahceden.databinding.ActivityCustomerAddressesBinding;

public class CustomerAddressesActivity extends AppCompatActivity {

    ActivityCustomerAddressesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerAddressesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.customerAddressesYourAddressesRV.setLayoutManager(new LinearLayoutManager(this));
        binding.customerAddressesYourAddressesRV.setAdapter(new YourAddressesAdapter(AuthUser.getCustomer().getAddresses(),
                this,
                getLayoutInflater(), this));

        binding.customerAddressesAddAddressButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, CustomerAddAddressActivity.class);
            startActivity(intent);
        });

        binding.customerAddressesBackButton.setOnClickListener(v -> CustomerAddressesActivity.super.onBackPressed());
    }

    @Override
    protected void onResume() {
        super.onResume();
        reload();
    }

    public void reload()
    {
        binding.customerAddressesYourAddressesRV.setAdapter(new YourAddressesAdapter(AuthUser.getCustomer().getAddresses(),
                this,
                getLayoutInflater(), this));
    }
}