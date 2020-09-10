package com.example.maximo;

public class CorpBonds {
    String name;
    String coupon;
    String rating;
    String nav;
    String risk;

    public CorpBonds(String name,String coupon,String rating,String nav,String risk)
    {
        this.coupon=coupon;
        this.rating = rating;
        this.risk = risk;
        this.nav = nav;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getCoupon() {
        return coupon;
    }

    public String getNav() {
        return nav;
    }

    public String getRating() {
        return rating;
    }

    public String getRisk() {
        return risk;
    }

}
