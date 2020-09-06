package com.example.maximo;

public class Education_Loan {
    String bank;
    String indiaRate;
    String abroadRate;

    public Education_Loan(String bank, String indiaRate, String abroadRate)
    {
        this.bank = bank;
        this.indiaRate = indiaRate;
        this.abroadRate = abroadRate;
    }

    public String getAbroadRate() {
        return abroadRate;
    }

    public String getBank() {
        return bank;
    }

    public String getIndiaRate() {
        return indiaRate;
    }
}
