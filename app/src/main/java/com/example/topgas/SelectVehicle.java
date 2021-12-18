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
import android.widget.Button;


import android.widget.ImageButton;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectVehicle extends AppCompatActivity {

    ArrayList<VehicleModel> arrayList = new ArrayList<VehicleModel>();
    VehicleListAdapter vehicleListAdapter;
    SharedPreferences sharedPreferences;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Intent intent;
    ImageButton addVehicle;

    ListView vehicleListview;
    RequestQueue requestQueue;
    String apiPath = "https://sxy5732.uta.cloud/topgas%20api/get_vehicle_details.php";
    Bundle extras;
    View header;

    TextView userNameOnMenu, userEmailOnMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_vehicle);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationIcon(R.drawable.menu);
        navigationView.setCheckedItem(R.id.nav_setting);


        header = navigationView.getHeaderView(0);
        userEmailOnMenu = (TextView) header.findViewById(R.id.user_email_on_menu);
        userNameOnMenu = (TextView) header.findViewById(R.id.user_name_on_menu);
        userEmailOnMenu.setText(UserDataHolder.getEmail()!=null?UserDataHolder.getEmail():"NA");
        userNameOnMenu.setText(UserDataHolder.getName()!=null?UserDataHolder.getName():"NA");


        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        addVehicle = (ImageButton) findViewById(R.id.addVehicle);

        vehicleListview = (ListView) findViewById(R.id.vehicle_listview);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            Double lat = extras.getDouble("lat");
            Double lng = extras.getDouble("lng");
            //check if there is already a vehicle info added or else redirect to add vehicle page or a similar page
//            locationDisplay = (TextView) findViewById(R.id.locationDisplay);
//            locationDisplay.setText(""+extras.getDouble("lat")+", "+extras.getDouble("lng"));
        }

        loadVehicleDataFromServer(this, this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                switch (id) {
                    case R.id.nav_home:
//                        Toast.makeText(getApplicationContext(),"nav_home",Toast.LENGTH_SHORT).show();
                        intent = new Intent(SelectVehicle.this, CustomerHome.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.nav_setting:
//                        Toast.makeText(getApplicationContext(),"nav_setting",Toast.LENGTH_SHORT).show();
                        intent = new Intent(SelectVehicle.this, ProfileSetting.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.nav_vehicle:
//                        Toast.makeText(getApplicationContext(),"nav_vehicle",Toast.LENGTH_SHORT).show();
                        intent = new Intent(SelectVehicle.this, ManageVehicle.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_history:
//                        Toast.makeText(getApplicationContext(),"nav_history",Toast.LENGTH_SHORT).show();
                        intent = new Intent(SelectVehicle.this, OrderHistory.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_faq:
                        intent = new Intent(SelectVehicle.this, FAQ.class);
                        startActivity(intent);
                        finish();
//                        Toast.makeText(getApplicationContext(),"nav_faq",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_rateus:
//                        Toast.makeText(getApplicationContext(), "nav_rateus", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_help:
                        intent = new Intent(SelectVehicle.this, HowTo.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_logout:
                        sharedPreferences = getSharedPreferences("MySharedPref_login", MODE_PRIVATE);
                        sharedPreferences.edit().clear().commit();
//                        Toast.makeText(getApplicationContext(), "preferences cleared", Toast.LENGTH_SHORT).show();
                        intent = new Intent(SelectVehicle.this, LoginPage.class);
                        startActivity(intent);
                        finish();
//                        Toast.makeText(getApplicationContext(),"nav_logout",Toast.LENGTH_SHORT).show();

                        break;
                }

                return true;
            }
        });

        vehicleListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), arrayList.get(position).getModel(), Toast.LENGTH_LONG).show();
                intent = new Intent(SelectVehicle.this, SelectDateTime.class);
                intent.putExtra("vehicle_id",arrayList.get(position).id);
                intent.putExtra("vehicle_name",arrayList.get(position).name);
                intent.putExtra("vehicle_brand",arrayList.get(position).brand);
                intent.putExtra("vehicle_model",arrayList.get(position).model);
                intent.putExtra("lat",extras.getDouble("lat"));
                intent.putExtra("lng",extras.getDouble("lng"));
                startActivity(intent);
            }
        });
        addVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addNewVehicleIntent = new Intent(SelectVehicle.this, AddNewVehicle.class);
                startActivity(addNewVehicleIntent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
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
                                vehicleListAdapter = new VehicleListAdapter(SelectVehicle.this, arrayList);
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