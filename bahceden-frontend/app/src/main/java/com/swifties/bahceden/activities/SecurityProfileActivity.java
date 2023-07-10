package com.swifties.bahceden.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.swifties.bahceden.data.AuthUser;
import com.swifties.bahceden.databinding.ActivitySecurityProfileBinding;

public class SecurityProfileActivity extends AppCompatActivity {
    private ActivitySecurityProfileBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecurityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Back button initialization
        binding.securityBackButton.setOnClickListener(backView -> SecurityProfileActivity.super.onBackPressed());

        firebaseAuth = FirebaseAuth.getInstance();

        // When "change password" button is tapped, send a password reset email
        binding.securityChangePasswordButton.setOnClickListener(changePasswordView -> {
            firebaseAuth.sendPasswordResetEmail(AuthUser.getInstance().getUser().getEmail()).addOnSuccessListener(authResult -> {
                Toast.makeText(this, "Password Change Email Sent", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(authResult -> {
                Log.e("error", authResult.getMessage());
                Toast.makeText(this, authResult.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });
    }
}