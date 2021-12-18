package com.example.topgas;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SelectDateTime extends AppCompatActivity {
    TextView locationDisplay;
    RecyclerView recyclerView;
    ArrayList<VehicleModel> arrayList = new ArrayList<VehicleModel>();
    VehicleListAdapter adapter;
    SharedPreferences sharedPreferences;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Intent intent;
    final Calendar myCalendar = Calendar.getInstance();
    Button confirmDateTime;
    EditText edittextDate;
    RadioGroup radioGroup;
    int TS_index;
    String sTime[] = new String[]{"11:00","13:00","15:00","17:00","19:00"};
    String eTime[] = new String[]{"13:00","15:00","17:00","19:00","21:00"};
    Bundle extras;

    View header;

    TextView userNameOnMenu, userEmailOnMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date_time);

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
        confirmDateTime = (Button)findViewById(R.id.confirm_dateTime);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);

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
                        intent = new Intent(SelectDateTime.this, CustomerHome.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.nav_setting:
//                        Toast.makeText(getApplicationContext(),"nav_setting",Toast.LENGTH_SHORT).show();
                        intent = new Intent(SelectDateTime.this, ProfileSetting.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.nav_vehicle:
//                        Toast.makeText(getApplicationContext(),"nav_vehicle",Toast.LENGTH_SHORT).show();
                        intent = new Intent(SelectDateTime.this, ManageVehicle.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_history:
//                        Toast.makeText(getApplicationContext(),"nav_history",Toast.LENGTH_SHORT).show();
                        intent = new Intent(SelectDateTime.this, OrderHistory.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_faq:
                        intent = new Intent(SelectDateTime.this, FAQ.class);
                        startActivity(intent);
                        finish();
//                        Toast.makeText(getApplicationContext(),"nav_faq",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_rateus:
                        Toast.makeText(getApplicationContext(),"nav_rateus",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_help:
                        intent = new Intent(SelectDateTime.this, HowTo.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.nav_logout:
                        sharedPreferences = getSharedPreferences("MySharedPref_login", MODE_PRIVATE);
                        sharedPreferences.edit().clear().commit();
                        Toast.makeText(getApplicationContext(),"preferences cleared",Toast.LENGTH_SHORT).show();
                        intent = new Intent(SelectDateTime.this, LoginPage.class);
                        startActivity(intent);
                        finish();
//                        Toast.makeText(getApplicationContext(),"nav_logout",Toast.LENGTH_SHORT).show();

                        break;
                }

                return true;
            }
        });


        edittextDate= (EditText) findViewById(R.id.date);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        edittextDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog datePickerDialog = new DatePickerDialog(SelectDateTime.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

            }
        });


        confirmDateTime.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdformat = new SimpleDateFormat("MM/dd/yyyy");
                try {
                    Date inputDate = sdformat.parse(edittextDate.getText().toString());
                    Date today = new Date();
                    Toast.makeText(getApplicationContext(), sdformat.format(today)+""+sdformat.format(inputDate), Toast.LENGTH_SHORT).show();
//                    if(sdformat.format(today).toString().equals(sdformat.format(inputDate).toString())){
                    if(!inputDate.before(today)){
                        int radioButtonId = radioGroup.getCheckedRadioButtonId();
                        if(radioButtonId == -1){
                            Toast.makeText(getApplicationContext()," Please select time from available slot", Toast.LENGTH_SHORT).show();
                        }else{
                            RadioButton radioButton = (RadioButton) findViewById(radioButtonId);
                            if(radioButton.getText().toString().equals("11 AM - 1 PM")){
                                TS_index = 0;
                            }else if(radioButton.getText().toString().equals("1 PM - 3 PM")){
                                TS_index = 1;
                            }else if(radioButton.getText().toString().equals("3 PM - 5 PM")){
                                TS_index = 2;
                            }else if(radioButton.getText().toString().equals("5 PM - 7 PM")){
                                TS_index = 3;
                            }else if(radioButton.getText().toString().equals("7 PM - 9 PM")){
                                TS_index = 4;
                            }

                            intent = new Intent(SelectDateTime.this,SelectFuel.class);
                            extras = getIntent().getExtras();
                            intent.putExtra("vehicle_id",extras.getString("vehicle_id"));
                            intent.putExtra("vehicle_name",extras.getString("vehicle_name"));
                            intent.putExtra("vehicle_brand",extras.getString("vehicle_brand"));
                            intent.putExtra("vehicle_model",extras.getString("vehicle_model"));
                            intent.putExtra("lat",extras.getDouble("lat"));
                            intent.putExtra("lng",extras.getDouble("lng"));
                            intent.putExtra("date",edittextDate.getText().toString());
                            intent.putExtra("stime",sTime[TS_index]);
                            intent.putExtra("etime",eTime[TS_index]);

                            startActivity(intent);
                        }

                    }else{
                        Toast.makeText(getApplicationContext(), edittextDate.getText(), Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                Toast.makeText(getApplicationContext(), edittextDate.getText(), Toast.LENGTH_SHORT).show();
//                intent = new Intent(SelectDateTime.this,SelectFuel.class);
//                intent.putExtra("time",edittextDate.getText());
//
//                startActivity(intent);
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

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edittextDate.setText(sdf.format(myCalendar.getTime()));
    }
}