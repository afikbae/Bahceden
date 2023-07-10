package com.swifties.bahceden.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.swifties.bahceden.R;

public class PaymentCardsAdapter extends RecyclerView.Adapter<PaymentCardsAdapter.ViewHolder> {

    // FIXME: AFAIK this file won't exist
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_credit_card_payment, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == 0)
        {
            holder.selectedImage.setImageResource(R.drawable.ic_tick_selected);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView selectedImage;

        public ViewHolder(View view) {
            super(view);

            selectedImage = view.findViewById(R.id.layoutCreditCartSelected);

            view.setOnClickListener(v ->
                    Toast.makeText(view.getContext(), "BaS:" + getBindingAdapterPosition(), Toast.LENGTH_SHORT).show());
        }
    }
}
