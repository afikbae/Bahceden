package com.swifties.bahceden.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.swifties.bahceden.activities.ProducerAllProductsActivity;
import com.swifties.bahceden.adapters.CommentProducerViewAdapter;
import com.swifties.bahceden.adapters.ProducerHomeNewReviewsAdapter;
import com.swifties.bahceden.adapters.YourListingsAdapter;
import com.swifties.bahceden.data.AuthUser;
import com.swifties.bahceden.data.RetrofitService;
import com.swifties.bahceden.data.apis.CommentApi;
import com.swifties.bahceden.data.apis.ProducerApi;
import com.swifties.bahceden.data.apis.ProductApi;
import com.swifties.bahceden.databinding.FragmentProducerHomeBinding;
import com.swifties.bahceden.models.Comment;
import com.swifties.bahceden.models.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProducerHomeFragment extends Fragment {

    FragmentProducerHomeBinding binding;
    List<Comment> comments;
    List<Product> listings;
    List<Product> productsInSlider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProducerHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.producerHomeProductsSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProducerAllProductsActivity.class);
                startActivity(intent);
            }
        });
        comments = new ArrayList<>();

        binding.producerHomeNewReviewsRV.setHasFixedSize(true);
        binding.producerHomeYourListingsRV.setHasFixedSize(true);

        binding.producerHomeNewReviewsRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.producerHomeYourListingsRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        binding.producerHomeNewReviewsRV.setAdapter(new ProducerHomeNewReviewsAdapter(comments, getContext(), inflater));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        RetrofitService.getApi(ProducerApi.class).getProductsOfProducer(AuthUser.getProducer().getId()).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.body() != null) {
                    listings = response.body().subList(0, Math.min(response.body().size(), 5));
                    binding.producerHomeSlider.setImageList(listings.stream().map(p -> new SlideModel(p.getImageURL().replace("localhost", "10.0.2.2"), ScaleTypes.FIT)).collect(Collectors.toList()));
                    binding.producerHomeYourListingsRV.setAdapter(new YourListingsAdapter(listings, getContext()));
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getContext(), "There was a problem retrieving your listings", Toast.LENGTH_SHORT).show();
                Log.d("debugPurposes", t.getMessage());
            }
        });

        RetrofitService.getApi(CommentApi.class).getProducersComments(AuthUser.getProducer().getId()).enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                comments.clear();
                if(response.body() != null) {
                    comments.addAll(response.body());
                    binding.producerHomeNewReviewsRV.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }
}