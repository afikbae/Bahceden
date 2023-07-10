package com.swifties.bahceden.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.swifties.bahceden.R;
import com.swifties.bahceden.adapters.SpinnerCustomAdapter;
import com.swifties.bahceden.data.RetrofitService;
import com.swifties.bahceden.data.apis.ProductApi;
import com.swifties.bahceden.databinding.ActivityProducerEditProductBinding;
import com.swifties.bahceden.models.Product;
import com.swifties.bahceden.uiclasses.SpinnerCustomItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProducerEditProductActivity extends AppCompatActivity {
    Spinner customCategoriesSpinner;
    Spinner customSubCategoriesSpinner;
    ArrayList<SpinnerCustomItem> customItems;
    ArrayList<ArrayList<SpinnerCustomItem>> customSubItems;
    SpinnerCustomAdapter spinnerSubCategoriesAdapter;
    ActivityProducerEditProductBinding binding;
    ActivityResultLauncher<String> getImageFromGallery;
    Intent intent;
    EditText nameField, descriptionField, amountInStockField, pricePerUnitField;
    Product product;
    Uri imageUri;
    boolean bothRequestsAreDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Make a back button for edit product activity
        super.onCreate(savedInstanceState);
        binding = ActivityProducerEditProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        nameField = binding.producerEditProductNameField;
        descriptionField = binding.producerEditProductDescriptionField;
        amountInStockField = binding.producerEditProductAmountField;
        pricePerUnitField = binding.producerEditProductPriceField;

        intent = getIntent();

        if (intent.hasExtra("product_id")) {
            RetrofitService.getApi(ProductApi.class).getProductById(intent.getIntExtra("product_id", -1)).enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    if (response.body() != null) {
                        product = response.body();
                        nameField.setText(product.getName());
                        descriptionField.setText(product.getDescription());
                        amountInStockField.setText(String.valueOf(product.getAmountInStock()));
                        pricePerUnitField.setText(String.valueOf(product.getPricePerUnit()));
                    }
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    Toast.makeText(ProducerEditProductActivity.this, "There was a problem retrieving the product information", Toast.LENGTH_SHORT).show();
                    Log.d("debugPurposes", t.getMessage());
                }
            });

            getImageFromGallery = registerForActivityResult(new ActivityResultContracts.GetContent(),
                    uri -> {
                        imageUri = uri;
                        binding.producerEditProductImage.setImageURI(uri);
                    });
            binding.producerEditProductImageSelectionButton.setOnClickListener(updateImageView -> {
                getImageFromGallery.launch("image/*");
            });

            bothRequestsAreDone = false;

            binding.producerEditProductUpdateButton.setOnClickListener(updateView -> {
                if (Double.parseDouble(String.valueOf(pricePerUnitField.getText())) <= 0) {
                    pricePerUnitField.setError("Please input a valid price.");
                    return;
                }

                if (Double.parseDouble(String.valueOf(amountInStockField.getText())) <= 0) {
                    amountInStockField.setError("Please input a valid amount left in stock.");
                    return;
                }

                if (String.valueOf(nameField.getText()).equals("") || String.valueOf(descriptionField.getText()).equals("")
                        || String.valueOf(pricePerUnitField.getText()).equals("") || String.valueOf(amountInStockField.getText()).equals("")) {
                    Toast.makeText(this, "Please input appropriate information into the fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                product.setName(String.valueOf(nameField.getText()));
                product.setDescription(String.valueOf(descriptionField.getText()));
                product.setAmountInStock(Double.parseDouble(String.valueOf(amountInStockField.getText())));
                product.setPricePerUnit(Double.parseDouble(String.valueOf(pricePerUnitField.getText())));
                product.setUnitType(Product.UnitType.KILOGRAMS);


                RetrofitService.getApi(ProductApi.class).updateProduct(product).enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        product = response.body();
                        Toast.makeText(ProducerEditProductActivity.this, "Product was updated successfully.", Toast.LENGTH_SHORT).show();
                        if (imageUri != null) {
                            RequestBody requestBody = null;
                            try {
                                requestBody = new ProgressRequestBody(getInputStreamFromUri(getBaseContext(), imageUri), "image/*");
                                MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", "uploadedImage.jpg", requestBody);

                                RetrofitService.getApi(ProductApi.class).uploadProductImage(intent.getIntExtra("product_id", -1), imagePart).enqueue(new Callback<Product>() {
                                    @Override
                                    public void onResponse(Call<Product> call, Response<Product> response) {
                                        if (bothRequestsAreDone) {
                                            Toast.makeText(ProducerEditProductActivity.this, "Product was updated successfully.", Toast.LENGTH_SHORT).show();
                                        }
                                        bothRequestsAreDone = true;
                                    }

                                    @Override
                                    public void onFailure(Call<Product> call, Throwable t) {

                                    }
                                });
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {

                    }
                });
            });
        }

        customSubCategoriesSpinner = binding.producerEditProductItemSubCategoriesSpinner;
        customCategoriesSpinner = binding.producerEditProductItemCategoriesSpinner;

        customItems = getCustomCategoriesList();
        customSubItems = getCustomSubCategoriesList();


        SpinnerCustomAdapter spinnerCategoriesAdapter = new SpinnerCustomAdapter(this, customItems);
        spinnerSubCategoriesAdapter = new SpinnerCustomAdapter(this, customSubItems.get(0));

        if (customCategoriesSpinner != null) {
            customCategoriesSpinner.setAdapter(spinnerCategoriesAdapter);
        }

        if (customSubCategoriesSpinner != null) {
            customSubCategoriesSpinner.setAdapter(spinnerSubCategoriesAdapter);
        }

        customCategoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle the selected item here
                spinnerSubCategoriesAdapter = new SpinnerCustomAdapter(ProducerEditProductActivity.this, customSubItems.get(position));
                customSubCategoriesSpinner.setAdapter(spinnerSubCategoriesAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private ArrayList<SpinnerCustomItem> getCustomCategoriesList() {
        ArrayList<SpinnerCustomItem> items = new ArrayList<>();
        items.add(new SpinnerCustomItem("Dairy", R.drawable.ic_dairy_products));
        items.add(new SpinnerCustomItem("Meat", R.drawable.ic_meat));
        items.add(new SpinnerCustomItem("Vegetable", R.drawable.ic_fruit));
        items.add(new SpinnerCustomItem("Nuts", R.drawable.ic_peanut));
        items.add(new SpinnerCustomItem("Bakery", R.drawable.ic_bread));
        items.add(new SpinnerCustomItem("Other", R.drawable.ic_honey));

        return items;
    }

    private ArrayList<ArrayList<SpinnerCustomItem>> getCustomSubCategoriesList() {
        ArrayList<ArrayList<SpinnerCustomItem>> items = new ArrayList<ArrayList<SpinnerCustomItem>>();
        ArrayList<SpinnerCustomItem> submenu1 = new ArrayList<SpinnerCustomItem>();
        submenu1.add(new SpinnerCustomItem("Milk", 0));
        submenu1.add(new SpinnerCustomItem("Cheese", 0));
        submenu1.add(new SpinnerCustomItem("Yogurt", 0));

        ArrayList<SpinnerCustomItem> submenu2 = new ArrayList<SpinnerCustomItem>();
        submenu2.add(new SpinnerCustomItem("Pepperoni", 0));
        submenu2.add(new SpinnerCustomItem("Ham", 0));
        submenu2.add(new SpinnerCustomItem("Wings", 0));

        ArrayList<SpinnerCustomItem> submenu3 = new ArrayList<SpinnerCustomItem>();
        submenu3.add(new SpinnerCustomItem("Fruits", 0));
        submenu3.add(new SpinnerCustomItem("Eggplant", 0));
        submenu3.add(new SpinnerCustomItem("Tomato", 0));

        ArrayList<SpinnerCustomItem> submenu4 = new ArrayList<SpinnerCustomItem>();
        submenu4.add(new SpinnerCustomItem("Hazelnut", 0));
        submenu4.add(new SpinnerCustomItem("Chestnut", 0));
        submenu4.add(new SpinnerCustomItem("Peanut", 0));

        ArrayList<SpinnerCustomItem> submenu5 = new ArrayList<SpinnerCustomItem>();
        submenu5.add(new SpinnerCustomItem("Bread", 0));
        submenu5.add(new SpinnerCustomItem("Toast", 0));
        submenu5.add(new SpinnerCustomItem("Croissant", 0));

        ArrayList<SpinnerCustomItem> submenu6 = new ArrayList<SpinnerCustomItem>();
        submenu6.add(new SpinnerCustomItem("Honey", 0));
        submenu6.add(new SpinnerCustomItem("Olive Oil", 0));
        submenu6.add(new SpinnerCustomItem("Pekmez", 0));

        items.add(submenu1);
        items.add(submenu2);
        items.add(submenu3);
        items.add(submenu4);
        items.add(submenu5);
        items.add(submenu6);

        return items;
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
            try (Source source = Okio.source(inputStream)) {
                long total = 0;
                long read;

                while ((read = source.read(sink.buffer(), 2048)) != -1) {
                    total += read;
                    sink.flush();
                    // here you can send any notification you want
                    // for example, you can send a broadcast intent to notify your progress bar
                }
            }
        }
    }
}