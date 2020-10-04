package com.example.maximo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Portfolio_List extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    PortFolio_Adapter mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.portfolio_list);
        recyclerView = findViewById(R.id.port_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new PortFolio_Adapter(this,PortFolio_Input.ports);
        recyclerView.setAdapter(mAdapter);
    }
}
