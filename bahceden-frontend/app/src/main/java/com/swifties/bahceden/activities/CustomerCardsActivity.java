package com.swifties.bahceden.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.swifties.bahceden.R;
import com.swifties.bahceden.adapters.CardsAdapter;

public class CustomerCardsActivity extends AppCompatActivity {

    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_cards);

        backButton = findViewById(R.id.customerCardsBackButton);
        backButton.setOnClickListener(backView -> CustomerCardsActivity.super.onBackPressed());
        Toast.makeText(this, "AFAIK this page won't exist", Toast.LENGTH_SHORT).show();

        RecyclerView yourCardsRV = findViewById(R.id.customerCardsYourCardsRV);

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        CardsAdapter cardsAdapter = new CardsAdapter();

        yourCardsRV.setLayoutManager(linearLayoutManager);
        yourCardsRV.setAdapter(cardsAdapter);
    }
}