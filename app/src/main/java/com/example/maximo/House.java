package com.example.maximo;

public class House {
    String AreaName;
    String FlatName;
    String Price;
    String Carpet;
    String Bathrooms;
    String Description;

    public House(String AreaName,String FlatName,String Price, String Carpet,String Bathrooms,String Description){
        this.AreaName = AreaName;
        this.FlatName = FlatName;
        this.Price = Price;
        this.Carpet = Carpet;
        this.Bathrooms = Bathrooms;
        this.Description = Description;
    }

    public String getDescription() {
        return Description;
    }

    public String getAreaName() {
        return AreaName;
    }

    public String getCarpet() {
        return Carpet;
    }

    public String getBathrooms() {
        return Bathrooms;
    }

    public String getFlatName() {
        return FlatName;
    }

    public String getPrice() {
        return Price;
    }

}
