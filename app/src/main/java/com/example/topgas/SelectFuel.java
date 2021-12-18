package com.example.topgas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.topgas.databinding.ActivitySelectFuelBinding;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SelectFuel extends AppCompatActivity {

    ArrayList<FuelModel> arrayList = new ArrayList<FuelModel>();
    FuelListAdapter fuelListAdapter;
    SharedPreferences sharedPreferences;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Intent intent;
    Button cartCounter;;
    int cartSize=0;
    ListView fuelListview;
    RequestQueue requestQueue;
    String apiPath = "https://sxy5732.uta.cloud/topgas%20api/get_fuel_data.php";
    Bundle extras;
    View header;

    TextView userNameOnMenu, userEmailOnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_fuel);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_viewfuel);
        toolbar = (Toolbar) findViewById(R.id.toolbarfuel);
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

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        fuelListview = (ListView)findViewById(R.id.fuel_listview);

        loadFuelDataFromServer(this,this);

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
                        intent = new Intent(SelectFuel.this, CustomerHome.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.nav_setting:
//                        Toast.makeText(getApplicationContext(),"nav_setting",Toast.LENGTH_SHORT).show();
                        intent = new Intent(SelectFuel.this, ProfileSetting.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.nav_vehicle:
//                        Toast.makeText(getApplicationContext(),"nav_vehicle",Toast.LENGTH_SHORT).show();
                        intent = new Intent(SelectFuel.this, ManageVehicle.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_history:
//                        Toast.makeText(getApplicationContext(),"nav_history",Toast.LENGTH_SHORT).show();
                        intent = new Intent(SelectFuel.this, OrderHistory.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_faq:
                        intent = new Intent(SelectFuel.this, FAQ.class);
                        startActivity(intent);
                        finish();
//                        Toast.makeText(getApplicationContext(),"nav_faq",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_rateus:
                        Toast.makeText(getApplicationContext(),"nav_rateus",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_help:
                        intent = new Intent(SelectFuel.this, HowTo.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_logout:
                        sharedPreferences = getSharedPreferences("MySharedPref_login", MODE_PRIVATE);
                        sharedPreferences.edit().clear().commit();
                        Toast.makeText(getApplicationContext(),"preferences cleared",Toast.LENGTH_SHORT).show();
                        intent = new Intent(SelectFuel.this, LoginPage.class);
                        startActivity(intent);
                        finish();
//                        Toast.makeText(getApplicationContext(),"nav_logout",Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        fuelListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                extras = getIntent().getExtras();

                intent = new Intent(SelectFuel.this, SummaryPage.class);
                intent.putExtra("vehicle_id",extras.getString("vehicle_id"));
                intent.putExtra("vehicle_name",extras.getString("vehicle_name"));
                intent.putExtra("vehicle_brand",extras.getString("vehicle_brand"));
                intent.putExtra("vehicle_model",extras.getString("vehicle_model"));
                intent.putExtra("date",extras.getString("date"));
                intent.putExtra("stime",extras.getString("stime"));
                intent.putExtra("etime",extras.getString("etime"));
                intent.putExtra("lat",extras.getDouble("lat"));
                intent.putExtra("lng",extras.getDouble("lng"));
                intent.putExtra("fType",arrayList.get(position).getFuelType());
                intent.putExtra("fPrice",arrayList.get(position).getFuelPrice().toString());
                intent.putExtra("fId",""+arrayList.get(position).getId());
                startActivity(intent);

//                make call to the summary page here.
//                intent = new Intent(SelectFuel.this,SelectDateTime.class);
//                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart,menu);
        RelativeLayout v=(RelativeLayout)menu.findItem(R.id.cart).getActionView();
        cartCounter=(Button)v.findViewById(R.id.counter);
        cartCounter.setText("0");//(String.valueOf(cartSize));
        cartCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartCounter.setText(String.valueOf(++cartSize));
            }
        });
        return true;//super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cart:
                Toast.makeText(getApplicationContext(),"cart selected",Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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

   public void loadFuelDataFromServer(Context mContext, Activity mAcitivity){

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

                           jsonArray = new JSONArray(response);
                           jsonObject = jsonArray.getJSONObject(0);
                           String status = jsonObject.getString("status");
                           if(status.equals("true")){
                               for(int index = 1;index<jsonArray.length();index++ ){
                                   jsonObject = jsonArray.getJSONObject(index);
                                   FuelModel obj = new FuelModel( jsonObject.getString("fuel_type"),
                                           Float.parseFloat(jsonObject.getString("fuel_price")),
                                           Integer.parseInt(jsonObject.getString("id")));
                                   arrayList.add(obj);
                               }
                               fuelListAdapter = new FuelListAdapter(SelectFuel.this, arrayList);
                               fuelListview.setAdapter(fuelListAdapter);
                               fuelListview.setClickable(true);


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
       );
       requestQueue.add(stringRequest);
    }
}