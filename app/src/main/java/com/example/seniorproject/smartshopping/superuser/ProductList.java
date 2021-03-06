package com.example.seniorproject.smartshopping.superuser;

/**
 * Created by boyburin on 9/13/2017 AD.
 */

public class ProductList {
    private String name;
    private double retailprice;
    private String type;
    private String unit;
    private String photoUrl;

    public ProductList(){

    }

    public ProductList(String name, double retailprice, String type, String unit, String photoUrl) {
        this.name = name;
        this.retailprice = retailprice;
        this.type = type;
        this.unit = unit;
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRetailprice() {
        return retailprice;
    }

    public void setRetailprice(double retailprice) {
        this.retailprice = retailprice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
