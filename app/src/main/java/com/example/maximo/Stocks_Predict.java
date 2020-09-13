package com.example.maximo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Stocks_Predict extends AppCompatActivity {
    TextView Name,Price,today_per,today_total,future_per,future_total,number,years,ROI;
    Button click;
    Context context = this;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_future_price);
        Name = findViewById(R.id.textViewStockName);
        Price = findViewById(R.id.stock_price);
        today_per = findViewById(R.id.today_stock_price);
        today_total = findViewById(R.id.today_total_price);
        future_per = findViewById(R.id.future_stock_price);
        future_total = findViewById(R.id.future_total_price);
        number = findViewById(R.id.nostocks);
        years = findViewById(R.id.noyears);
        ROI = findViewById(R.id.ROI);
        click = findViewById(R.id.stock_detail);

        double today = Math.round(Double.parseDouble(Stock_Predict_Input.today) * Stock_Predict_Input.num);
        double future = Math.round(Double.parseDouble(Stock_Predict_Input.future) * Stock_Predict_Input.num);

        double roi = (Math.abs(Math.abs(future)-Math.abs(today)) * 100)/today;

        Name.setText(Stock_Predict_Input.name);
        Price.setText("Rs. " + String.valueOf(future));
        today_per.setText("Rs. " + Stock_Predict_Input.today);
        today_total.setText("Rs. " + String.valueOf(today));
        future_per.setText("Rs. " + Stock_Predict_Input.future);
        future_total.setText("Rs. " + future);
        number.setText("No.of Stocks: " + Stock_Predict_Input.num);
        years.setText("Time to Maturity:" + Stock_Predict_Input.days + " days");
        ROI.setText("Return on Investment: " + roi + "%");

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www1.nseindia.com/live_market/dynaContent/live_watch/equities_stock_watch.htm";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }
}
