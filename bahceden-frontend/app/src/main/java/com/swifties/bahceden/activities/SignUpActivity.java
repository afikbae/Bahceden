package com.swifties.bahceden.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;
import com.swifties.bahceden.R;
import com.swifties.bahceden.data.AuthUser;
import com.swifties.bahceden.data.RetrofitService;
import com.swifties.bahceden.data.apis.CustomerApi;
import com.swifties.bahceden.data.apis.ProducerApi;
import com.swifties.bahceden.models.Customer;
import com.swifties.bahceden.models.Producer;
import com.swifties.bahceden.models.User;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    Button backButton, signUp;
    TextView haveAnAccount;
    EditText nameInput, emailInput, passwordInput, confirmPasswordInput;
    TextInputLayout passwordInputLayout, confirmPasswordInputLayout;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private int userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        backButton = findViewById(R.id.signUpBackButton);
        signUp = findViewById(R.id.signUpButton);
        nameInput = findViewById(R.id.signUpNameField);
        emailInput = findViewById(R.id.signUpEmailField);
        haveAnAccount = findViewById(R.id.signUpHaveAnAccountButton);
        passwordInput = findViewById(R.id.signUpPasswordField);
        confirmPasswordInput = findViewById(R.id.signUpConfirmPasswordField);
        passwordInputLayout = findViewById(R.id.signUpPasswordLayout);
        confirmPasswordInputLayout = findViewById(R.id.signUpConfirmPasswordLayout);

        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userType = getIntent().getIntExtra("userType", -1);

        passwordInputLayout.setEndIconVisible(false);
        confirmPasswordInputLayout.setEndIconVisible(false);

        //        Text watcher for name Input
        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameInput.getCompoundDrawables()[0].setTint(ContextCompat.getColor(SignUpActivity.this, R.color.bahceden_green));
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty())
                    nameInput.getCompoundDrawables()[0].setTint(ContextCompat.getColor(SignUpActivity.this, R.color.darkGray));
            }
        });

        //        Text watcher for email Input
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailInput.getCompoundDrawables()[0].setTint(ContextCompat.getColor(SignUpActivity.this, R.color.bahceden_green));
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty())
                    emailInput.getCompoundDrawables()[0].setTint(ContextCompat.getColor(SignUpActivity.this, R.color.darkGray));
            }
        });

//        Text Watcher for password Input
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordInput.getCompoundDrawables()[0].setTint(ContextCompat.getColor(SignUpActivity.this, R.color.bahceden_green));
                passwordInputLayout.setEndIconVisible(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    passwordInput.getCompoundDrawables()[0].setTint(ContextCompat.getColor(SignUpActivity.this, R.color.darkGray));
                    passwordInputLayout.setEndIconVisible(false);
                }
            }
        });

        //        Text Watcher for confirm password Input
        confirmPasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                confirmPasswordInput.getCompoundDrawables()[0].setTint(ContextCompat.getColor(SignUpActivity.this, R.color.bahceden_green));
                confirmPasswordInputLayout.setEndIconVisible(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    confirmPasswordInput.getCompoundDrawables()[0].setTint(ContextCompat.getColor(SignUpActivity.this, R.color.darkGray));
                    confirmPasswordInputLayout.setEndIconVisible(false);
                }
            }
        });

        signUp.setOnClickListener(this::onClick);

        haveAnAccount.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
            startActivity(intent);
        });

        backButton.setOnClickListener(v -> SignUpActivity.super.onBackPressed());

    }

    private void onClick(View v) {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        final User[] appUser = new User[1];

        if (name.isEmpty()) {
            nameInput.setError("Name Can't be Empty!");
        } else if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Enter a Valid Email Address!");
        } else if (password.isEmpty()) {
            passwordInput.setError("Password Can't be Empty!");
        } else if (confirmPassword.isEmpty()) {
            confirmPasswordInput.setError("Confirm Password Can't be Empty!");
        } else if (!password.equals(confirmPassword)) {
            confirmPasswordInputLayout.setEndIconVisible(false);
            confirmPasswordInput.setError("Passwords Don't Match!");
        } else {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Map<String, String> user = new HashMap<>();
                        user.put("userType", userType + "");
                        user.put("email", email);
                        user.put("password", password);

                        firebaseFirestore.collection("user")
                                .document(FirebaseAuth.getInstance().getUid())
                                .set(user);

                        Toast.makeText(SignUpActivity.this, "User Created Successfully.", Toast.LENGTH_SHORT).show();
                        Toast.makeText(SignUpActivity.this, "User Sending to Database.", Toast.LENGTH_SHORT).show();

                        if (userType == IntroActivity.PRODUCER_TYPE) {
                            appUser[0] = new Producer();
                            appUser[0].setName(name);
                            appUser[0].setEmail(email);
                            RetrofitService.getApi(ProducerApi.class).save((Producer) appUser[0]).enqueue(new Callback<Producer>() {
                                @Override
                                public void onResponse(Call<Producer> call, Response<Producer> response) {
                                    Toast.makeText(SignUpActivity.this, "User Sent Successfully", Toast.LENGTH_SHORT).show();
                                    AuthUser.getInstance().createUser(email, userType, SignUpActivity.this);
                                }

                                @Override
                                public void onFailure(Call<Producer> call, Throwable t) {
                                    Toast.makeText(SignUpActivity.this, "Error sending user to database", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else if (userType == IntroActivity.CUSTOMER_TYPE) {
                            appUser[0] = new Customer();
                            appUser[0].setName(name);
                            appUser[0].setEmail(email);

                            RetrofitService.getApi(CustomerApi.class).save((Customer) appUser[0]).enqueue(new Callback<Customer>() {
                                @Override
                                public void onResponse(Call<Customer> call, Response<Customer> response) {
                                    Toast.makeText(SignUpActivity.this, "User Sent Successfully", Toast.LENGTH_SHORT).show();
                                    AuthUser.getInstance().createUser(email, userType, SignUpActivity.this);
                                }

                                @Override
                                public void onFailure(Call<Customer> call, Throwable t) {
                                    //Toast.makeText(SignUpActivity.this, "Error sending user to database", Toast.LENGTH_SHORT).show();
                                    throw new RuntimeException(t);
                                }
                            });
                        }
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(SignUpActivity.this, "Email address is already registered.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Failed " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
}