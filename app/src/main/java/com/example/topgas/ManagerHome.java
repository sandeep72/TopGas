package com.example.topgas;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManagerHome extends AppCompatActivity {

    ArrayList<OrderHistoryModel> arrayList = new ArrayList<OrderHistoryModel>();
    SharedPreferences sharedPreferences;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Intent intent;
    ListView orderHistoryListview;
    OrderHistoryListAdapter orderHistoryListAdapter;
    RequestQueue requestQueue;
    String apiPath = "https://sxy5732.uta.cloud/topgas%20api/get_all_orders.php";
    View header;



    TextView userNameOnMenu, userEmailOnMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_home);

//        tabLayout = findViewById(R.id.tabLayout);
//        viewPager = findViewById(R.id.viewPager);
//
//        tabLayout.addTab(tabLayout.newTab().setText("Active Orders"));
//        tabLayout.addTab(tabLayout.newTab().setText("Past Orders"));
//
//        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
//            @NonNull
//            @Override
//            public Fragment getItem(int position) {
//                switch (position){
//                    case 0:
//                        ActiveOrders activeOrders = new ActiveOrders();
//                        return activeOrders;
//
//                    case 1:
//                        PastOrders pastOrders = new PastOrders();
//                        return pastOrders;
//
//                    default:
//                        return null;
//
//                }
//            }
//
//            @Override
//            public int getCount()
//            {
//                return tabLayout.getTabCount();
//            }
//
//        });
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab)
//            {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//
//    }

//        drawerLayout = findViewById(R.id.drawer_layout);
//        navigationView = findViewById(R.id.nav_view);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        navigationView.bringToFront();
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
//        toolbar.setNavigationIcon(R.drawable.menu);
//        navigationView.setCheckedItem(R.id.nav_setting);

//        header = navigationView.getHeaderView(0);
//        userEmailOnMenu = (TextView) header.findViewById(R.id.user_email_on_menu);
//        userNameOnMenu = (TextView) header.findViewById(R.id.user_name_on_menu);
//        userEmailOnMenu.setText(UserDataHolder.getEmail()!=null?UserDataHolder.getEmail():"NA");
//        userNameOnMenu.setText(UserDataHolder.getName()!=null?UserDataHolder.getName():"NA");

        orderHistoryListview = (ListView)findViewById(R.id.orderHistory_listview);
        loadOrderDataFromServer(this,this);
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
                                    arrayList.add(model);

                                }
                                orderHistoryListAdapter = new OrderHistoryListAdapter(ManagerHome.this, arrayList);
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

    @Override
    public void onBackPressed() {

        sharedPreferences = getSharedPreferences("MySharedPref_login", MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();

        super.onBackPressed();
    }
}