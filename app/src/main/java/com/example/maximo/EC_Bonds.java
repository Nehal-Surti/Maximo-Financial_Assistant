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

import java.text.Normalizer;

public class EC_Bonds extends AppCompatActivity {
    EditText Amount;
    TextView Coupon,Total;
    TextView Form1,Form2,Form3,Form4;
    Button Check;
    LinearLayout linearLayout;
    Context context = this;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ec_bonds);
        Amount = findViewById(R.id.ec_input);
        Check = findViewById(R.id.check_ec);
        linearLayout = findViewById(R.id.ec_linear);
        Coupon = findViewById(R.id.ec_coupon);
        Total = findViewById(R.id.ec_total_coupon);
        Form1 = findViewById(R.id.form1);
        Form2 = findViewById(R.id.form2);
        Form3 = findViewById(R.id.form3);
        Form4 = findViewById(R.id.form4);

        Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = Amount.getText().toString();

                if(amount.isEmpty())
                {
                    Toast.makeText(context,"Please enter the Amount",Toast.LENGTH_LONG).show();
                }
                else
                {
                    int invest = Integer.parseInt(amount);
                    if(invest<10000)
                    {
                        Toast.makeText(context,"Please enter the Amount greater than 10,000",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Bonds.getCoupon(invest,5.0,60,6,1,context);
                        double year_coupon = (1000 * 5.0) / 100;
                        double coupon = (year_coupon * 6 / 12);
                        Coupon.setText("Coupon Payment of Rs. "+ String.valueOf(coupon)+ " every 6 months");
                        double total_coupon = (year_coupon * 60 / 12);
                        Total.setText("Total Coupon over the maturity: Rs. " + String.valueOf(total_coupon));
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                }

            }
        });

        Form1.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String url = "https://kosmic.kfintech.com/REC/download_applns.aspx";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return false;
            }
        });

        Form2.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String url = "https://app.sbismart.com/pdfstamping/bonds/BondApp.aspx?SubBrokerName=HDFCSECURITIES";
                Intent i2 = new Intent(Intent.ACTION_VIEW);
                i2.setData(Uri.parse(url));
                startActivity(i2);
                return false;
            }
        });

        Form3.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String url = "https://kosmic.kfintech.com/pfc/download_applns.aspx";
                Intent i3 = new Intent(Intent.ACTION_VIEW);
                i3.setData(Uri.parse(url));
                startActivity(i3);
                return false;
            }
        });

        Form4.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String url = "https://kosmic.kfintech.com/IRFC/download_applns.aspx";
                Intent i4 = new Intent(Intent.ACTION_VIEW);
                i4.setData(Uri.parse(url));
                startActivity(i4);
                return false;
            }
        });
    }
}
