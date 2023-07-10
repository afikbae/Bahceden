package com.swifties.bahceden.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.swifties.bahceden.activities.CategoryItemsActivity;
import com.swifties.bahceden.activities.CustomerViewProductActivity;
import com.swifties.bahceden.adapters.ProductListingAdapter;
import com.swifties.bahceden.data.apis.ProductApi;
import com.swifties.bahceden.data.RetrofitService;
import com.swifties.bahceden.databinding.FragmentCustomerHomeBinding;
import com.swifties.bahceden.models.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerHomeFragment extends Fragment {

    List<Product> newArrivals;
    List<Product> popularProducts;
    FragmentCustomerHomeBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCustomerHomeBinding.inflate(inflater, container, false);
        newArrivals = new ArrayList<>();
        popularProducts = new ArrayList<>();

        binding.customerHomeNewArrivalsRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.customerHomeNewArrivalsRV.setAdapter(new ProductListingAdapter(newArrivals, getContext(), inflater));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        for (int i = 1; i <= binding.customerHomeCategoriesGrid.getChildCount(); i++)
        {
            int finalI = i;
            binding.customerHomeCategoriesGrid.getChildAt(i-1).setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), CategoryItemsActivity.class);
                intent.putExtra("category_id", finalI);
                startActivity(intent);
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        ArrayList<SlideModel> slideModels = new ArrayList<>();

        RetrofitService.getApi(ProductApi.class).getPopularProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                popularProducts.clear();
                popularProducts.addAll(response.body());
                slideModels.addAll(popularProducts.stream().map(p -> new SlideModel(p.getImageURL().replace("localhost", "10.0.2.2"), ScaleTypes.FIT)).collect(Collectors.toList()));
                binding.customerHomeSlider.setImageList(slideModels);
                binding.customerHomeSlider.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemSelected(int i) {
                        // "i" is the index of the clicked item in the slider.
                        // You can use this index to find the clicked product from your popularProducts list.
                        Product clickedProduct = popularProducts.get(i);

                        // Now you can use the clickedProduct to navigate to its specific product page.
                        Intent intent = new Intent(getContext(), CustomerViewProductActivity.class);
                        intent.putExtra("product_id", popularProducts.get(i).getId());
                        intent.putExtra("product_id", clickedProduct.getId());  // Assuming "getId()" is a method in your Product class.
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

            }
        });
        RetrofitService.getApi(ProductApi.class).getNewArrivals().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                newArrivals.clear();
                newArrivals.addAll(response.body());
                binding.customerHomeNewArrivalsRV.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

            }
        });


    }
}