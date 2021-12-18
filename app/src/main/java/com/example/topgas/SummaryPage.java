package com.example.topgas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SummaryPage extends AppCompatActivity {
    TextView vehicleName, vehicleBrand, vehicleModel, date, sTime, eTime, fType, price;
    Button addToCart, confirm;
    Bundle extras;
    RequestQueue requestQueue;
    String apiPath = "https://sxy5732.uta.cloud/topgas%20api/addToCart.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_page);

        vehicleName=(TextView) findViewById(R.id.vehicleName);
        vehicleBrand=(TextView) findViewById(R.id.vehicleBrand);
        vehicleModel=(TextView) findViewById(R.id.vehicleModel);

        date=(TextView) findViewById(R.id.date);
        sTime=(TextView) findViewById(R.id.sTime);
        eTime=(TextView) findViewById(R.id.eTime);
        fType=(TextView) findViewById(R.id.fType);
        price=(TextView) findViewById(R.id.price);

        addToCart = (Button) findViewById(R.id.addToCart);
        confirm = (Button) findViewById(R.id.confirm);

        extras = getIntent().getExtras();
        vehicleName.setText(extras.getString("vehicle_name"));
        vehicleBrand.setText(extras.getString("vehicle_brand"));
        vehicleModel.setText(extras.getString("vehicle_model"));
        date.setText(extras.getString("date"));
        sTime.setText(extras.getString("stime"));
        eTime.setText(extras.getString("etime"));
        fType.setText(extras.getString("fType"));
        price.setText("$ "+extras.getString("fPrice"));

        Toast.makeText(getApplicationContext(), UserDataHolder.getEmail(), Toast.LENGTH_SHORT).show();


        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        apiPath,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    if(status.equals("true")){
                                        Toast.makeText(getApplicationContext(), "Service request added to cart", Toast.LENGTH_SHORT).show();
                                        Intent registerIntent = new Intent(SummaryPage.this, CustomerHome.class);
                                        startActivity(registerIntent);
                                        finishAffinity();
                                        finish();
                                    }else{
                                        String msg = jsonObject.getString("msg");
                                        Toast.makeText(getApplicationContext(), "failure, please try in sometime", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "failure, please try in sometime", Toast.LENGTH_SHORT).show();

                            }
                        }
                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("customer_id", UserDataHolder.getId());
                        params.put("vehicle_id", extras.getString("vehicle_id"));
                        params.put("fuel_id", extras.getString("fId"));
                        params.put("date", extras.getString("date"));
                        params.put("time_window", extras.getString("stime")+"-" + extras.getString("etime"));
                        params.put("order_status", "cart");
                        params.put("latitude", ""+extras.getDouble("lat"));
                        params.put("longitude", ""+extras.getDouble("lng"));
                        return params;
                    }
                };
                requestQueue.add(stringRequest);

            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        apiPath,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    if(status.equals("true")){
                                        Toast.makeText(getApplicationContext(), "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                                        Intent registerIntent = new Intent(SummaryPage.this, CustomerHome.class);
                                        startActivity(registerIntent);
                                        finishAffinity();
                                        finish();
                                    }else{
                                        String msg = jsonObject.getString("msg");
                                        Toast.makeText(getApplicationContext(), "failure, please try in sometime", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "failure, please try in sometime", Toast.LENGTH_SHORT).show();

                            }
                        }
                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("customer_id", UserDataHolder.getId());
                        params.put("vehicle_id", extras.getString("vehicle_id"));
                        params.put("fuel_id", extras.getString("fId"));
                        params.put("date", extras.getString("date"));
                        params.put("time_window", extras.getString("stime")+"-" + extras.getString("etime"));
                        params.put("order_status", "confirm");
                        params.put("latitude", ""+extras.getDouble("lat"));
                        params.put("longitude", ""+extras.getDouble("lng"));
                        return params;
                    }
                };
                requestQueue.add(stringRequest);

            }
        });

    }
}