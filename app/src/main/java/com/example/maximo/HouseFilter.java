package com.example.maximo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HouseFilter extends AppCompatActivity{
    Button Location;
    Button BHK;
    Button MaxPrice;
    Button MinArea;
    private static final String[] BHKS = {"1 BHK", "2 BHK", "3 BHK"};
    private static final String[] locations = {"Churchgate", "Dadar", "Chembur"};
    private static final String[] Prices = {"5 Lacs", "10 Lacs", "15 Lacs","20 Lacs","25 Lacs","30 Lacs","35 Lacs","40 Lacs"};
    private static final String[] Areas = {"200 sq.ft", "500 sq.ft", "700 sq.ft", "900 sq.ft", "1100 sq.ft", "1400 sq.ft" ,"1500 sq.ft","1700 sq.ft" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_filter);
        Location = findViewById(R.id.location);
        BHK = findViewById(R.id.bhk);
        MinArea = findViewById(R.id.minArea);
        MaxPrice = findViewById(R.id.maxPrice);
        final ArrayAdapter<String> Locationadapter = new ArrayAdapter<String>(HouseFilter.this,
                android.R.layout.simple_spinner_item,locations);
        final ArrayAdapter<String> BHKadapter = new ArrayAdapter<String>(HouseFilter.this,
                android.R.layout.simple_spinner_item,BHKS);
        final ArrayAdapter<String> Priceadapter = new ArrayAdapter<String>(HouseFilter.this,
                android.R.layout.simple_spinner_item,Prices);
        final ArrayAdapter<String> Areaadapter = new ArrayAdapter<String>(HouseFilter.this,
                android.R.layout.simple_spinner_item,Areas);

        Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getApplicationContext())
                        .setTitle("the prompt")
                        .setAdapter(Locationadapter, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // TODO: user specific action
                                Location.setText(locations[which]);
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

        BHK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getApplicationContext())
                        .setTitle("the prompt")
                        .setAdapter(BHKadapter, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // TODO: user specific action
                                BHK.setText(BHKS[which]);
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

        MaxPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getApplicationContext())
                        .setTitle("the prompt")
                        .setAdapter(Priceadapter, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // TODO: user specific action
                                MaxPrice.setText(Prices[which]);
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

        MinArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getApplicationContext())
                        .setTitle("the prompt")
                        .setAdapter(Areaadapter, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // TODO: user specific action
                                MinArea.setText(Areas[which]);
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
    }
}
