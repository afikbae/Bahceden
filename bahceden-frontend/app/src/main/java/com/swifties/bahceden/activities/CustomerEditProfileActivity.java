package com.swifties.bahceden.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.swifties.bahceden.data.AuthUser;
import com.swifties.bahceden.data.RetrofitService;
import com.swifties.bahceden.data.apis.CustomerApi;
import com.swifties.bahceden.databinding.ActivityCustomerEditProfileBinding;
import com.swifties.bahceden.models.Customer;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerEditProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;

    ActivityCustomerEditProfileBinding binding;
    ActivityResultLauncher<String> getImageFromGallery;
    boolean bothRequestsAreDone;

    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.customerEditProfileBackButton.setOnClickListener(backView -> CustomerEditProfileActivity.super.onBackPressed());
        Picasso.get()
                .load(AuthUser.getCustomer().getProfileImageURL().replace("localhost", "10.0.2.2"))
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(binding.customerEditProfileImage);
        binding.customerEditProfileEditFullName.setText(AuthUser.getCustomer().getName());
        //binding.customerEditProfileEditEmail.setText(AuthUser.getCustomer().getEmail());
        getImageFromGallery = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    imageUri = uri;
                    binding.customerEditProfileImage.setImageURI(uri);
                });
        binding.customerEditImageButton.setOnClickListener(v -> {
            getImageFromGallery.launch("image/*");
        });
        bothRequestsAreDone = false;
        binding.customerEditProfileUpdateProfileButton.setOnClickListener(updateView -> {
            if (imageUri != null) {
                try {
                    RequestBody requestBody = new ProgressRequestBody(getInputStreamFromUri(this, imageUri), "image/*");
                    MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", "uploadedImage.jpg", requestBody);

                    RetrofitService.getApi(CustomerApi.class).uploadCustomerImage(AuthUser.getCustomer().getId(), imagePart).enqueue(new Callback<Customer>() {
                        @Override
                        public void onResponse(Call<Customer> call, Response<Customer> response) {
                            if (bothRequestsAreDone) {
                                AuthUser.getInstance().updateUser();
                                CustomerEditProfileActivity.super.onBackPressed();
                            }
                            bothRequestsAreDone = true;
                        }

                        @Override
                        public void onFailure(Call<Customer> call, Throwable t) {
                            throw new RuntimeException(t);
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            else
            {
                bothRequestsAreDone = true;
            }
            AuthUser.getCustomer().setName(binding.customerEditProfileEditFullName.getText().toString());
            RetrofitService.getApi(CustomerApi.class).updateCustomer(AuthUser.getCustomer()).enqueue(new Callback<Customer>() {
                @Override
                public void onResponse(Call<Customer> call, Response<Customer> response) {
                    if (bothRequestsAreDone) {
                        AuthUser.getInstance().updateUser();
                        CustomerEditProfileActivity.super.onBackPressed();
                    }
                    bothRequestsAreDone = true;
                }

                @Override
                public void onFailure(Call<Customer> call, Throwable t) {

                }
            });
        });
    }

    public InputStream getInputStreamFromUri(Context context, Uri uri) throws IOException {
        return context.getContentResolver().openInputStream(uri);
    }

    public static class ProgressRequestBody extends RequestBody {

        private final InputStream inputStream;
        private final String contentType;

        public ProgressRequestBody(final InputStream inputStream, final String contentType) {
            this.inputStream = inputStream;
            this.contentType = contentType;
        }

        @Override
        public MediaType contentType() {
            return MediaType.parse(contentType);
        }

        @Override
        public long contentLength() throws IOException {
            try {
                return inputStream.available();
            } catch (IOException e) {
                return 0;
            }
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            Source source = null;
            try {
                source = Okio.source(inputStream);
                long total = 0;
                long read;

                while ((read = source.read(sink.buffer(), 2048)) != -1) {
                    total += read;
                    sink.flush();
                    // here you can send any notification you want
                    // for example, you can send a broadcast intent to notify your progress bar
                }
            } finally {
                if (source != null) {
                    source.close();
                }
            }
        }
    }
}

