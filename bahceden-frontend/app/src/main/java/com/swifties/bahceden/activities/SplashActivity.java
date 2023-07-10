package com.swifties.bahceden.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.swifties.bahceden.R;
import com.swifties.bahceden.data.AuthUser;
import com.swifties.bahceden.data.local.DBHelper;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        String[] user = new DBHelper(this).getUser();

        if (user != null)
        {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

            String email = user[0];
            String password = user[1];

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {
                        String userId = FirebaseAuth.getInstance().getUid();
                        firebaseFirestore.collection("user")
                                .document(userId)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            int userType = Integer.parseInt(document.getString("userType"));
                                            if (userType == IntroActivity.PRODUCER_TYPE)
                                                AuthUser.getInstance().createUser(email, userType, SplashActivity.this);

                                            else if (userType == IntroActivity.CUSTOMER_TYPE)
                                                AuthUser.getInstance().createUser(email, userType, SplashActivity.this);

                                        }
                                    }
                                });
                        Toast.makeText(SplashActivity.this, "Logging In...", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SplashActivity.this, "Incorrect email or password.", Toast.LENGTH_SHORT).show();
                            new DBHelper(SplashActivity.this).deleteAllUsers();
                            new DBHelper(SplashActivity.this).deleteAllSearchHistory();
                        }
                    });
        }
        else
        {
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(SplashActivity.this, IntroActivity.class);
                startActivity(intent);
                finish();
            }, 1000);
        }
    }
}