package com.example.maximo;

public class GSec {
    String name;
    String coupon;
    String maturity;
    String isn;
    String bid;

    public GSec(String symbol,String series,String coupon,String maturity,String isn,String bid)
    {
        this.coupon=coupon;
        this.maturity = maturity;
        this.isn = isn;
        this.bid = bid;
        this.name = symbol+ " " + series;
    }

    public String getMaturity() {
        return maturity;
    }

    public String getCoupon() {
        return coupon;
    }

    public String getBid() {
        return bid;
    }

    public String getIsn() {
        return isn;
    }

    public String getName() {
        return name;
    }
}
