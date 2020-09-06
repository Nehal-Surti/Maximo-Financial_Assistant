package com.example.maximo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EMIScreen extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    TextView EMI;
    EMIAdapter mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emi_screen);
        recyclerView = findViewById(R.id.show_emi);
        EMI = findViewById(R.id.emi_total);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("emi");
        EMI.setText("Rs. " + bundle.getString("EMI"));
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new EMIAdapter(this,EducationEMICalculator.details);
        recyclerView.setAdapter(mAdapter);
    }
}
