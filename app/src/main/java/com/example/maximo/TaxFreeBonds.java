package com.example.maximo;

public class TaxFreeBonds {
    String name;
    String coupon;
    String maturity;
    String value;
    String Ytm;

    public TaxFreeBonds(String symbol,String series,String coupon,String maturity,String value,String Ytm)
    {
        this.coupon=coupon;
        this.maturity = maturity;
        this.value = value;
        this.Ytm = Ytm;
        this.name = symbol+ " " + series;
    }

    public String getName() {
        return name;
    }

    public String getCoupon() {
        return coupon;
    }

    public String getMaturity() {
        return maturity;
    }

    public String getValue() {
        return value;
    }

    public String getYtm() {
        return Ytm;
    }
}
