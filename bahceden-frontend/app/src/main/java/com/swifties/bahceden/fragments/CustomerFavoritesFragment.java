package com.swifties.bahceden.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swifties.bahceden.R;
import com.swifties.bahceden.adapters.FavDukkanAdapter;
import com.swifties.bahceden.adapters.FavItemAdapter;
import com.swifties.bahceden.data.AuthUser;
import com.swifties.bahceden.models.Producer;
import com.swifties.bahceden.models.Product;

import java.util.List;

public class CustomerFavoritesFragment extends Fragment {

    private TextView productTxt, dukkanTxt;
    private RecyclerView customerFavoritesItemsRV;
    private RecyclerView customerFavoritesDukkansRV;

    View rootView;
    List<Product> favProducts;
    List<Producer> favProducers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_customer_favorites, container, false);

        favProducts = AuthUser.getCustomer().getFavoriteProducts();
        favProducers = AuthUser.getCustomer().getFavoriteProducers();

        customerFavoritesItemsRV = rootView.findViewById(R.id.customerFavoriteProductsRV);
        FavItemAdapter customerFavItemAdapter = new FavItemAdapter(favProducts, getContext(), inflater);
        customerFavoritesItemsRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        customerFavoritesItemsRV.setAdapter(customerFavItemAdapter);

        customerFavoritesDukkansRV = rootView.findViewById(R.id.customerFavoritesFavDukkansRV);
        FavDukkanAdapter customerFavDukkanAdapter = new FavDukkanAdapter(favProducers, getContext(), inflater);
        customerFavoritesDukkansRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        customerFavoritesDukkansRV.setAdapter(customerFavDukkanAdapter);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productTxt = view.findViewById(R.id.customerFavoriteProductsButton);
        dukkanTxt = view.findViewById(R.id.customerFavoriteDukkansButton);
        ButtonsListener listener = new ButtonsListener();
        productTxt.setOnClickListener(listener);
        dukkanTxt.setOnClickListener(listener);
    }

    private class ButtonsListener implements View.OnClickListener {

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.customerFavoriteProductsButton:

                    productTxt.setBackgroundColor(getResources().getColor(R.color.bahceden_green, productTxt.getContext().getTheme()));
                    productTxt.setTextColor(getResources().getColor(R.color.white, productTxt.getContext().getTheme()));
                    dukkanTxt.setBackgroundColor(getResources().getColor(R.color.bahceden_background, dukkanTxt.getContext().getTheme()));
                    dukkanTxt.setTextColor(getResources().getColor(R.color.darkGray, dukkanTxt.getContext().getTheme()));
                    rootView.findViewById(R.id.customerFavoritesFavDukkansRV).setVisibility(View.GONE);
                    rootView.findViewById(R.id.customerFavoriteProductsRV).setVisibility(View.VISIBLE);
                    break;
                case R.id.customerFavoriteDukkansButton:

                    dukkanTxt.setBackgroundColor(getResources().getColor(R.color.bahceden_green, dukkanTxt.getContext().getTheme()));
                    dukkanTxt.setTextColor(getResources().getColor(R.color.white, dukkanTxt.getContext().getTheme()));
                    productTxt.setBackgroundColor(getResources().getColor(R.color.bahceden_background, productTxt.getContext().getTheme()));
                    productTxt.setTextColor(getResources().getColor(R.color.darkGray, productTxt.getContext().getTheme()));
                    rootView.findViewById(R.id.customerFavoritesFavDukkansRV).setVisibility(View.VISIBLE);
                    rootView.findViewById(R.id.customerFavoriteProductsRV).setVisibility(View.GONE);
                    break;
            }
        }
    }
}