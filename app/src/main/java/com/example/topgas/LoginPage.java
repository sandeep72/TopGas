package com.example.topgas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginPage extends AppCompatActivity {

    EditText userName;
    EditText password;
    Button singinButton;
    TextView registgerUser;
    TextView forgotPassword;
    SharedPreferences sharedPreferences; //= getSharedPreferences("MySharedPref", MODE_PRIVATE);
    SharedPreferences.Editor myEdit; //= sharedPreferences.edit();
    private String apiPath = "https://sxy5732.uta.cloud/topgas%20api/customer_login.php";
    RequestQueue requestQueue;
    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);
        userName = (EditText)findViewById(R.id.inputName);
        password = (EditText) findViewById(R.id.inputPassword);
        singinButton = (Button) findViewById(R.id.loginButton);
        registgerUser = (TextView) findViewById(R.id.register);
        forgotPassword = (TextView)findViewById(R.id.forgotPassword);
        forgotPasswordClickListener();
        signInClickListener(this,this);
        registerClickListener();
    }

    public void signInClickListener(Context mContext, Activity mActivity){
        singinButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                if(!userName.getText().toString().equals("") && !password.getText().toString().equals("")) {
                    if(password.getText().toString().matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}") && password.length() >= 8 ){

                        apiPath = apiPath+"?email="+ userName.getText().toString()+"&password="+password.getText().toString();
                        requestQueue=Volley.newRequestQueue(mContext);
                        StringRequest stringRequest = new StringRequest(
                                Request.Method.POST,
                                apiPath,
                                new Response.Listener<String>()  {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            userType="customer";
                                            JSONObject jsonObject;
                                            JSONArray jsonArray,jsonArrayUser, jsonArrayVehicle;

                                            jsonArray = new JSONArray(response);
                                            jsonObject = jsonArray.getJSONObject(0);
                                            String status = jsonObject.getString("status");
//                                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                                            if(status.equals("true")){
                                                sharedPreferences = getSharedPreferences("MySharedPref_login", MODE_PRIVATE);
                                                myEdit = sharedPreferences.edit();
                                                myEdit.putString("email", userName.getText().toString());
                                                myEdit.putString("password", password.getText().toString());
                                                myEdit.apply();

                                                jsonObject = jsonArray.getJSONObject(1);
                                                jsonArrayUser = jsonObject.getJSONArray("user");
//                                                Toast.makeText(getApplicationContext(), jsonArrayUser.toString(), Toast.LENGTH_SHORT).show();
                                                for(int index = 0;index<jsonArrayUser.length();index++ ){
                                                    jsonObject = jsonArrayUser.getJSONObject(index);
                                                    userType = jsonObject.getString("type");
                                                    UserDataHolder.setId(jsonObject.getString("id"));
                                                    UserDataHolder.setEmail(jsonObject.getString("email"));
                                                    UserDataHolder.setName(jsonObject.getString("name"));
                                                    UserDataHolder.setMobile_no(jsonObject.getString("mobile_no"));
                                                    UserDataHolder.setAddress(jsonObject.getString("address"));
                                                    UserDataHolder.setStreet(jsonObject.getString("street"));
                                                    UserDataHolder.setCity(jsonObject.getString("city"));
                                                    UserDataHolder.setState(jsonObject.getString("state"));
                                                    UserDataHolder.setZip_code(jsonObject.getString("zip_code"));
                                                    UserDataHolder.setType(jsonObject.getString("type"));
                                                }

                                                jsonObject = jsonArray.getJSONObject(2);
                                                jsonArrayVehicle = jsonObject.getJSONArray("vehicle");
//                                                Toast.makeText(getApplicationContext(), jsonArrayVehicle.toString(), Toast.LENGTH_SHORT).show();
                                                jsonObject = jsonArrayVehicle.getJSONObject(0);

                                                VehiclesDataHolder.setVehicleDetails(null);
                                                if(jsonObject.getInt("vehicle_count") != 0) {
                                                    Toast.makeText(getApplicationContext(), "vehicle count: "+jsonObject.getInt("vehicle_count"), Toast.LENGTH_SHORT).show();
                                                    ArrayList<VehicleDataHolder> vehicleDataHolderList= new ArrayList<>();

                                                    for (int index = 1; index < jsonArrayVehicle.length(); index++) {
                                                        jsonObject = jsonArrayVehicle.getJSONObject(index);
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
                                                }
//                                                Toast.makeText(getApplicationContext(),"user type : "+UserDataHolder.getType(),Toast.LENGTH_LONG).show();
                                                if(userType.equals("manager")){
                                                    Intent signInIntent = new Intent(LoginPage.this, ManagerHome.class);
                                                    startActivity(signInIntent);
                                                    finish();
                                                }else if(userType.equals("customer")){
                                                    Intent signInIntent = new Intent(LoginPage.this, CustomerHome.class);
                                                    startActivity(signInIntent);
                                                    finish();
                                                }else{
                                                    Toast.makeText(getApplicationContext(),"invalid user type maintained in DB for user, please contact support",Toast.LENGTH_LONG).show();
                                                }
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
                    else{
                        Toast.makeText(getApplicationContext(),"Please does not meets criteria",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Please provide login details",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void forgotPasswordClickListener(){
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotPasswordIntent = new Intent(LoginPage.this, ForgotPassword.class);
                startActivity(forgotPasswordIntent);
            }
        });
    }

    public void registerClickListener(){
        registgerUser.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent registerIntent = new Intent(LoginPage.this, Register.class);
                startActivity(registerIntent);
            }
        });
    }


    //making an API call and getting the return.
