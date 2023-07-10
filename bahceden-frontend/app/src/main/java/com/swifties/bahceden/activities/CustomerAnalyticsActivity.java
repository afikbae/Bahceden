package com.swifties.bahceden.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.swifties.bahceden.R;
import com.swifties.bahceden.data.AuthUser;
import com.swifties.bahceden.data.RetrofitService;
import com.swifties.bahceden.data.apis.CustomerApi;
import com.swifties.bahceden.databinding.ActivityCustomerAnalyticsBinding;
import com.swifties.bahceden.models.Order;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerAnalyticsActivity extends AppCompatActivity {


    private ImageView backButton;
    private PieChart consumerChart;
    private ActivityCustomerAnalyticsBinding binding;
    double marketTotal;
    double bahcedenTotal;
    double profit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerAnalyticsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.customerAnalyticsBackButton.setOnClickListener(view -> CustomerAnalyticsActivity.super.onBackPressed());


        

        RetrofitService.getApi(CustomerApi.class).getCustomerProfit(AuthUser.getCustomer().getId()).enqueue(new Callback<Double[]>() {
            @Override
            public void onResponse(Call<Double[]> call, Response<Double[]> response) {
                marketTotal = response.body()[0];
                System.out.println(marketTotal);
                bahcedenTotal = response.body()[1];
                System.out.println(bahcedenTotal);
                profit = response.body()[2];
                System.out.println(profit);
                setViews();
            }

            @Override
            public void onFailure(Call<Double[]> call, Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }

    private void setViews ()
    {
        ArrayList<PieEntry> dataEntries = new ArrayList<>();
        dataEntries.add(new PieEntry((int) (profit), "Benefits"));
        dataEntries.add(new PieEntry((int) bahcedenTotal, "Costs"));

        PieDataSet pieDataSet = new PieDataSet(dataEntries, "Monthly Data");
        pieDataSet.setColors(getResources().getColor(R.color.plus_green, null), getResources().getColor(R.color.minus_red, null));
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(16);
        PieData pieData = new PieData(pieDataSet);

        binding.customerAnalyticsConsumerChart.setData(pieData);
        binding.customerAnalyticsConsumerChart.setCenterText("Monthly Data");
        binding.customerAnalyticsConsumerChart.getDescription().setEnabled(false);
        binding.customerAnalyticsConsumerChart.animate();

        binding.customerAnalyticsBenefitValue.setText(String.valueOf(profit));
        binding.customerAnalyticsCostsValue.setText(String.valueOf(bahcedenTotal));
        binding.totalCostText.setText(String.format(getString(R.string.analytics_text), marketTotal));
    }
}