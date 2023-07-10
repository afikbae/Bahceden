package com.swifties.bahceden.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.swifties.bahceden.R;
import com.swifties.bahceden.activities.ProducerMainActivity;
import com.swifties.bahceden.adapters.SpinnerCustomAdapter;
import com.swifties.bahceden.data.AuthUser;
import com.swifties.bahceden.data.RetrofitService;
import com.swifties.bahceden.data.apis.CategoryApi;
import com.swifties.bahceden.data.apis.ProductApi;
import com.swifties.bahceden.databinding.FragmentProducerAddProductBinding;
import com.swifties.bahceden.models.Category;
import com.swifties.bahceden.models.Product;
import com.swifties.bahceden.uiclasses.SpinnerCustomItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProducerAddProductFragment extends Fragment {

    Spinner itemCategoriesSpinner;
    Spinner itemSubCategoriesSpinner;
    Spinner itemUnitTypeSpinner;
    ArrayList<SpinnerCustomItem> categoryItems;
    ArrayList<ArrayList<SpinnerCustomItem>> subCategoryItems;
    ArrayList<SpinnerCustomItem> customUnitTypes;

    EditText productNameField, productDescriptionField, productPriceField, productAvailableAmountField;
    SpinnerCustomAdapter spinnerSubCategoriesAdapter;
    SpinnerCustomAdapter spinnerCategoriesAdapter;
    SpinnerCustomAdapter spinnerUnitTypeAdapter;
    Button addProductButton;
    ActivityResultLauncher<String> getImageFromGallery;
    Uri imageUri;
    FragmentProducerAddProductBinding binding;
    boolean bothRequestsAreDone;
    Product product;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProducerAddProductBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        itemSubCategoriesSpinner = binding.addItemSubCategoriesSpinner;
        itemCategoriesSpinner = binding.addItemCategoriesSpinner;
        itemUnitTypeSpinner = binding.addProductUnitTypeSpinner;

//        customItems = getCustomCategoriesList();
//        customSubItems = getCustomSubCategoriesList();
        categoryItems = new ArrayList<>();
        subCategoryItems = new ArrayList<>();
        customUnitTypes = getUnitTypeList();

        productNameField = binding.nameOfItemField;
        productDescriptionField = binding.descriptionOfItemField;
        productPriceField = binding.addProductPriceOfItemField;
        productAvailableAmountField = binding.availableAmountOfItemField;

        addProductButton = binding.producerAddProductUpdateButton;

        getImageFromGallery = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    imageUri = uri;
                    binding.producerAddProductAddImage.setImageURI(uri);
                });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RetrofitService.getApi(CategoryApi.class).getAllCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                categoryItems.addAll(response.body().stream().filter(c -> c.getParentId() == 0).map(c -> new SpinnerCustomItem(c,0)).collect(Collectors.toList()));
                for (Category category : response.body().stream().filter(c -> c.getParentId() != 0).collect(Collectors.toList()))
                {
                    if (subCategoryItems.size() <= category.getParentId())
                    {
                        subCategoryItems.add(category.getParentId()-1, new ArrayList<>());
                    }
                    subCategoryItems.get(category.getParentId()-1).add(new SpinnerCustomItem(category, 0));
                }

                spinnerCategoriesAdapter = new SpinnerCustomAdapter(requireActivity(), categoryItems);
                spinnerSubCategoriesAdapter = new SpinnerCustomAdapter(requireActivity(), subCategoryItems.get(0));
                spinnerUnitTypeAdapter = new SpinnerCustomAdapter(requireActivity(), customUnitTypes);



                if (itemCategoriesSpinner != null) {
                    itemCategoriesSpinner.setAdapter(spinnerCategoriesAdapter);
                }

                if (itemSubCategoriesSpinner != null) {
                    itemSubCategoriesSpinner.setAdapter(spinnerSubCategoriesAdapter);
                }

                if (itemUnitTypeSpinner != null) {
                    itemUnitTypeSpinner.setAdapter(spinnerUnitTypeAdapter);
                }

                itemUnitTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((SpinnerCustomAdapter)itemUnitTypeSpinner.getAdapter()).position = position;

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                itemCategoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // Handle the selected item here
                        spinnerSubCategoriesAdapter = new SpinnerCustomAdapter(getActivity(), subCategoryItems.get(position));
                        itemSubCategoriesSpinner.setAdapter(spinnerSubCategoriesAdapter);
                        ((SpinnerCustomAdapter)itemCategoriesSpinner.getAdapter()).position = position;

                        itemSubCategoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                ((SpinnerCustomAdapter)itemSubCategoriesSpinner.getAdapter()).position = position;
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                binding.producerAddProductAddImage.setOnClickListener(v -> {
                    getImageFromGallery.launch("image/*");
                });

                product = new Product();

                addProductButton.setOnClickListener(addView -> {
                    // TODO: take the product name, description, price, and category (further TODO), and make a new Product object.
                    //  afterwards, pass this new product onto the database.


                    if (String.valueOf(productNameField.getText()).equals("") ||
                            String.valueOf(productDescriptionField.getText()).equals("") ||
                            String.valueOf(productPriceField.getText()).equals("") ||
                            String.valueOf(productAvailableAmountField).equals("")) {
                        Toast.makeText(view.getContext(), "Please input appropriate information to the fields.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (Double.parseDouble(String.valueOf(productPriceField.getText())) < 0) {
                        productPriceField.setError("Please input an appropriate price");
                        return;
                    }

                    // Making the new product
                    product.setName(String.valueOf(productNameField.getText()));
                    product.setDescription(String.valueOf(productDescriptionField.getText()));
                    product.setPricePerUnit(Double.parseDouble(String.valueOf(productPriceField.getText())));
                    product.setAmountInStock(Double.parseDouble(String.valueOf(productAvailableAmountField.getText())));
                    product.setUnitType(customUnitTypes.get(((SpinnerCustomAdapter)itemUnitTypeSpinner.getAdapter()).position).getUnitType());
                    product.setCategory(Category.getCategory(categoryItems.get(((SpinnerCustomAdapter)itemCategoriesSpinner.getAdapter()).position).getCategory()));
                    product.setSubCategory(Category.getCategory(subCategoryItems.get(((SpinnerCustomAdapter)itemCategoriesSpinner.getAdapter()).position)
                            .get(((SpinnerCustomAdapter)itemSubCategoriesSpinner.getAdapter()).position).getCategory()));
                    product.setProducer(AuthUser.getProducer());



                    RetrofitService.getApi(ProductApi.class).saveProduct(product).enqueue(new Callback<Product>() {
                        @Override
                        public void onResponse(Call<Product> call, Response<Product> response) {
                            if (response.body() == null) return;
                            product = response.body();
                            if (imageUri != null) {
                                try {
                                    RequestBody requestBody = new ProgressRequestBody(getInputStreamFromUri(requireContext(), imageUri), "image/*");
                                    MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", "uploadedImage.jpg", requestBody);

                                    RetrofitService.getApi(ProductApi.class).uploadProductImage(product.getId(), imagePart).enqueue(new Callback<Product>() {
                                        @Override
                                        public void onResponse(Call<Product> call, Response<Product> response) {
                                            if (response.body() == null) return;
                                            product = response.body();
                                            Intent intent = new Intent(getContext(), ProducerMainActivity.class);
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onFailure(Call<Product> call, Throwable t) {
                                            throw new RuntimeException(t);
                                        }
                                    });
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                Intent intent = new Intent(getContext(), ProducerMainActivity.class);
                                startActivity(intent);
                            }
                        }
                        @Override
                        public void onFailure(Call<Product> call, Throwable t) {
                            throw new RuntimeException(t);
                        }
                    });
                });
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                throw new RuntimeException(t);
            }
        });


    }

    private ArrayList<SpinnerCustomItem> getUnitTypeList() {
        ArrayList<SpinnerCustomItem> items = new ArrayList<>();
        items.add(new SpinnerCustomItem(Product.UnitType.KILOGRAMS, R.drawable.bahceden_logo));
        items.add(new SpinnerCustomItem(Product.UnitType.LITERS, R.drawable.bahceden_logo));
        items.add(new SpinnerCustomItem(Product.UnitType.PACKAGES, R.drawable.bahceden_logo));
        return items;
    }
