package com.example.maximo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
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
    TextView taxSave;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income_final);
        tax = findViewById(R.id.tax);
        calculate = findViewById(R.id.calculateTax);
        taxableAmount = findViewById(R.id.total_taxable);
        taxPaid = findViewById(R.id.tax_paid);
        taxSave = findViewById(R.id.tax_save);

        taxableAmount.setText(String.valueOf(IncomeTax.net_salary));
        taxable = IncomeTax.net_salary;

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double tax_amount = calulateTax();
                if(taxable < 500000)
                {
                    if(tax_amount > 12500)
                    {
                        tax_amount = tax_amount - 12500;
                    }
                    else
                    {
                        tax_amount = 0;
                    }
                }
                double cess = calculateCess(tax_amount);
                Log.d("BBB",String.valueOf(cess));
                tax_amount  = tax_amount + cess;
                taxPaid.setText(String.valueOf(tax_amount));
                tax.setVisibility(View.VISIBLE);
            }
        });

        taxSave.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String url = "https://groww.in/blog/how-to-save-tax/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return false;
            }
        });
    }

    public  double calculateCess(double tax)
    {
        Log.d("BBB",String.valueOf(tax));
        double temp = 0.04 * tax;
        return temp;
    }
    public double calulateTax()
    {
        double tax = 0;
        if(taxable > 250000)
        {
            taxable = taxable - 250000;
            Log.d("CCC",String.valueOf(tax));
        }

        if(taxable > 300000)
        {
            tax = tax + (0.05 * 50000);
            taxable = taxable - 50000;
            Log.d("CCC",String.valueOf(tax));
        }
        else if(taxable < 300000)
        {
            tax = tax + (0.05 * taxable);
            taxable = 0;
            Log.d("CCC",String.valueOf(tax));
        }

        if(taxable > 500000)
        {
            tax = tax + (0.05 * 200000);
            taxable = taxable - 200000;
        }
        else if(taxable < 500000)
        {
            tax = tax + (0.05 * taxable);
            taxable = 0;
        }

        if(taxable > 1000000)
        {
            tax = tax + (0.2 * 500000);
            taxable = taxable - 500000;
        }
        else if(taxable < 1000000)
        {
            tax = tax + (0.2 * taxable);
            taxable = 0;
        }

        if(taxable > 1000000)
        {
            tax = tax + (0.3 * taxable);
        }
        Log.d("CCC",String.valueOf(tax));
        return tax;
    }
}
