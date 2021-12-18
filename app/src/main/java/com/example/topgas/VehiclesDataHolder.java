package com.example.topgas;

import java.util.ArrayList;

public class VehiclesDataHolder {
    private static ArrayList<VehicleDataHolder> vehicleDetails;

    public static ArrayList<VehicleDataHolder> getVehicleDetails() {
        return vehicleDetails;
    }

    public static void setVehicleDetails(ArrayList<VehicleDataHolder> vehicleDetails) {
        VehiclesDataHolder.vehicleDetails = vehicleDetails;
    }
}
class VehicleDataHolder{
    private  String id;
    private  String customer_email;
    private   String name;
    private  String model;
    private  String brand;
    private  String plate_no;
    private  String make_year;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPlate_no() {
        return plate_no;
    }

    public void setPlate_no(String plate_no) {
        this.plate_no = plate_no;
    }

    public String getMake_year() {
        return make_year;
    }

    public void setMake_year(String make_year) {
        this.make_year = make_year;
    }
}