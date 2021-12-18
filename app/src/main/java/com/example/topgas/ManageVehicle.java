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
import android.widget.Button;
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

public class ManageVehicle extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Intent intent;
    View header;
    ListView vehicleListview;
    RequestQueue requestQueue;
    TextView userNameOnMenu, userEmailOnMenu;
    ArrayList<VehicleModel> arrayList = new ArrayList<VehicleModel>();
    VehicleListAdapter vehicleListAdapter;
    String apiPath = "https://sxy5732.uta.cloud/topgas%20api/get_vehicle_details.php";
    Button checkbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_vehicle);

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
        checkbutton = (Button) findViewById(R.id.checkbutton);

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        vehicleListview = (ListView) findViewById(R.id.vehicle_listview);

        loadVehicleDataFromServer(this, this);

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
                        intent = new Intent(ManageVehicle.this, CustomerHome.class);
                        startActivity(intent);
                        finish();
                        break;


                    case R.id.nav_setting:
//                        Toast.makeText(getApplicationContext(),"nav_setting",Toast.LENGTH_SHORT).show();
                        intent = new Intent(ManageVehicle.this, ProfileSetting.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_vehicle:
                        break;
                    case R.id.nav_history:
//                        Toast.makeText(getApplicationContext(),"nav_history",Toast.LENGTH_SHORT).show();
                        intent = new Intent(ManageVehicle.this, OrderHistory.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_faq:
//                        Toast.makeText(getApplicationContext(),"nav_faq",Toast.LENGTH_SHORT).show();
                        intent = new Intent(ManageVehicle.this, FAQ.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_rateus:
                        Toast.makeText(getApplicationContext(),"nav_rateus",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_help:
                        intent = new Intent(ManageVehicle.this, HowTo.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_logout:
                        sharedPreferences = getSharedPreferences("MySharedPref_login", MODE_PRIVATE);
                        sharedPreferences.edit().clear().commit();
                        Toast.makeText(getApplicationContext(),"preferences cleared",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ManageVehicle.this, LoginPage.class);
                        startActivity(intent);
                        finish();
//                        Toast.makeText(getApplicationContext(),"nav_logout",Toast.LENGTH_SHORT).show();

                        break;
                }

                return true;
            }
        });


        vehicleListview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return false;
            }
        });

        checkbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"show message",Toast.LENGTH_SHORT);
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


    public void loadVehicleDataFromServer(Context mContext, Activity mAcitivity) {

        requestQueue = Volley.newRequestQueue(mContext);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                apiPath,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject;
                            JSONArray jsonArray;

                            jsonArray = new JSONArray(response);
                            jsonObject = jsonArray.getJSONObject(0);
                            String status = jsonObject.getString("status");
                            if (status.equals("true")) {
//                                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                                for (int index = 1; index < jsonArray.length(); index++) {
                                    jsonObject = jsonArray.getJSONObject(index);
                                    VehicleModel model = new VehicleModel();
                                    model.setId(jsonObject.getString("id"));
                                    model.setCustomer_email(jsonObject.getString("customer_email"));
                                    model.setBrand(jsonObject.getString("brand"));
                                    model.setName( jsonObject.getString("name"));
                                    model.setModel( jsonObject.getString("model"));
                                    model.setMake_year(jsonObject.getString("year"));
                                    model.setPlate_no(jsonObject.getString("plate_no"));
                                    arrayList.add(model);
                                }
                                vehicleListAdapter = new VehicleListAdapter(ManageVehicle.this, arrayList);
                                vehicleListview.setAdapter(vehicleListAdapter);
                                vehicleListview.setClickable(true);

                            } else {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "cannot call API" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email",UserDataHolder.getEmail());
                return params;
            }
        };;
        requestQueue.add(stringRequest);
    }


}