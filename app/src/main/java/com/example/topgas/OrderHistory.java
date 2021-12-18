package com.example.topgas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderHistory extends AppCompatActivity {
    ArrayList<OrderHistoryModel> arrayList = new ArrayList<OrderHistoryModel>();
    SharedPreferences sharedPreferences;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Intent intent;
    ListView orderHistoryListview;
    OrderHistoryListAdapter orderHistoryListAdapter;
    RequestQueue requestQueue;
    String apiPath = "https://sxy5732.uta.cloud/topgas%20api/get_order_history.php";
    View header;

    TextView userNameOnMenu, userEmailOnMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationIcon(R.drawable.menu);
        navigationView.setCheckedItem(R.id.nav_setting);


        header = navigationView.getHeaderView(0);
        userEmailOnMenu = (TextView) header.findViewById(R.id.user_email_on_menu);
        userNameOnMenu = (TextView) header.findViewById(R.id.user_name_on_menu);
        userEmailOnMenu.setText(UserDataHolder.getEmail()!=null?UserDataHolder.getEmail():"NA");
        userNameOnMenu.setText(UserDataHolder.getName()!=null?UserDataHolder.getName():"NA");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else{
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id= item.getItemId();

                switch(id){
                    case R.id.nav_home:
//                        Toast.makeText(getApplicationContext(),"nav_home",Toast.LENGTH_SHORT).show();
                        intent = new Intent(OrderHistory.this, CustomerHome.class);
                        startActivity(intent);
                        finish();
                        break;


                    case R.id.nav_setting:
//                        Toast.makeText(getApplicationContext(),"nav_setting",Toast.LENGTH_SHORT).show();
                        intent = new Intent(OrderHistory.this, ProfileSetting.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_vehicle:
                        intent = new Intent(OrderHistory.this, ManageVehicle.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_history:


                        break;
                    case R.id.nav_faq:

                        intent = new Intent(OrderHistory.this, FAQ.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_rateus:
                        Toast.makeText(getApplicationContext(),"nav_rateus",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_help:
                        intent = new Intent(OrderHistory.this, HowTo.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_logout:
                        sharedPreferences = getSharedPreferences("MySharedPref_login", MODE_PRIVATE);
                        sharedPreferences.edit().clear().commit();
                        Toast.makeText(getApplicationContext(),"preferences cleared",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OrderHistory.this, LoginPage.class);
                        startActivity(intent);
                        finish();
//                        Toast.makeText(getApplicationContext(),"nav_logout",Toast.LENGTH_SHORT).show();

                        break;
                }

                return true;
            }
        });

        orderHistoryListview = (ListView)findViewById(R.id.orderHistory_listview);
        loadOrderDataFromServer(this,this);

        orderHistoryListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(OrderHistory.this, OrderDetail.class);
                intent.putExtra("vehicleName",arrayList.get(position).vehicleName);
                intent.putExtra("vehicleBrand",arrayList.get(position).vehicleBrand);
                intent.putExtra("vehicleModel",arrayList.get(position).vehicleModel);
                intent.putExtra("date",arrayList.get(position).date);
                intent.putExtra("timeWindow",arrayList.get(position).timeWindow);
                intent.putExtra("fType",arrayList.get(position).fType);
                intent.putExtra("fPrice",arrayList.get(position).fPrice);
                intent.putExtra("lat",arrayList.get(position).lat);
                intent.putExtra("lng",arrayList.get(position).lng);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();

        }
    }

    public void loadOrderDataFromServer(Context mContext, Activity mAcitivity){

        requestQueue= Volley.newRequestQueue(mContext);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                apiPath,
                new Response.Listener<String>()  {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject;
                            JSONArray jsonArray;
                            Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                            jsonArray = new JSONArray(response);
                            jsonObject = jsonArray.getJSONObject(0);
                            String status = jsonObject.getString("status");
                            if(status.equals("true")){
                                for(int index = 1;index<jsonArray.length();index++ ){
                                    jsonObject = jsonArray.getJSONObject(index);
                                    OrderHistoryModel model = new OrderHistoryModel();
                                    model.setVehicleName(jsonObject.getString("vehicle_name"));
                                    model.setVehicleModel(jsonObject.getString("vehicle_model"));
                                    model.setVehicleBrand(jsonObject.getString("vehicle_brand"));
                                    model.setDate( jsonObject.getString("date"));
                                    model.setfType( jsonObject.getString("fuel_type"));
                                    model.setfPrice(jsonObject.getString("fuel_price"));
                                    model.setTimeWindow(jsonObject.getString("time_window"));
                                    model.setLat(jsonObject.getString("lat"));
                                    model.setLng(jsonObject.getString("lng"));
                                    arrayList.add(model);

                                }
                                orderHistoryListAdapter = new OrderHistoryListAdapter(OrderHistory.this, arrayList);
                                orderHistoryListview.setAdapter(orderHistoryListAdapter);
                                orderHistoryListview.setClickable(true);


                            }else{
                                Toast.makeText(getApplicationContext(),jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"cannot call API"+error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",UserDataHolder.getId());
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }
}