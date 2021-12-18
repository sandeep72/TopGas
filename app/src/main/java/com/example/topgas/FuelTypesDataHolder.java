package com.example.topgas;

import java.util.ArrayList;

public class FuelTypesDataHolder {

    private static ArrayList<FuelTypeDataHolder> fuelDetails;

    public static ArrayList<FuelTypeDataHolder> getFuelDetails() {
        return fuelDetails;
    }

    public static void setFuelDetails(ArrayList<FuelTypeDataHolder> fuelDetails) {
        FuelTypesDataHolder.fuelDetails = fuelDetails;
    }
}
class FuelTypeDataHolder {
    private String id;
    private String fuelType;
    private Float fuelPrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public Float getFuelPrice() {
        return fuelPrice;
    }

    public void setFuelPrice(Float fuelPrice) {
        this.fuelPrice = fuelPrice;
    }
}