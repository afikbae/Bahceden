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
import com.swifties.bahceden.data.apis.ProducerApi;
import com.swifties.bahceden.databinding.ActivityProducerEditProfileBinding;
import com.swifties.bahceden.models.Customer;
import com.swifties.bahceden.models.Producer;

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

public class ProducerEditProfileActivity extends AppCompatActivity {

    ActivityProducerEditProfileBinding binding;
    ActivityResultLauncher<String> getProfileImageFromGallery;
    ActivityResultLauncher<String> getBgImageFromGallery;
    int finishedRequests;

    Uri profileImageUri;
    Uri bgImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProducerEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (!getIntent().hasExtra("signup"))
            binding.producerEditProfileBackButton.setOnClickListener(backView -> ProducerEditProfileActivity.super.onBackPressed());

        binding.producerEditName.setText(AuthUser.getProducer().getName());
        binding.producerEditDukkanName.setText(AuthUser.getProducer().getShopName());
        if (AuthUser.getProducer().getPhoneNumber() != null) {
            binding.countryCodePicker.setDefaultCountryUsingPhoneCode(Integer.parseInt(AuthUser.getProducer().getPhoneNumber().substring(1, AuthUser.getProducer().getPhoneNumber().length() - 10)));
            binding.customerEditNumber.setText(AuthUser.getProducer().getPhoneNumber().substring(AuthUser.getProducer().getPhoneNumber().length() - 10));
        }

        if (AuthUser.getProducer().getProfileImageURL() != null)
            Picasso.get().load(AuthUser.getProducer().getProfileImageURL().replace("localhost", "10.0.2.2"))
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(binding.producerEditProfileImage);
        if (AuthUser.getProducer().getBackgroundImageURL() != null)
            Picasso.get().load(AuthUser.getProducer().getBackgroundImageURL().replace("localhost", "10.0.2.2"))
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(binding.producerEditBannerImage);

        getProfileImageFromGallery = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    profileImageUri = uri;
                    binding.producerEditProfileImage.setImageURI(uri);
                });

        getBgImageFromGallery = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    bgImageUri = uri;
                    binding.producerEditBannerImage.setImageURI(uri);
                });

        binding.customerEditImageButton.setOnClickListener(v -> getProfileImageFromGallery.launch("image/*"));
        binding.producerEditBannerButton.setOnClickListener(v -> getBgImageFromGallery.launch("image/*"));

        finishedRequests = 0;

        binding.producerEditProfileUpdateButton.setOnClickListener(updateView -> {
            ProducerApi api = RetrofitService.getApi(ProducerApi.class);

            if (profileImageUri != null) {
                try {
                    RequestBody requestBody = new CustomerEditProfileActivity.ProgressRequestBody(getInputStreamFromUri(this, profileImageUri), "image/*");
                    MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", "uploadedImage.jpg", requestBody);

                    api.uploadImage(AuthUser.getProducer().getId(), imagePart, "profile").enqueue(new Callback<Producer>() {
                        @Override
                        public void onResponse(Call<Producer> call, Response<Producer> response) {
                            doAfterFinishingThreeRequests();
                        }

                        @Override
                        public void onFailure(Call<Producer> call, Throwable t) {
                            throw new RuntimeException(t);
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            else
                finishedRequests++;

            if (bgImageUri != null) {
                try {
                    RequestBody requestBody = new CustomerEditProfileActivity.ProgressRequestBody(getInputStreamFromUri(this, bgImageUri), "image/*");
                    MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", "uploadedImage.jpg", requestBody);

                    api.uploadImage(AuthUser.getProducer().getId(), imagePart, "back").enqueue(new Callback<Producer>() {
                        @Override
                        public void onResponse(Call<Producer> call, Response<Producer> response) {
                            doAfterFinishingThreeRequests();
                        }

                        @Override
                        public void onFailure(Call<Producer> call, Throwable t) {
                            throw new RuntimeException(t);
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            else
                finishedRequests++;


            AuthUser.getProducer().setName(binding.producerEditName.getText().toString());
            AuthUser.getProducer().setShopName(binding.producerEditDukkanName.getText().toString());
            AuthUser.getProducer().setPhoneNumber(binding.countryCodePicker.getSelectedCountryCodeWithPlus() + binding.customerEditNumber.getText().toString());
            api.updateProducer(AuthUser.getProducer()).enqueue(new Callback<Producer>() {
                @Override
                public void onResponse(Call<Producer> call, Response<Producer> response) {
                    doAfterFinishingThreeRequests();
                }

                @Override
                public void onFailure(Call<Producer> call, Throwable t) {

                }
            });
        });
    }

    private void doAfterFinishingThreeRequests()
    {
        if (++finishedRequests == 3)
        {
            AuthUser.getInstance().updateUser();
            ProducerEditProfileActivity.super.onBackPressed();
        }
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