package com.example.topgas;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class FuelListAdapter extends ArrayAdapter<FuelModel> {
    Activity activity;
    ArrayList<FuelModel> arrayList;
    int index=-1;

    public FuelListAdapter(Context context, ArrayList<FuelModel> arrayList){
        super(context, R.layout.item_fuel, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        FuelModel fuelModel  = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_fuel,parent,false);

        }

        TextView fuelType= (TextView) convertView.findViewById(R.id.fuelType);
        TextView fuelPrice= (TextView) convertView.findViewById(R.id.fuelPrice);

        fuelType.setText(fuelModel.fuelType);
        fuelPrice.setText(String.format("%.2g%n", fuelModel.fuelPrice).toString());
        return convertView;
    }
}
