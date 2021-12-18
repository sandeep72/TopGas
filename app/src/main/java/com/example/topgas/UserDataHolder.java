package com.example.topgas;

import java.util.ArrayList;

public class UserDataHolder {
    private static String name, id, email, mobile_no, address, street, city, state, zip_code, type;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        UserDataHolder.name = name;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        UserDataHolder.id = id;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        UserDataHolder.email = email;
    }

    public static String getMobile_no() {
        return mobile_no;
    }

    public static void setMobile_no(String mobile_no) {
        UserDataHolder.mobile_no = mobile_no;
    }

    public static String getAddress() {
        return address;
    }

    public static void setAddress(String address) {
        UserDataHolder.address = address;
    }

    public static String getStreet() {
        return street;
    }

    public static void setStreet(String street) {
        UserDataHolder.street = street;
    }

    public static String getCity() {
        return city;
    }

    public static void setCity(String city) {
        UserDataHolder.city = city;
    }

    public static String getState() {
        return state;
    }

    public static void setState(String state) {
        UserDataHolder.state = state;
    }

    public static String getZip_code() {
        return zip_code;
    }

    public static void setZip_code(String zip_code) {
        UserDataHolder.zip_code = zip_code;
    }

    public static String getType() {
        return type;
    }

    public static void setType(String type) {
        UserDataHolder.type = type;
    }
}


