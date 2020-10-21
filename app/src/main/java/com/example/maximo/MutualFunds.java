package com.example.maximo;

public class MutualFunds {
    String name;
    String rating;
    String rate_1;
    String return_1;
    String rate_3;
    String return_3;
    String rate_5;
    String return_5;
    String rate_10;
    String return_10;

    public MutualFunds(String name, String rating, String rate_1, String return_1, String rate_3, String return_3, String rate_5, String return_5, String rate_10, String return_10) {
        this.name = name;
        this.rating = rating;
        this.rate_1 = rate_1;
        this.rate_3 = rate_3;
        this.rate_5 = rate_5;
        this.rate_10 = rate_10;
        this.return_1 = return_1;
        this.return_3 = return_3;
        this.return_5 = return_5;
        this.return_10 = return_10;
    }

    public String getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }

    public String getRate_1() {
        return rate_1;
    }

    public String getRate_3() {
        return rate_3;
    }

    public String getRate_5() {
        return rate_5;
    }

    public String getRate_10() {
        return rate_10;
    }

    public String getReturn_1() {
        return return_1;
    }

    public String getReturn_3() {
        return return_3;
    }

    public String getReturn_5() {
        return return_5;
    }

    public String getReturn_10() {
        return return_10;
    }
}