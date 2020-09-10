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

public class RBI_Bonds extends AppCompatActivity {
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
        setContentView(R.layout.rbi_bonds);
        Amount = findViewById(R.id.rbi_input);
        Check = findViewById(R.id.check_rbi);
        linearLayout = findViewById(R.id.rbi_linear);
        Coupon = findViewById(R.id.rbi_coupon);
        Total = findViewById(R.id.rbi_total_coupon);
        Form1 = findViewById(R.id.form);

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
                    double year_coupon = (1000 * 7.15) / 100;
                    double coupon = (year_coupon * 6 / 12);
                    Coupon.setText("Coupon Payment of Rs. "+ String.valueOf(coupon)+ " every 6 months");
                    double total_coupon = (year_coupon * 60 / 12);
                    Total.setText("Total Coupon over the maturity: Rs. " + String.valueOf(total_coupon));
                    linearLayout.setVisibility(View.VISIBLE);

                }

            }
        });

        Form1.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String url = "https://www.hdfcsec.com/hsl.docs//HDFC%20RBI%20Application%20Form-202007011241441780723.pdf";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return false;
            }
        });
    }
}
