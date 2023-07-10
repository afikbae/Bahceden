package com.swifties.bahceden.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.swifties.bahceden.R;
import com.swifties.bahceden.adapters.SpinnerCustomAdapter;
import com.swifties.bahceden.data.RetrofitService;
import com.swifties.bahceden.data.apis.CategoryApi;
import com.swifties.bahceden.data.apis.ProducerApi;
import com.swifties.bahceden.models.Category;
import com.swifties.bahceden.models.ProducerDataDTO;
import com.swifties.bahceden.uiclasses.SpinnerCustomItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProducerDataFragment extends Fragment {

    Spinner customDataCategoriesSpinner;
    Spinner customDataSubCategoriesSpinner;
    ArrayList<SpinnerCustomItem> categoryItems;
    ArrayList<ArrayList<SpinnerCustomItem>> subCategoryItems;
    SpinnerCustomAdapter spinnerCategoriesAdapter;
    SpinnerCustomAdapter spinnerSubCategoriesAdapter;
    PieChart producerChart;
    private TextView producerDataMarketMin;
    private TextView producerDataMarketAverage;
    private TextView producerDataMarketMax;

    private TextView producerDataOtherMin;
    private TextView producerDataOtherAverage;
    private TextView producerDataOtherMax;

    private TextView producerDataOurRecommendationValue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        categoryItems = new ArrayList<>();
        subCategoryItems = new ArrayList<>();
        return inflater.inflate(R.layout.fragment_producer_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        producerChart = view.findViewById(R.id.producerDataChart);
        customDataCategoriesSpinner = view.findViewById(R.id.producerDataCategoriesSpinner);
        customDataSubCategoriesSpinner = view.findViewById(R.id.producerDataSubCategoriesSpinner);
        producerDataMarketMin = view.findViewById(R.id.producerDataMarketMin);
        producerDataMarketAverage = view.findViewById(R.id.producerDataMarketAverage);
        producerDataMarketMax = view.findViewById(R.id.producerDataMarketMax);

        producerDataOtherMin = view.findViewById(R.id.producerDataOtherMin);
        producerDataOtherAverage = view.findViewById(R.id.producerDataOtherAverage);
        producerDataOtherMax = view.findViewById(R.id.producerDataOtherMax);

        producerDataOurRecommendationValue = view.findViewById(R.id.producerDataOurRecommendationValue);

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

                if (customDataCategoriesSpinner != null) {
                    customDataCategoriesSpinner.setAdapter(spinnerCategoriesAdapter);
                }

                if (customDataSubCategoriesSpinner != null) {
                    customDataSubCategoriesSpinner.setAdapter(spinnerSubCategoriesAdapter);
                }
                setViews();

                customDataCategoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // Handle the selected item here
                        spinnerSubCategoriesAdapter = new SpinnerCustomAdapter(getActivity(), subCategoryItems.get(position));
                        customDataSubCategoriesSpinner.setAdapter(spinnerSubCategoriesAdapter);
                        ((SpinnerCustomAdapter)customDataCategoriesSpinner.getAdapter()).position = position;
                        setViews();
                        customDataSubCategoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                ((SpinnerCustomAdapter)customDataSubCategoriesSpinner.getAdapter()).position = position;
                                setViews();
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


            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }

    public void setViews ()
    {
        RetrofitService.getApi(ProducerApi.class).getProductData(Category
                .getCategory(
                        subCategoryItems
                                .get(((SpinnerCustomAdapter)customDataCategoriesSpinner.getAdapter()).position)
                                .get(((SpinnerCustomAdapter)customDataSubCategoriesSpinner.getAdapter()).position).getCategory()
                ).getId()).enqueue(new Callback<ProducerDataDTO>() {
            @Override
            public void onResponse(Call<ProducerDataDTO> call, Response<ProducerDataDTO> response) {
                if (response.body() == null) return;
                producerDataMarketMin.setText(String.format("%.2f",response.body().getMarketPriceMIN()));
                producerDataMarketAverage.setText(String.format("%.2f",response.body().getMarketPriceAVG()));
                producerDataMarketMax.setText(String.format("%.2f",response.body().getMarketPriceMAX()));

                producerDataOtherMin.setText(String.format("%.2f",response.body().getSellersMIN()));
                producerDataOtherAverage.setText(String.format("%.2f",response.body().getSellersAVG()));
                producerDataOtherMax.setText(String.format("%.2f",response.body().getSellersMAX()));

                producerDataOurRecommendationValue.setText(String.format("%.2f",response.body().getRecommendedPrice()));
            }

            @Override
            public void onFailure(Call<ProducerDataDTO> call, Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }

//    public void setPiechart ()
//    {
//        ArrayList<PieEntry> dataEntries = new ArrayList<>();
//        dataEntries.add(new PieEntry(70, "Our Recommendation"));
//        dataEntries.add(new PieEntry(40, "Other Prices"));
//
//        PieDataSet pieDataSet = new PieDataSet(dataEntries, "Success Rate");
//        pieDataSet.setColors(getResources().getColor(R.color.plus_green, null), getResources().getColor(R.color.minus_red, null));
//        pieDataSet.setValueTextColor(Color.WHITE);
//        pieDataSet.setValueTextSize(16);
//
//        PieData pieData = new PieData(pieDataSet);
//
//        producerChart.setData(pieData);
//        producerChart.setCenterText("Success Rate");
//        producerChart.getDescription().setEnabled(false);
//        producerChart.animate();
//    }

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
}