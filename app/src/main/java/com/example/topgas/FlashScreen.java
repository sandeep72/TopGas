package com.example.topgas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.topgas.databinding.ProgressDialogBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FlashScreen extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    String intentName, email, password;
    private String apiPath = "https://sxy5732.uta.cloud/topgas%20api/customer_login.php";
    String userType;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        sharedPreferences = getSharedPreferences("MySharedPref_login", MODE_PRIVATE);
        if (sharedPreferences.contains("email") && sharedPreferences.contains("password")) {
            email = sharedPreferences.getString("email", "");
            password = sharedPreferences.getString("password", "");

            new ServiceStubAsyncTask(this, this).execute();

        } else {
            Intent signInIntent = new Intent(FlashScreen.this, LoginPage.class);
            startActivity(signInIntent);
            finish();

        }

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if(intentName.equals("LOGIN")){
//                    Intent intent = new Intent(FlashScreen.this, LoginPage.class);
//                    startActivity(intent);
//                    finish();
//                }else if(intentName.equals("CUSTOMERHOME")){
//                    Intent intent = new Intent(FlashScreen.this, CustomerHome.class);
//                    startActivity(intent);
//                    finish();
//                }
//            }
//        }, 5000);
    }


    //making an API call and getting the return.
    private class ServiceStubAsyncTask extends AsyncTask<Void, Void, Void> {

        private Context mContext;
        private Activity mActivity;
        RequestQueue requestQueue;


        public ServiceStubAsyncTask(Context context, Activity activity) {
            mContext = context;
            mActivity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(mContext);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            progressDialog.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            apiPath = apiPath+"?email="+ email+"&password="+password;
            requestQueue=Volley.newRequestQueue(mContext);
            StringRequest stringRequest = new StringRequest(
                    Request.Method.GET,
                    apiPath,
                    new Response.Listener<String>()  {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject;
                                JSONArray jsonArray,jsonArrayUser, jsonArrayVehicle;

                                jsonArray = new JSONArray(response);
                                jsonObject = jsonArray.getJSONObject(0);
                                String status = jsonObject.getString("status");
//                                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                                if(status.equals("true")){

                                    jsonObject = jsonArray.getJSONObject(1);
                                    jsonArrayUser = jsonObject.getJSONArray("user");
//                                    Toast.makeText(getApplicationContext(), jsonArrayUser.toString(), Toast.LENGTH_SHORT).show();
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
//                                    Toast.makeText(getApplicationContext(), jsonArrayVehicle.toString(), Toast.LENGTH_SHORT).show();
                                    jsonObject = jsonArrayVehicle.getJSONObject(0);
                                    if(jsonObject.getInt("vehicle_count") != 0) {
//                                        Toast.makeText(getApplicationContext(), "vehicle count: "+jsonObject.getInt("vehicle_count"), Toast.LENGTH_SHORT).show();
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
                                    if(progressDialog.isShowing()){
                                        progressDialog.dismiss();
                                    }
//                                    Toast.makeText(getApplicationContext(),"user type : "+UserDataHolder.getType(),Toast.LENGTH_LONG).show();
                                    if(userType.equals("manager")){
                                        Intent signInIntent = new Intent(FlashScreen.this, ManagerHome.class);
                                        startActivity(signInIntent);
                                        finish();
                                    }else if(userType.equals("customer")){
                                        Intent signInIntent = new Intent(FlashScreen.this, CustomerHome.class);
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

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }

    }


}