//
//    private ArrayList<SpinnerCustomItem> getCustomCategoriesList() {
//        ArrayList<SpinnerCustomItem> items = new ArrayList<>();
//        items.add(new SpinnerCustomItem("Dairy", R.drawable.ic_dairy_products));
//        items.add(new SpinnerCustomItem("Meat", R.drawable.ic_meat));
//        items.add(new SpinnerCustomItem("Vegetable", R.drawable.ic_fruit));
//        items.add(new SpinnerCustomItem("Nuts", R.drawable.ic_peanut));
//        items.add(new SpinnerCustomItem("Bakery", R.drawable.ic_bread));
//        items.add(new SpinnerCustomItem("Other", R.drawable.ic_honey));
//
//        return items;
//    }
//
//    private ArrayList<ArrayList<SpinnerCustomItem>> getCustomSubCategoriesList() {
//        ArrayList<ArrayList<SpinnerCustomItem>> items = new ArrayList<ArrayList<SpinnerCustomItem>>();
//        ArrayList<SpinnerCustomItem> submenu1 = new ArrayList<SpinnerCustomItem>();
//        submenu1.add(new SpinnerCustomItem("Milk", 0));
//        submenu1.add(new SpinnerCustomItem("Cheese", 0));
//        submenu1.add(new SpinnerCustomItem("Yogurt", 0));
//
//        ArrayList<SpinnerCustomItem> submenu2 = new ArrayList<SpinnerCustomItem>();
//        submenu2.add(new SpinnerCustomItem("Pepperoni", 0));
//        submenu2.add(new SpinnerCustomItem("Ham", 0));
//        submenu2.add(new SpinnerCustomItem("Wings", 0));
//
//        ArrayList<SpinnerCustomItem> submenu3 = new ArrayList<SpinnerCustomItem>();
//        submenu3.add(new SpinnerCustomItem("Fruits", 0));
//        submenu3.add(new SpinnerCustomItem("Eggplant", 0));
//        submenu3.add(new SpinnerCustomItem("Tomato", 0));
//
//        ArrayList<SpinnerCustomItem> submenu4 = new ArrayList<SpinnerCustomItem>();
//        submenu4.add(new SpinnerCustomItem("Hazelnut", 0));
//        submenu4.add(new SpinnerCustomItem("Chestnut", 0));
//        submenu4.add(new SpinnerCustomItem("Peanut", 0));
//
//        ArrayList<SpinnerCustomItem> submenu5 = new ArrayList<SpinnerCustomItem>();
//        submenu5.add(new SpinnerCustomItem("Bread", 0));
//        submenu5.add(new SpinnerCustomItem("Toast", 0));
//        submenu5.add(new SpinnerCustomItem("Croissant", 0));
//
//        ArrayList<SpinnerCustomItem> submenu6 = new ArrayList<SpinnerCustomItem>();
//        submenu6.add(new SpinnerCustomItem("Honey", 0));
//        submenu6.add(new SpinnerCustomItem("Olive Oil", 0));
//        submenu6.add(new SpinnerCustomItem("Pekmez", 0));
//
//        items.add(submenu1);
//        items.add(submenu2);
//        items.add(submenu3);
//        items.add(submenu4);
//        items.add(submenu5);
//        items.add(submenu6);
//
//        return items;
//    }

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