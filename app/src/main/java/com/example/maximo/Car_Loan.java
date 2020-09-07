package com.example.maximo;

public class Car_Loan {
    String bank;
    String rate;
    String maxTenure;
    String fix;
    String road;
    String ltv;

    public Car_Loan(String bank,String rate,String maxTenure, String fix,String road,String ltv)
    {
        this.bank = bank;
        this.fix = fix;
        this.ltv = ltv;
        this.road = road;
        this.maxTenure = maxTenure;
        this.rate = rate;
    }

    public String getRate() {
        return rate;
    }

    public String getMaxTenure() {
        return maxTenure;
    }

    public String getLtv() {
        return ltv;
    }

    public String getBank() {
        return bank;
    }

    public String getFix() {
        return fix;
    }

    public String getRoad() {
        return road;
    }
}