//    private class ServiceStubAsyncTask extends AsyncTask<Void, Void, Void> {
//
//        private Context mContext;
//        private Activity mActivity;
////        RequestQueue requestQueue;
//
//        public ServiceStubAsyncTask(Context context, Activity activity) {
//            mContext = context;
//            mActivity = activity;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Void doInBackground(Void... arg0) {
//
//            apiPath = apiPath+"?email="+ userName.getText().toString()+"&password="+password.getText().toString();
//            requestQueue=Volley.newRequestQueue(mContext);
//            StringRequest stringRequest = new StringRequest(
//                    Request.Method.POST,
//                    apiPath,
//                    new Response.Listener<String>()  {
//                        @Override
//                        public void onResponse(String response) {
//                            try {
//                                String userType="customer";
//                                JSONObject jsonObject;
//                                JSONArray jsonArray,jsonArrayUser, jsonArrayVehicle;
//
//                                jsonArray = new JSONArray(response);
//                                jsonObject = jsonArray.getJSONObject(0);
//                                String status = jsonObject.getString("status");
//                                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
//                                if(status.equals("true")){
//                                    sharedPreferences = getSharedPreferences("MySharedPref_login", MODE_PRIVATE);
//                                    myEdit = sharedPreferences.edit();
//                                    myEdit.putString("email", userName.getText().toString());
//                                    myEdit.putString("password", password.getText().toString());
//                                    myEdit.apply();
//
//                                    jsonObject = jsonArray.getJSONObject(1);
//                                    jsonArrayUser = jsonObject.getJSONArray("user");
//                                    Toast.makeText(getApplicationContext(), jsonArrayUser.toString(), Toast.LENGTH_SHORT).show();
//                                    for(int index =1;index<jsonArrayUser.length();index++ ){
//                                        jsonObject = jsonArrayUser.getJSONObject(index);
////                                        userType = jsonObject.getString("type");
//                                        UserDataHolder.setId(jsonObject.getString("id"));
//                                        UserDataHolder.setEmail(jsonObject.getString("email"));
//                                        UserDataHolder.setName(jsonObject.getString("name"));
//                                        UserDataHolder.setMobile_no(jsonObject.getString("mobile_no"));
//                                        UserDataHolder.setAddress(jsonObject.getString("address"));
//                                        UserDataHolder.setStreet(jsonObject.getString("street"));
//                                        UserDataHolder.setCity(jsonObject.getString("city"));
//                                        UserDataHolder.setState(jsonObject.getString("state"));
//                                        UserDataHolder.setZip_code(jsonObject.getString("zip_code"));
//                                        UserDataHolder.setType(jsonObject.getString("type"));
//                                    }
//
//                                    jsonObject = jsonArray.getJSONObject(2);
//                                    jsonArrayVehicle = jsonObject.getJSONArray("vehicle");
//                                    Toast.makeText(getApplicationContext(), jsonArrayVehicle.toString(), Toast.LENGTH_SHORT).show();
//                                    jsonObject = jsonArrayVehicle.getJSONObject(0);
//                                    if(jsonObject.getInt("vehicle_count") != 0) {
//                                        Toast.makeText(getApplicationContext(), "vehicle count: "+jsonObject.getInt("vehicle_count"), Toast.LENGTH_SHORT).show();
//                                        ArrayList<UserDataHolder.VehicleDataHolder> vehicleDataHolderList= new ArrayList<>();
//                                        UserDataHolder.VehicleDataHolder vehicleDataHolder = new UserDataHolder.VehicleDataHolder();
//                                        for (int index = 1; index < jsonArrayVehicle.length(); index++) {
//                                            jsonObject = jsonArrayVehicle.getJSONObject(index);
//                                            vehicleDataHolder.setId(jsonObject.getString("id"));
//                                            vehicleDataHolder.setCustomer_email(jsonObject.getString("customer_email"));
//                                            vehicleDataHolder.setBrand(jsonObject.getString("brand"));
//                                            vehicleDataHolder.setName(jsonObject.getString("name"));
//                                            vehicleDataHolder.setModel(jsonObject.getString("model"));
//                                            vehicleDataHolder.setMake_year(jsonObject.getString("make_year"));
//                                            vehicleDataHolder.setPlate_no(jsonObject.getString("plate_no"));
//                                            vehicleDataHolderList.add(vehicleDataHolder);
//                                        }
//                                        UserDataHolder.setVehicleDetails(vehicleDataHolderList);
//                                    }
//                                    Toast.makeText(getApplicationContext(),"user type : "+UserDataHolder.getName(),Toast.LENGTH_LONG).show();
//                                    if(userType.equals("manager")){
//                                        Intent signInIntent = new Intent(LoginPage.this, ManagerHome.class);
//                                        startActivity(signInIntent);
//                                        finish();
//                                    }else if(userType.equals("customer")){
//                                        Intent signInIntent = new Intent(LoginPage.this, CustomerHome.class);
//                                        startActivity(signInIntent);
//                                        finish();
//                                    }else{
//                                        Toast.makeText(getApplicationContext(),"invalid user type maintained in DB for user, please contact support",Toast.LENGTH_LONG).show();
//                                    }
//                                }else{
//                                    Toast.makeText(getApplicationContext(),jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Toast.makeText(getApplicationContext(),"cannot call API"+error.toString(),Toast.LENGTH_SHORT).show();
//                        }
//                    }
//            );
//            requestQueue.add(stringRequest);
//            return null;
//        }
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//        }
//    }

}