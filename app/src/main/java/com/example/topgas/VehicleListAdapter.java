package com.example.topgas;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VehicleListAdapter extends ArrayAdapter<VehicleModel> {
    Activity activity;
    ArrayList<VehicleModel> arrayList;
    int index=-1;

    public VehicleListAdapter(Context context, ArrayList<VehicleModel> arrayList){
        super(context, R.layout.item_vehicle, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        VehicleModel vehicleModel  = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_vehicle,parent,false);

        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.vehicle_image);
        TextView name= (TextView) convertView.findViewById(R.id.vehicle_name);
        TextView model= (TextView) convertView.findViewById(R.id.vehicle_model);
        TextView brand= (TextView) convertView.findViewById(R.id.vehicle_brand);
        TextView plate_no= (TextView) convertView.findViewById(R.id.vehicle_plate_no);

//        imageView.setImageResource(R.drawable.);
        name.setText(vehicleModel.name);
        model.setText(vehicleModel.model);
        brand.setText(vehicleModel.brand);
        plate_no.setText(vehicleModel.plate_no);
        return convertView;
    }
}
