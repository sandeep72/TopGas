package com.example.topgas;

public class FuelModel {
    String fuelType;
    Float fuelPrice;
    int id;

    public FuelModel(String fuelType, Float fuelPrice, int id) {
        this.fuelType = fuelType;
        this.fuelPrice = fuelPrice;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
