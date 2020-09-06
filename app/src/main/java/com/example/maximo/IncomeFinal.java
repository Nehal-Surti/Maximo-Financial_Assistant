package com.example.maximo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class IncomeFinal extends AppCompatActivity {
    LinearLayout tax;
    Button calculate;
    TextView taxableAmount;
    TextView taxPaid;
    int taxable;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income_final);
        tax = findViewById(R.id.tax);
        calculate = findViewById(R.id.calculateTax);
        taxableAmount = findViewById(R.id.total_taxable);
        taxPaid = findViewById(R.id.tax_paid);

        taxableAmount.setText(String.valueOf(IncomeTax.net_salary));
        taxable = IncomeTax.net_salary;

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double tax_amount = calulateTax();
                double cess = calculateCess(tax_amount);
                tax_amount  = tax_amount + cess;
                taxPaid.setText(String.valueOf(tax_amount));
            }
        });
    }

    public  double calculateCess(double tax)
    {
        return (0.04*tax);
    }
    public double calulateTax()
    {
        double tax = 0;
        if(taxable > 250000)
        {
            taxable = taxable - 250000;
        }

        if(taxable > 300000)
        {
            tax = tax + (0.05 * 50000);
            taxable = taxable - 50000;
        }
        else if(taxable < 300000)
        {
            taxable = 300000 - taxable;
            tax = tax + (0.05 * taxable);
        }

        if(taxable > 500000)
        {
            tax = tax + (0.05 * 200000);
            taxable = taxable - 200000;
        }
        else if(taxable < 500000)
        {
            taxable = 500000 - taxable;
            tax = tax + (0.05 * taxable);
        }

        if(taxable > 1000000)
        {
            tax = tax + (0.2 * 500000);
            taxable = taxable - 500000;
        }
        else if(taxable < 1000000)
        {
            taxable = 1000000 - taxable;
            tax = tax + (0.2 * taxable);
        }

        if(taxable > 1000000)
        {
            tax = tax + (0.3 * taxable);
        }
        return tax;
    }
}
