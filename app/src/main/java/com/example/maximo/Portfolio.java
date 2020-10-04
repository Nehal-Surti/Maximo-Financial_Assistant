package com.example.maximo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Portfolio extends AppCompatActivity {
    ImageView house,master,money,car;
    Context context = this;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goal_selection);
        house = findViewById(R.id.house_layout);
        master = findViewById(R.id.master_layout);
        money = findViewById(R.id.money_layout);
        car = findViewById(R.id.car_layout);

        house.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(context,PortFolio_Input.class);
                startActivity(intent);
                return false;
            }
        });

        master.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(context,PortFolio_Input.class);
                startActivity(intent);
                return false;
            }
        });

        car.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(context,PortFolio_Input.class);
                startActivity(intent);
                return false;
            }
        });

        money.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(context,PortFolio_Input.class);
                startActivity(intent);
                return false;
            }
        });
    }
}
