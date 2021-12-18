package com.example.topgas;

public class VehicleModel {
    String id,customer_email, name, model, brand, plate_no, make_year , img;

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMake_year() {
        return make_year;
    }

    public void setMake_year(String make_year) {
        this.make_year = make_year;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getPlate_no() {
        return plate_no;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setPlate_no(String plate_no) {
        this.plate_no = plate_no;
    }
}
