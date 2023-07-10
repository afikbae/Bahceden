package com.swifties.bahceden.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.swifties.bahceden.R;
import com.swifties.bahceden.data.AuthUser;
import com.swifties.bahceden.data.local.DBHelper;

public class LogInActivity extends AppCompatActivity {

    Button backButton, logIn;
    TextView doNotHaveAnAccount, forgetPassword;
    EditText emailInput, passwordInput;
    TextInputLayout textInputLayout;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private int userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        backButton = findViewById(R.id.loginBackButton);
        emailInput = findViewById(R.id.loginEmail);
        doNotHaveAnAccount = findViewById(R.id.dontHaveAccount);
        passwordInput = findViewById(R.id.loginPassword);
        textInputLayout = findViewById(R.id.loginPasswordLayout);
        forgetPassword = findViewById(R.id.forgetPassword);
        logIn = findViewById(R.id.logIn);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userType = getIntent().getIntExtra("userType", -1);

        textInputLayout.setEndIconVisible(false);

        //Text watcher for email Input
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailInput.getCompoundDrawables()[0].setTint(ContextCompat.getColor(LogInActivity.this, R.color.bahceden_green));
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty())
                    emailInput.getCompoundDrawables()[0].setTint(ContextCompat.getColor(LogInActivity.this, R.color.darkGray));
            }
        });

//        Text Watcher for password Input
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordInput.getCompoundDrawables()[0].setTint(ContextCompat.getColor(LogInActivity.this, R.color.bahceden_green));
                textInputLayout.setEndIconVisible(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    passwordInput.getCompoundDrawables()[0].setTint(ContextCompat.getColor(LogInActivity.this, R.color.darkGray));
                    textInputLayout.setEndIconVisible(false);
                }
            }
        });

        logIn.setOnClickListener(logInView -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.setError("Enter Valid Email Address!");
            } else if (password.isEmpty()) {
                passwordInput.setError("Password Can't be Empty!");
            } else {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
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

                                                    CheckBox rememberMe = findViewById(R.id.loginRememberMe);

                                                    if (rememberMe.isChecked())
                                                        new DBHelper(LogInActivity.this).insertUser(email, password);

                                                    if (userType == IntroActivity.PRODUCER_TYPE)
                                                        AuthUser.getInstance().createUser(email, userType, LogInActivity.this);

                                                    else if (userType == IntroActivity.CUSTOMER_TYPE)
                                                        AuthUser.getInstance().createUser(email, userType, LogInActivity.this);

                                                }
                                            }
                                        });
                                Toast.makeText(LogInActivity.this, "Logging In...", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LogInActivity.this, "Incorrect email or password.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        doNotHaveAnAccount.setOnClickListener(doNotHaveAccountView -> {
            Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        forgetPassword.setOnClickListener(forgetPasswordView -> {
            Intent intent = new Intent(LogInActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        backButton.setOnClickListener(backView -> LogInActivity.super.onBackPressed());
    }
}