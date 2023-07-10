package com.swifties.bahceden.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.swifties.bahceden.R;
import com.swifties.bahceden.adapters.SearchResultsAdapter;
import com.swifties.bahceden.adapters.SpinnerCustomAdapter;
import com.swifties.bahceden.data.RetrofitService;
import com.swifties.bahceden.data.apis.ProducerApi;
import com.swifties.bahceden.data.apis.ProductApi;
import com.swifties.bahceden.data.local.DBHelper;
import com.swifties.bahceden.models.Producer;
import com.swifties.bahceden.models.Product;
import com.swifties.bahceden.uiclasses.SpinnerCustomItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerSearchFragment extends Fragment {

    ArrayList<String> searchHistoryList = new ArrayList<>();
    PopupWindow searchHistoryPopup;
    RecyclerView productSearchRV;
    Button filtersButton, sortByButton;
    ImageView searchButton;
    EditText searchEditText;
    LinearLayout filtersMenu, sortByMenu;

    RadioButton priceSortButton, ratingSortButton;

    SwitchCompat producerSwitch, productSwitch, ascDscSwitch;
    Spinner categorySpinner;

     DBHelper dbHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new DBHelper(getActivity());
        // Create the RecyclerView and its adapter
        productSearchRV = view.findViewById(R.id.customerSearchResultsRV);
        productSearchRV.setHasFixedSize(false);

        searchButton = view.findViewById(R.id.customerSearchImg);
        filtersButton = view.findViewById(R.id.customerSearchFiltersButton);
        sortByButton = view.findViewById(R.id.customerSearchSortByButton);
        filtersMenu = view.findViewById(R.id.filtersMenu);
        ascDscSwitch = view.findViewById(R.id.ascendingSortSwitch);
        ratingSortButton = view.findViewById(R.id.ratingSortSwitch);
        priceSortButton = view.findViewById(R.id.priceSortSwitch);
        sortByMenu = view.findViewById(R.id.sortByMenu);
        searchEditText = view.findViewById(R.id.customerSearchEditText);

        filtersButton.setOnClickListener(v -> {
            if (sortByMenu.getVisibility() == View.VISIBLE) sortByMenu.setVisibility(View.GONE);
            if (filtersMenu.getVisibility() == View.GONE) {
                filtersMenu.setVisibility(View.VISIBLE);
                Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.pop);
                filtersMenu.startAnimation(animation);
            } else {
                filtersMenu.setVisibility(View.GONE);
            }
        });

        sortByButton.setOnClickListener(v -> {
            if (filtersMenu.getVisibility() == View.VISIBLE) filtersMenu.setVisibility(View.GONE);
            if (sortByMenu.getVisibility() == View.VISIBLE) {
                sortByMenu.setVisibility(View.GONE);
            } else {
                sortByMenu.setVisibility(View.VISIBLE);
                Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.pop);
                sortByMenu.startAnimation(animation);
            }
        });

        priceSortButton.setOnClickListener(v -> {
            ratingSortButton.setChecked(false);
        });

        ratingSortButton.setOnClickListener(v -> {
            priceSortButton.setChecked(false);

        });

        producerSwitch = view.findViewById(R.id.producerSwitch);
        productSwitch = view.findViewById(R.id.productSwitch);

        producerSwitch.setOnClickListener(v -> {
            productSwitch.setChecked(!productSwitch.isChecked());
            if(producerSwitch.isChecked()){
                priceSortButton.setEnabled(false);
                ratingSortButton.setChecked(true);
                priceSortButton.setChecked(false);
            }
            else{
                priceSortButton.setEnabled(true);
            }
        });

        productSwitch.setOnClickListener(v -> {
            producerSwitch.setChecked(!producerSwitch.isChecked());
            if(productSwitch.isChecked()){
                priceSortButton.setEnabled(true);
            }else{
                priceSortButton.setEnabled(false);
                ratingSortButton.setChecked(true);
                priceSortButton.setChecked(false);
            }
        });



        categorySpinner = view.findViewById(R.id.searchCategoriesSpinner);
        SpinnerCustomAdapter spinnerAdapter = new SpinnerCustomAdapter(getActivity(), getCustomCategoriesList());
        categorySpinner.setAdapter(spinnerAdapter);

        // Create an adapter for the search history list
        ArrayAdapter<String> searchHistoryAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, searchHistoryList);

        // Create a ListView for the search history
        ListView searchHistoryListView = new ListView(requireActivity());
        searchHistoryListView.setAdapter(searchHistoryAdapter);

        // Set a click listener for the search history items
        searchHistoryListView.setOnItemClickListener((parent, view12, position, id) -> {
            String selectedSearchHistory = searchHistoryList.get(position);
            EditText editText = getView().findViewById(R.id.customerSearchEditText);
            editText.setText(selectedSearchHistory);
            searchHistoryPopup.dismiss();
        });

        // Create a ListView for the search history popup
        ListView popupListView = new ListView(requireActivity());
        popupListView.setAdapter(searchHistoryAdapter);
        popupListView.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedSearchHistory = searchHistoryList.get(position);
            EditText editText = getView().findViewById(R.id.customerSearchEditText);
            editText.setText(selectedSearchHistory);
            searchHistoryPopup.dismiss();
        });

        // Create a PopupWindow for the search history popup
        searchHistoryPopup = new PopupWindow(popupListView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        searchHistoryPopup.setOutsideTouchable(true);
        searchHistoryPopup.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        searchEditText.setOnClickListener(k -> {
            fillSearchHistory();
            searchHistoryAdapter.notifyDataSetChanged();
            searchHistoryPopup.showAsDropDown(k);
        });
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                dbHelper.insertSearchHistory(searchEditText.getText().toString());
                if(producerSwitch.isChecked()){
                    RetrofitService.getApi(ProducerApi.class).searchProducer(searchEditText.getText().toString(),
                                    "rating",
                                    !ascDscSwitch.isChecked())
                            .enqueue(new Callback<List<Producer>>() {
                                @Override
                                public void onResponse(Call<List<Producer>> call, Response<List<Producer>> response) {
                                    RecyclerView rv = view.findViewById(R.id.customerSearchResultsRV);
                                    rv.setLayoutManager(new LinearLayoutManager(view.getContext()));
                                    rv.setAdapter(SearchResultsAdapter.fromProducerList(response.body(), view.getContext(), getLayoutInflater()));
                                }

                                @Override
                                public void onFailure(Call<List<Producer>> call, Throwable t) {

                                }
                            });
                }else{
                    RetrofitService.getApi(ProductApi.class).searchProduct(searchEditText.getText().toString(), ratingSortButton.isChecked() ? "rating" : "pricePerUnit", !ascDscSwitch.isChecked())
                            .enqueue(new Callback<List<Product>>() {
                                @Override
                                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                                    RecyclerView rv = view.findViewById(R.id.customerSearchResultsRV);
                                    rv.setLayoutManager(new LinearLayoutManager(view.getContext()));
                                    rv.setAdapter(SearchResultsAdapter.fromProductList(response.body(), view.getContext(), getLayoutInflater()));
                                }

                                @Override
                                public void onFailure(Call<List<Product>> call, Throwable t) {

                                }
                            });

                }
                return true;
            }
        });

        searchButton.setOnClickListener(v -> {

            if (producerSwitch.isChecked()) {

                RetrofitService.getApi(ProducerApi.class).searchProducer(searchEditText.getText().toString(),
                                "rating",
                                !ascDscSwitch.isChecked())
                        .enqueue(new Callback<List<Producer>>() {
                            @Override
                            public void onResponse(Call<List<Producer>> call, Response<List<Producer>> response) {
                                RecyclerView rv = view.findViewById(R.id.customerSearchResultsRV);
                                rv.setLayoutManager(new LinearLayoutManager(view.getContext()));
                                rv.setAdapter(SearchResultsAdapter.fromProducerList(response.body(), view.getContext(), getLayoutInflater()));
                            }

                            @Override
                            public void onFailure(Call<List<Producer>> call, Throwable t) {
                                throw new RuntimeException(t);
                            }
                        });
            }else{
                priceSortButton.setEnabled(true);
                RetrofitService.getApi(ProductApi.class).searchProduct(searchEditText.getText().toString(), ratingSortButton.isChecked() ? "rating" : "pricePerUnit", !ascDscSwitch.isChecked())
                        .enqueue(new Callback<List<Product>>() {
                            @Override
                            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                                RecyclerView rv = view.findViewById(R.id.customerSearchResultsRV);
                                rv.setLayoutManager(new LinearLayoutManager(view.getContext()));
                                rv.setAdapter(SearchResultsAdapter.fromProductList(response.body(), view.getContext(), getLayoutInflater()));
                            }

                            @Override
                            public void onFailure(Call<List<Product>> call, Throwable t) {

                            }
                        });

            }
        });

//        // Set a touch listener for the parent layout
//        ViewGroup parentLayout = view.findViewById(android.R.id.content);
//        parentLayout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    // Remove focus from the EditText
//                    InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(searchHistoryEditText.getWindowToken(), 0);
//                    searchHistoryEditText.clearFocus();
//                    // Dismiss the PopupWindow
//                    searchHistoryPopup.dismiss();
//                    return true;
//                }
//                return false;
//            }
//        });
    }

    private void fillSearchHistory() {
        DBHelper dbHelper = new DBHelper(getContext());
        searchHistoryList = dbHelper.getSearchHistory();
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
}