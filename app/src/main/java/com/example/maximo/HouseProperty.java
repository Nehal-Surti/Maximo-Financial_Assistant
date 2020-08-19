package com.example.maximo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HouseProperty extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    String [] myDataset = { "3BHK Residential Apartment in Chembur (East)" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_property);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

//        Intent instent = getIntent();
//        Bundle args = instent.getBundleExtra("bundle");
//        ArrayList<House> houses = (ArrayList<House>) args.get("houses");
        // specify an adapter (see also next example)
        mAdapter = new HouseAdapter(this,HouseFilter.houses);
        recyclerView.setAdapter(mAdapter);
    }
}
