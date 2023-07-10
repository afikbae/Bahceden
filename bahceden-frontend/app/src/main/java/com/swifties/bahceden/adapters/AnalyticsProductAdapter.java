package com.swifties.bahceden.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.swifties.bahceden.databinding.LayoutProducerAnalyticsProductItemBinding;
import com.swifties.bahceden.models.ProductInformation;

import java.util.List;
import java.util.Locale;

public class AnalyticsProductAdapter extends RecyclerView.Adapter<AnalyticsProductAdapter.ViewHolder> {

    List<ProductInformation> productInformationList;
    Context context;
    LayoutInflater inflater;

    public AnalyticsProductAdapter(List<ProductInformation> productInformationList, Context context, LayoutInflater inflater) {
        this.productInformationList = productInformationList;
        this.context = context;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public AnalyticsProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AnalyticsProductAdapter.ViewHolder(LayoutProducerAnalyticsProductItemBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AnalyticsProductAdapter.ViewHolder holder, int position) {
        ProductInformation productInformation = productInformationList.get(position);
        Picasso.get()
                .load(productInformation.getProduct().getImageURL().replace("localhost", "10.0.2.2"))
                .into(holder.binding.cartProductImage);
        holder.binding.analyticsProductName.setText(productInformation.getProduct().getName());
        holder.binding.analyticsProductId.setText(String.format(Locale.ENGLISH,"#%d", productInformation.getProduct().getId()));
        holder.binding.producerAnalyticsAmountLeftInStock.setText(String.valueOf(productInformation.getProduct().getAmountInStock()));
        holder.binding.earningsValue.setText(productInformation.getEarningsInString());
        holder.binding.amountSoldValue.setText(productInformation.getAmountSoldInString());
        // TODO: the image of the product can direct the user to its product page
    }

    @Override
    public int getItemCount() {
        return productInformationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LayoutProducerAnalyticsProductItemBinding binding;
        public ViewHolder(LayoutProducerAnalyticsProductItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            
        }
    }
}
