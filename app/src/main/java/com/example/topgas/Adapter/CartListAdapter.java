package com.example.topgas.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.topgas.OrderHistoryModel;
import com.example.topgas.R;
import com.example.topgas.model.CartModel;

import java.util.ArrayList;
import java.util.List;

public class CartListAdapter extends ArrayAdapter<CartModel> {


    public CartListAdapter(Context context, ArrayList<CartModel> arrayList){
        super(context, R.layout.item_cart, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        CartModel cartModel  = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_cart,parent,false);

        }

        TextView vehicleName=(TextView) convertView.findViewById(R.id.vehicleName);
        TextView date=(TextView) convertView.findViewById(R.id.date);
        TextView timeWindow=(TextView) convertView.findViewById(R.id.timeWindow);

        vehicleName.setText(cartModel.vehicleName);
        date.setText(cartModel.date);
        timeWindow.setText(cartModel.timeWindow);

        return convertView;
    }
}
