package com.example.topgas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class OrderDetail extends AppCompatActivity implements OnMapReadyCallback{
    TextView vehicleName, vehicleBrand, vehicleModel , date, timeWindow, fType, price;
    Bundle extras;
    SupportMapFragment mapFragment;
    FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap mMap;
    LatLng Arlington;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        extras = getIntent().getExtras();

         vehicleName=(TextView) findViewById(R.id.vehicleName);
         vehicleBrand=(TextView) findViewById(R.id.vehicleBrand);
         vehicleModel=(TextView) findViewById(R.id.vehicleModel);
         date=(TextView) findViewById(R.id.date);
         timeWindow=(TextView) findViewById(R.id.timeWindow);
         fType=(TextView) findViewById(R.id.fType);
         price=(TextView) findViewById(R.id.price);

        vehicleName.setText(extras.getString("vehicleName"));
        vehicleBrand.setText(extras.getString("vehicleBrand"));
        vehicleModel.setText(extras.getString("vehicleModel"));
        date.setText(extras.getString("date"));
        timeWindow.setText(extras.getString("timeWindow"));
        fType.setText(extras.getString("fType"));
        price.setText(extras.getString("fPrice"));

        Arlington  = new LatLng(Float.parseFloat(extras.getString("lat")), Float.parseFloat(extras.getString("lng")));
        Toast.makeText(getApplicationContext(),Float.parseFloat(extras.getString("lat"))+" "+ Float.parseFloat(extras.getString("lng")),Toast.LENGTH_SHORT).show();
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap= googleMap;
        marker = mMap.addMarker((new MarkerOptions()).position(Arlington));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Arlington));
        mMap.getUiSettings().setZoomControlsEnabled(true);
//        https://www.youtube.com/watch?v=_Sx45CbBsoA
        mMap.setMapType(mMap.MAP_TYPE_SATELLITE);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Arlington,18));

    }
}