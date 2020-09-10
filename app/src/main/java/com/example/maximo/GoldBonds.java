package com.example.maximo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GoldBonds extends AppCompatActivity {
    EditText Amount,Year;
    TextView Coupon,Total;
    TextView Form1;
    Button Check;
    LinearLayout linearLayout;
    Context context = this;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gold_bonds);
        Amount = findViewById(R.id.gold_amount);
        Year = findViewById(R.id.gold_year);
        Check = findViewById(R.id.check_gold);
        linearLayout = findViewById(R.id.gold_linear);
        Coupon = findViewById(R.id.gold_coupon);
        Total = findViewById(R.id.gold_total_coupon);
        Form1 = findViewById(R.id.gold_form);

        Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = Amount.getText().toString();
                String year = Year.getText().toString();
                if(amount.isEmpty() || year.isEmpty())
                {
                    Toast.makeText(context,"Please enter the information",Toast.LENGTH_LONG).show();
                }
                else
                {
                    int invest = Integer.parseInt(amount);
                    int months = Integer.parseInt(year) * 12;
                    double year_coupon = (invest * 2.5) / 100;
                    double coupon = (year_coupon * 6/ 12);
                    Coupon.setText("Coupon Payment of Rs. "+ String.valueOf(coupon)+ " every 6 months");
                    double total_coupon = (year_coupon * months / 12);
                    Total.setText("Total Coupon over the maturity: Rs. " + String.valueOf(total_coupon));
                    linearLayout.setVisibility(View.VISIBLE);

                }

            }
        });

        Form1.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String url = "https://docs.google.com/document/d/1_uNw_X5FhMl4o8eTHgFSTtdXCPwO50w8Bp_JLLCpCJg/edit";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return false;
            }
        });
    }
}
