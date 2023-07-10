package com.swifties.bahceden.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.swifties.bahceden.R;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder> {

    // FIXME: AFAIK this file won't exist

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_credit_card, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View view) {
            super(view);

            view.setOnClickListener(v ->
                    Toast.makeText(view.getContext(), "BaS:" + getBindingAdapterPosition(), Toast.LENGTH_SHORT).show());
        }
    }
}
