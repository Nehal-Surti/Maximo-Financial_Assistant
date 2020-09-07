package com.example.maximo;

public class Home_Loan {
    String bank;
    String rate;
    String maxTenure;
    String maxAge;
    String fee;
    String ltv;

    public Home_Loan(String bank,String rate,String maxTenure, String maxAge,String fee,String ltv)
    {
        this.bank = bank;
        this.fee = fee;
        this.ltv = ltv;
        this.maxAge = maxAge;
        this.maxTenure = maxTenure;
        this.rate = rate;
    }

    public String getBank() {
        return bank;
    }

    public String getFee() {
        return fee;
    }

    public String getLtv() {
        return ltv;
    }

    public String getMaxAge() {
        return maxAge;
    }

    public String getMaxTenure() {
        return maxTenure;
    }

    public String getRate() {
        return rate;
    }
}
