package com.swifties.bahceden.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.swifties.bahceden.databinding.ActivityIntroBinding;

public class IntroActivity extends AppCompatActivity {

    public static final int PRODUCER_TYPE = 1;
    public static final int CUSTOMER_TYPE = 2;
    ActivityIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.introCustomerButton.setOnClickListener(v -> {
            Intent intent = new Intent(IntroActivity.this, SignUpActivity.class);
            intent.putExtra("userType", 2);
            startActivity(intent);
        });

        binding.introProducerButton.setOnClickListener(v -> {
            Intent intent = new Intent(IntroActivity.this, SignUpActivity.class);
            intent.putExtra("userType", 1);
            startActivity(intent);
        });

        binding.introAlreadyHaveAnAccountButton.setOnClickListener(v -> {
            Intent intent = new Intent(IntroActivity.this, LogInActivity.class);
            startActivity(intent);
        });
    }
}