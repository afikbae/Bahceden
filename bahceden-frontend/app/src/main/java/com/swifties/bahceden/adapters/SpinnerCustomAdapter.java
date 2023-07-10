package com.swifties.bahceden.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.swifties.bahceden.R;
import com.swifties.bahceden.uiclasses.SpinnerCustomItem;

import java.util.ArrayList;

public class SpinnerCustomAdapter extends ArrayAdapter {

    public int position;

    public SpinnerCustomAdapter(@NonNull Context context, ArrayList<SpinnerCustomItem> customList) {
        super(context, 0, customList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_custom_spinner, parent, false);
        }

        SpinnerCustomItem spinnerCustomItem = (SpinnerCustomItem) getItem(position);
        ImageView imageView = convertView.findViewById(R.id.spinnerLayoutIcon);
        TextView textView = convertView.findViewById(R.id.spinnerLayoutText);

        if(spinnerCustomItem != null) {
            imageView.setImageResource(spinnerCustomItem.getItemIcon());
            textView.setText(spinnerCustomItem.getItemName());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_custom_dropdown, parent, false);
        }

        SpinnerCustomItem spinnerCustomItem = (SpinnerCustomItem) getItem(position);
        ImageView imageView = convertView.findViewById(R.id.spinnerItemIcon);
        TextView textView = convertView.findViewById(R.id.spinnerItemText);

        if(spinnerCustomItem != null) {
            imageView.setImageResource(spinnerCustomItem.getItemIcon());
            textView.setText(spinnerCustomItem.getItemName());
        }
        return convertView;
    }
}
