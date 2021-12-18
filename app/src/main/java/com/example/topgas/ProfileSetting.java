package com.example.topgas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileSetting extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Intent intent;
    EditText editTextName, editTextEmail, editTextContact, editTextAddress, editTextStreet, editTextZipcode, editTextCity, editTextState;
    ImageButton buttonEdit, buttonSave, buttonCancel;
    RequestQueue requestQueue;
    String apiPath = "https://sxy5732.uta.cloud/topgas%20api/update_user.php";
    View header;

    TextView userNameOnMenu, userEmailOnMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);
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


        editTextName=(EditText)findViewById(R.id.editTextName) ;
        editTextEmail=(EditText)findViewById(R.id.editTextEmail);
        editTextContact = (EditText)findViewById(R.id.editTextContact);
        editTextAddress = (EditText)findViewById(R.id.editTextAddress);
        editTextStreet = (EditText)findViewById(R.id.editTextStreet);
        editTextCity = (EditText)findViewById(R.id.editTextCity);
        editTextState = (EditText)findViewById(R.id.editTextState);
        editTextZipcode = (EditText)findViewById(R.id.editTextZipcode);

        buttonSave = (ImageButton) findViewById(R.id.buttonSave);
        buttonEdit = (ImageButton) findViewById(R.id.buttonEdit);
        buttonCancel = (ImageButton) findViewById(R.id.buttonCancel);

        buttonSave.setVisibility(View.GONE);
        buttonCancel.setVisibility(View.GONE);

        editTextName.setText(UserDataHolder.getName());
        editTextEmail.setText(UserDataHolder.getEmail());
        editTextContact.setText(UserDataHolder.getMobile_no());
        editTextAddress.setText(UserDataHolder.getAddress());
        editTextStreet.setText(UserDataHolder.getStreet());
        editTextCity.setText(UserDataHolder.getCity());
        editTextState.setText(UserDataHolder.getState());
        editTextZipcode.setText(UserDataHolder.getZip_code());

        editTextName.setEnabled(false);
        editTextEmail.setEnabled(false);
        editTextContact.setEnabled(false);
        editTextAddress.setEnabled(false);
        editTextStreet.setEnabled(false);
        editTextCity.setEnabled(false);
        editTextState.setEnabled(false);
        editTextZipcode.setEnabled(false);

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSave.setVisibility(View.VISIBLE);
                buttonCancel.setVisibility(View.VISIBLE);
                buttonEdit.setEnabled(false);
                buttonEdit.setBackgroundColor(getResources().getColor(R.color.GRAY) );
                buttonEdit.setBackground(getResources().getDrawable(R.drawable.roundborder));

                editTextName.setEnabled(true);
                editTextEmail.setEnabled(true);
                editTextContact.setEnabled(true);
                editTextAddress.setEnabled(true);
                editTextStreet.setEnabled(true);
                editTextCity.setEnabled(true);
                editTextState.setEnabled(true);
                editTextZipcode.setEnabled(true);

            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), UserDataHolder.getId()+" "+UserDataHolder.getEmail()+" "+UserDataHolder.getMobile_no()+" "+UserDataHolder.getName(), Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(getApplicationContext(), "information updated successfully", Toast.LENGTH_SHORT).show();
                                        UserDataHolder.setEmail(editTextEmail.getText().toString());
                                        UserDataHolder.setName(editTextName.getText().toString());
                                        UserDataHolder.setMobile_no(editTextContact.getText().toString());
                                        UserDataHolder.setAddress(editTextAddress.getText().toString());
                                        UserDataHolder.setStreet(editTextStreet.getText().toString());
                                        UserDataHolder.setCity(editTextCity.getText().toString());
                                        UserDataHolder.setState(editTextState.getText().toString());
                                        UserDataHolder.setZip_code(editTextZipcode.getText().toString());

                                    }else{
                                        String msg = jsonObject.getString("msg");
                                        Toast.makeText(getApplicationContext(), "information update failed", Toast.LENGTH_SHORT).show();

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
                        params.put("email", editTextEmail.getText().toString());
                        params.put("name", editTextName.getText().toString());
                        params.put("contact_no", editTextContact.getText().toString());

                        params.put("address", editTextAddress.getText().toString());
                        params.put("street", editTextStreet.getText().toString());
                        params.put("city", editTextCity.getText().toString());
                        params.put("state", editTextState.getText().toString());
                        params.put("zipcode", editTextZipcode.getText().toString());

                        params.put("id", UserDataHolder.getId());
                        return params;
                    }
                };

                requestQueue.add(stringRequest);



            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSave.setVisibility(View.GONE);
                buttonCancel.setVisibility(View.GONE);
                buttonEdit.setEnabled(true);
                buttonEdit.setBackgroundColor(getResources().getColor(R.color.BtRED) );
                buttonEdit.setBackground(getResources().getDrawable(R.drawable.roundborder));

                editTextName.setEnabled(false);
                editTextEmail.setEnabled(false);
                editTextContact.setEnabled(false);
                editTextAddress.setEnabled(false);
                editTextStreet.setEnabled(false);
                editTextCity.setEnabled(false);
                editTextState.setEnabled(false);
                editTextZipcode.setEnabled(false);
            }
        });





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
                        intent = new Intent(ProfileSetting.this, CustomerHome.class);
                        startActivity(intent);
                        finish();
                        break;


                    case R.id.nav_setting:

                        break;
                    case R.id.nav_vehicle:
                        break;
                    case R.id.nav_history:
//                        Toast.makeText(getApplicationContext(),"nav_history",Toast.LENGTH_SHORT).show();
                        intent = new Intent(ProfileSetting.this, OrderHistory.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_faq:
//                        Toast.makeText(getApplicationContext(),"nav_faq",Toast.LENGTH_SHORT).show();
                        intent = new Intent(ProfileSetting.this, FAQ.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_rateus:
                        Toast.makeText(getApplicationContext(),"nav_rateus",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_help:
                        intent = new Intent(ProfileSetting.this, HowTo.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_logout:
                        sharedPreferences = getSharedPreferences("MySharedPref_login", MODE_PRIVATE);
                        sharedPreferences.edit().clear().commit();
                        Toast.makeText(getApplicationContext(),"preferences cleared",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ProfileSetting.this, LoginPage.class);
                        startActivity(intent);
                        finish();
//                        Toast.makeText(getApplicationContext(),"nav_logout",Toast.LENGTH_SHORT).show();

                        break;
                }

                return true;
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
}