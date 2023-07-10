package com.swifties.bahceden.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.hbb20.CountryCodePicker;
import com.swifties.bahceden.R;
import com.swifties.bahceden.data.AuthUser;
import com.swifties.bahceden.data.RetrofitService;
import com.swifties.bahceden.data.apis.AddressesApi;
import com.swifties.bahceden.databinding.ActivityCustomerAddAddressBinding;
import com.swifties.bahceden.models.Action;
import com.swifties.bahceden.models.Address;
import com.swifties.bahceden.models.Customer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerAddAddressActivity extends AppCompatActivity {

    private EditText addressNameField, fullAddressField, phoneNumberField;
    private CountryCodePicker countryCodePicker;
    private ActivityCustomerAddAddressBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerAddAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Back button and listener initialization

            binding.customerAddAddressBackButton.setOnClickListener(backView -> {
                        CustomerAddAddressActivity.super.onBackPressed();
                        finish();
                    });

        // Fields
        addressNameField = binding.customerAddAddressEditTitleOfAddress;
        fullAddressField = binding.customerAddAddressEditFullAddress;

        // Phone number
        phoneNumberField = binding.customerAddAddressEditPhoneNumber;
        countryCodePicker = binding.customerAddAddressEditCountryCode;

        Button addAddressButton = findViewById(R.id.customerAddAddressButton);


        Intent intent = getIntent();
        // If this activity gets an address_id on its intent,
        // it should be used to update the address
        if (intent.hasExtra("address_id"))
        {
            int addressId = intent.getIntExtra("address_id", 0);
            Customer c = AuthUser.getCustomer();
            Address address = c.getAddresses().stream().filter(a -> a.getId() == addressId).findFirst().get();
            addressNameField.setText(address.getTitle());
            fullAddressField.setText(address.getFullAddress());
            phoneNumberField.setText(address.getPhoneNumber().substring(address.getPhoneNumber().length() - 10));
            countryCodePicker.setDefaultCountryUsingPhoneCode(Integer.parseInt(address.getPhoneNumber().substring(1,address.getPhoneNumber().length() - 10)));
            addAddressButton.setText(R.string.update_address);
            addAddressButton.setOnClickListener(v -> {
                RetrofitService.getApi(AddressesApi.class).updateAddress(
                        new Address(addressId, addressNameField.getText().toString(),
                                fullAddressField.getText().toString(),
                                countryCodePicker.getSelectedCountryCodeWithPlus() + phoneNumberField.getText(),
                                AuthUser.getCustomer()))
                        .enqueue(new Callback<Address>() {
                    @Override
                    public void onResponse(Call<Address> call, Response<Address> response) {
                        Toast.makeText(CustomerAddAddressActivity.this,
                                "successful", Toast.LENGTH_SHORT).show();
                        AuthUser.getInstance().updateUser(new Action() {
                            @Override
                            public void act() {
                                CustomerAddAddressActivity.super.onBackPressed();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<Address> call, Throwable t) {
                        throw new RuntimeException(t);
                    }
                });
            });
        }
        // Otherwise, add a new address
        else
        {
            addAddressButton.setOnClickListener(addAddressView -> {

                String addressName = String.valueOf(addressNameField.getText());
                String fullAddress = String.valueOf(fullAddressField.getText());
                String phoneNumber = String.valueOf(countryCodePicker.getSelectedCountryCodeWithPlus() + phoneNumberField.getText());

                // Check if any field is empty
                if (!addressName.equals("") && !fullAddress.equals("") && !phoneNumber.equals("")) {
                    Address newAddress = new Address(addressName, fullAddress, phoneNumber, AuthUser.getCustomer());
                    RetrofitService.getApi(AddressesApi.class).saveAddress(newAddress).enqueue(new Callback<Address>() {
                        @Override
                        public void onResponse(@NonNull Call<Address> call, @NonNull Response<Address> response) {
                            Toast.makeText(CustomerAddAddressActivity.this,
                                    "successful", Toast.LENGTH_SHORT).show();
                            AuthUser.getInstance().updateUser(new Action() {
                                @Override
                                public void act() {
                                    CustomerAddAddressActivity.super.onBackPressed();
                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<Address> call, Throwable t) {
                            Toast.makeText(CustomerAddAddressActivity.this,
                                    "There was a problem adding the new address", Toast.LENGTH_SHORT).show();
                            Log.d("debug",
                                    "There was a problem adding the new address " + t.getMessage());
                        }
                    });
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }
}