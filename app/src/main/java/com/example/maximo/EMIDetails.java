package com.example.maximo;

public class EMIDetails {
    String year;
    String interest;
    String principal;
    String remaining;

    public EMIDetails(String year,String interest,String principal,String remaining)
    {
        this.year = year;
        this.interest = interest;
        this.principal = principal;
        this.remaining = remaining;
    }

    public String getInterest() {
        return interest;
    }

    public String getPrincipal() {
        return principal;
    }

    public String getYear() {
        return year;
    }

    public String getRemaining() {
        return remaining;
    }
}
