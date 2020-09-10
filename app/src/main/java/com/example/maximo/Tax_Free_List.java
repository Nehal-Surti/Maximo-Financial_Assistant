package com.example.maximo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Tax_Free_List extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    TaxFree_Adapter mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tax_free_list);
        recyclerView = findViewById(R.id.tb_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new TaxFree_Adapter(this,Tax_Free.taxFreeBonds);
        recyclerView.setAdapter(mAdapter);
    }
}
