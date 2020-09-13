package com.example.maximo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Fix_Bonds extends AppCompatActivity {
    Button RBI,Gsec,Gold,Tax;
    Context context = this;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fixed_bonds);
        RBI = findViewById(R.id.rbi);
        Gsec = findViewById(R.id.gsec);
        Gold = findViewById(R.id.goldbonds);
        Tax = findViewById(R.id.taxfree);

        RBI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,RBI_Bonds.class);
                startActivity(intent);
            }
        });

        Gsec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,GovSec.class);
                startActivity(intent);
            }
        });

        Gold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,GoldBonds.class);
                startActivity(intent);
            }
        });

        Tax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Tax_Free.class);
                startActivity(intent);
            }
        });
    }
}
