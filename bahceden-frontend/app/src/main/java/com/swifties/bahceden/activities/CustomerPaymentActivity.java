package com.swifties.bahceden.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.swifties.bahceden.R;
import com.swifties.bahceden.adapters.PaymentCardsAdapter;

public class CustomerPaymentActivity extends AppCompatActivity {

    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_payment);

        backButton = findViewById(R.id.customerPaymentBackButton);
        backButton.setOnClickListener(backView -> CustomerPaymentActivity.super.onBackPressed());
        Toast.makeText(this, "AFAIK this page won't exist", Toast.LENGTH_SHORT).show();

        RecyclerView paymentCardsRV = findViewById(R.id.customerPaymentCardRV);

        RecyclerView.LayoutManager paymentCardsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        PaymentCardsAdapter cardsAdapter = new PaymentCardsAdapter();

        paymentCardsRV.setLayoutManager(paymentCardsLayoutManager);
        paymentCardsRV.setAdapter(cardsAdapter);
    }
}