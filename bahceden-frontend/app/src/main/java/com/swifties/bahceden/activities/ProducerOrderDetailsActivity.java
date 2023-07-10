package com.swifties.bahceden.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.swifties.bahceden.R;
import com.swifties.bahceden.data.RetrofitService;
import com.swifties.bahceden.data.apis.OrderApi;
import com.swifties.bahceden.databinding.ActivityProducerOrderDetailsBinding;
import com.swifties.bahceden.models.Order;
import com.swifties.bahceden.models.Product;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProducerOrderDetailsActivity extends AppCompatActivity {

    ActivityProducerOrderDetailsBinding binding;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProducerOrderDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        intent = getIntent();

        // If activity is called without proper intent
        if (!intent.hasExtra("order_id")) {
            Toast.makeText(this, "Could not find order.", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }

        RetrofitService.getApi(OrderApi.class).getOrderByID(intent.getIntExtra("order_id", -1)).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(@NonNull Call<Order> call, @NonNull Response<Order> response) {
                if (response.body() != null) {
                    Order order = response.body();
                    String shipmentType;
                    String amountType;

                    Order.ShipmentType orderShipmentType = order.getShipmentType();
                    Product.UnitType unitType = order.getProduct().getUnitType();

                    // Converting ShipmentType to String
                    if (orderShipmentType == Order.ShipmentType.SHIPMENT) {
                        shipmentType = "Shipment";
                    }
                    else if (orderShipmentType == Order.ShipmentType.PRODUCER_DELIVERY) {
                        shipmentType = "Producer Delivery";
                    }
                    else {
                        shipmentType = "Customer Pickup";
                    }

                    // Converting UnitType to String
                    if (unitType == Product.UnitType.KILOGRAMS) {
                        amountType = "KG";
                    }
                    else if (unitType == Product.UnitType.LITERS) {
                        amountType = "L";
                    }
                    else {
                        amountType = "Packages";
                    }

                    Picasso.get()
                            .load(order.getProduct().getImageURL().replace("localhost", "10.0.2.2"))
                            .into(binding.producerOrderDetailsProductImage);
                    binding.producerOrderDetailsProductName.setText(order.getProduct().getName());
                    binding.producerOrderDetailsProductID.setText(String.format(getString(R.string.number_symbol), order.getProduct().getId()));
                    binding.producerOrderDetailsOrderDate.setText(order.getDateOfPurchase());
                    binding.producerOrderDetailsDeliveryType.setText(shipmentType);
                    binding.producerOrderDetailsAmountValue.setText(String.format(Locale.ENGLISH ,"%d %s", order.getAmount(), amountType));
                    binding.producerOrderDetailsCustomerNameValue.setText(order.getCustomer().getName());
                    binding.producerOrderDetailsCustomerPhoneNumberValue.setText(order.getDeliveryAddress().getPhoneNumber());
                    binding.producerOrderDetailsCustomerAddressValue.setText(order.getDeliveryAddress().getFullAddress());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Order> call, @NonNull Throwable t) {
                Toast.makeText(ProducerOrderDetailsActivity.this, "There was an error retrieving the order.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.producerOrderDetailsBackButton.setOnClickListener(backView -> {
            ProducerOrderDetailsActivity.super.onBackPressed();
            finish();
        });

    }
}