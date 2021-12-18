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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.topgas.databinding.ActivitySelectVehicleBinding;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddNewVehicle extends AppCompatActivity {


    SharedPreferences sharedPreferences;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Intent intent;
    Button addNewVehicle;
    RequestQueue requestQueue;
    String apiPath = "https://sxy5732.uta.cloud/topgas%20api/add_vehicle.php";
    String apiPathReloadVehicleList ="https://sxy5732.uta.cloud/topgas%20api/get_vehicle_details.php";
    Spinner sBrand, sFuel;
    EditText inputModel, inputPlate, inputMakeYear, inputVehicleName;

    View header;

    TextView userNameOnMenu, userEmailOnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_vehicle);

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

        String[] brandList = new String[]{"Hyundai", "Toyota", "Ford", "Dodge", "GM"};
        String[] fuelTypeList = new String[]{"Petrol", "Diesel", "Lead Petrol", "Lead Diesel"};
        sFuel = (Spinner) findViewById(R.id.inputFuel);
        sBrand = (Spinner) findViewById(R.id.inputBrand);
        inputVehicleName = (EditText)findViewById(R.id.inputVehicleName);
        sBrand = (Spinner) findViewById(R.id.inputBrand);
        inputModel = (EditText) findViewById(R.id.inputModel);
        inputPlate = (EditText) findViewById(R.id.inputPlate);
        inputMakeYear = (EditText) findViewById(R.id.inputMakeYear);
        ArrayAdapter<String> myAdapterBrand = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, brandList);
        sBrand.setAdapter(myAdapterBrand);

        ArrayAdapter<String> myAdapterFuel = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, fuelTypeList);
        sFuel.setAdapter(myAdapterFuel);

        addNewVehicle = (Button) findViewById(R.id.addNewVehicle);
        addNewVehicleOnClickListener(this,this);
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
                        intent = new Intent(AddNewVehicle.this, CustomerHome.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.nav_setting:
//                        Toast.makeText(getApplicationContext(),"nav_setting",Toast.LENGTH_SHORT).show();
                        intent = new Intent(AddNewVehicle.this, ProfileSetting.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.nav_vehicle:
//                        Toast.makeText(getApplicationContext(),"nav_vehicle",Toast.LENGTH_SHORT).show();
                        intent = new Intent(AddNewVehicle.this, ManageVehicle.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_history:
//                        Toast.makeText(getApplicationContext(),"nav_history",Toast.LENGTH_SHORT).show();
                        intent = new Intent(AddNewVehicle.this, OrderHistory.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_faq:
                        intent = new Intent(AddNewVehicle.this, FAQ.class);
                        startActivity(intent);
                        finish();
//                        Toast.makeText(getApplicationContext(),"nav_faq",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_rateus:
                        Toast.makeText(getApplicationContext(), "nav_rateus", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_help:
                        intent = new Intent(AddNewVehicle.this, HowTo.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_logout:
                        sharedPreferences = getSharedPreferences("MySharedPref_login", MODE_PRIVATE);
                        sharedPreferences.edit().clear().commit();
                        Toast.makeText(getApplicationContext(), "preferences cleared", Toast.LENGTH_SHORT).show();
                        intent = new Intent(AddNewVehicle.this, LoginPage.class);
                        startActivity(intent);
                        finish();
//                        Toast.makeText(getApplicationContext(),"nav_logout",Toast.LENGTH_SHORT).show();

                        break;
                }

                return true;
            }
        });

    }


    public void addNewVehicleOnClickListener(Context mContext, Activity mActivity){
        addNewVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestQueue = Volley.newRequestQueue(mContext);
                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        apiPath,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    if(status.equals("true")){
                                        fillVehicleListAdapter( mContext,  mActivity);
                                        Toast.makeText(getApplicationContext(), jsonObject.toString(), Toast.LENGTH_SHORT).show();
                                        Intent registerIntent = new Intent(AddNewVehicle.this, SelectVehicle.class);
                                        startActivity(registerIntent);
                                        finish();
                                    }else{
                                        String msg = jsonObject.getString("msg");
                                        Toast.makeText(getApplicationContext(), jsonObject.toString(), Toast.LENGTH_SHORT).show();

                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "cannot call API", Toast.LENGTH_SHORT).show();

                            }
                        }
                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("brand", sBrand.getSelectedItem().toString());
                        params.put("model", inputModel.getText().toString());
                        params.put("year", inputMakeYear.getText().toString());
                        params.put("plate_no", inputPlate.getText().toString());
                        params.put("vehicle_name",inputVehicleName.getText().toString());
                        params.put("email",UserDataHolder.getEmail());
                        return params;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
    }


    public void fillVehicleListAdapter(Context mContext, Activity mActivity){
        requestQueue= Volley.newRequestQueue(mContext);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                apiPathReloadVehicleList,
                new Response.Listener<String>()  {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject;
                            JSONArray jsonArray;

                            jsonArray = new JSONArray(response);
                            jsonObject = jsonArray.getJSONObject(0);
                            String status = jsonObject.getString("status");

                            VehiclesDataHolder.setVehicleDetails(null);
                            ArrayList<VehicleDataHolder> vehicleDataHolderList= new ArrayList<>();

                            if(status.equals("true")){
                                for(int index = 1;index<jsonArray.length();index++ ){
                                    jsonObject = jsonArray.getJSONObject(index);
                                    VehicleDataHolder vehicleDataHolder= new VehicleDataHolder();
                                    vehicleDataHolder.setId(jsonObject.getString("id"));
                                    vehicleDataHolder.setCustomer_email(jsonObject.getString("customer_email"));
                                    vehicleDataHolder.setBrand(jsonObject.getString("brand"));
                                    vehicleDataHolder.setName(jsonObject.getString("name"));
                                    vehicleDataHolder.setModel(jsonObject.getString("model"));
                                    vehicleDataHolder.setMake_year(jsonObject.getString("make_year"));
                                    vehicleDataHolder.setPlate_no(jsonObject.getString("plate_no"));
                                    vehicleDataHolderList.add(vehicleDataHolder);
                                }
                                VehiclesDataHolder.setVehicleDetails(vehicleDataHolderList);
                                Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
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
                params.put("email",UserDataHolder.getEmail());
                return params;
            }
        };;
        requestQueue.add(stringRequest);
    }
}