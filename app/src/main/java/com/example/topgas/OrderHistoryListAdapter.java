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

public class OrderHistoryListAdapter extends ArrayAdapter<OrderHistoryModel> {


    public OrderHistoryListAdapter(Context context, ArrayList<OrderHistoryModel> arrayList){
        super(context, R.layout.item_orderhistory, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        OrderHistoryModel orderHistoryModel  = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_orderhistory,parent,false);

        }

        TextView vehicleName=(TextView) convertView.findViewById(R.id.vehicleName);
//        TextView vehicleBrand=(TextView) convertView.findViewById(R.id.vehicleBrand);
//        TextView vehicleModel=(TextView) convertView.findViewById(R.id.vehicleModel);
        TextView date=(TextView) convertView.findViewById(R.id.date);
        TextView timeWindow=(TextView) convertView.findViewById(R.id.timeWindow);
//        TextView fType=(TextView) convertView.findViewById(R.id.fType);
//        TextView price=(TextView) convertView.findViewById(R.id.price);

        vehicleName.setText(orderHistoryModel.vehicleName);
//        vehicleBrand.setText(orderHistoryModel.vehicleBrand);
//        vehicleModel.setText(orderHistoryModel.vehicleModel);
        date.setText(orderHistoryModel.date);
        timeWindow.setText(orderHistoryModel.timeWindow);
//        fType.setText(orderHistoryModel.fType);
//        price.setText(orderHistoryModel.fPrice);




        return convertView;
    }
}
