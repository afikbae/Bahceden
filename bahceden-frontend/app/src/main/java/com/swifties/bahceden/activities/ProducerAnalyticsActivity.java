package com.swifties.bahceden.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.swifties.bahceden.R;
import com.swifties.bahceden.adapters.AnalyticsProductAdapter;
import com.swifties.bahceden.data.AuthUser;
import com.swifties.bahceden.data.RetrofitService;
import com.swifties.bahceden.data.apis.OrderApi;
import com.swifties.bahceden.databinding.ActivityProducerAnalyticsBinding;
import com.swifties.bahceden.models.Order;
import com.swifties.bahceden.models.Product;
import com.swifties.bahceden.models.ProductInformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProducerAnalyticsActivity extends AppCompatActivity {

    RecyclerView analyticsProductRV;
    RecyclerView.Adapter<AnalyticsProductAdapter.ViewHolder> analyticsProductAdapter;
    RecyclerView.LayoutManager analyticsProductLM;
    Spinner spinner1;
    Spinner spinner2;

    TextView totalText;
    int totalRevenue;
    TextView averageText;
    List<Order> orders;
    List<Product> products;
    List<ProductInformation> productInfo;
    ActivityProducerAnalyticsBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProducerAnalyticsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        totalText = binding.producerAnalyticsTotalText;
        averageText = binding.producerAnalyticsAverageText;

        RetrofitService.getApi(OrderApi.class).getOrdersOfProducer(AuthUser.getProducer().getId()).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if( response.body() == null) return;

                orders = response.body().stream()
                        .filter(order -> (order.getStatus() != Order.OrderStatus.IN_CART || order.getStatus() != Order.OrderStatus.CANCELLED))
                        .collect(Collectors.toList());


                products = new ArrayList<>();
                productInfo = new ArrayList<>();
                totalRevenue = 0;
                for (int i = 0; i < orders.size(); i++) {
                    Order o = orders.get(i);
                    if (!products.contains(o.getProduct())) {
                        products.add(o.getProduct());
                        productInfo.add(new ProductInformation(o.getProduct(), o.getAmount(), o.getTotalPrice()));
                    }
                    productInfo.get(products.indexOf(o.getProduct())).increaseEarnings(o.getTotalPrice());
                    productInfo.get(products.indexOf(o.getProduct())).increaseAmountOfSales(o.getAmount());
                    totalRevenue += o.getTotalPrice();
                }

                binding.producerAnalyticsTotalText.setText(String.format(Locale.ENGLISH,"Total: %d", totalRevenue));


                analyticsProductRV = binding.producerAnalyticsRV;
                analyticsProductRV.setHasFixedSize(true);
                analyticsProductLM = new LinearLayoutManager(ProducerAnalyticsActivity.this);
                analyticsProductRV.setLayoutManager(analyticsProductLM);
                analyticsProductAdapter = new AnalyticsProductAdapter(productInfo, ProducerAnalyticsActivity.this, getLayoutInflater());
                analyticsProductRV.setAdapter(analyticsProductAdapter);
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {

            }
        });
        binding.producerAnalyticsBackButton.setOnClickListener(view -> ProducerAnalyticsActivity.super.onBackPressed());


        spinner1 = binding.producerAnalyticsSpinner1;
        spinner2 = binding.producerAnalyticsSpinner2;

        ArrayAdapter<CharSequence> spinner1Adapter = ArrayAdapter.createFromResource(this, R.array.analytics_spinner_1, android.R.layout.simple_spinner_dropdown_item);
        spinner1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(spinner1Adapter);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                switch (selectedItem) {
                    case "This Month":
                        break;
                    case "Last Month":
                        break;
                    case "This Year":
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                switch (selectedItem) {
                    case "Earnings":
                        totalText.setText("Total : 1500$");
                        averageText.setText("Average : 46$");
                        break;
                    case "Quantity":
                        totalText.setText("Total : 58");
                        averageText.setText("Average : 16");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2 = findViewById(R.id.producerAnalyticsSpinner2);

        ArrayAdapter<CharSequence> spinner2Adapter = ArrayAdapter.createFromResource(this, R.array.analytics_spinner_2, android.R.layout.simple_spinner_dropdown_item);
        spinner2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(spinner2Adapter);
    }
}