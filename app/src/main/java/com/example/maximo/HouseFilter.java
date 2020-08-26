package com.example.maximo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class HouseFilter extends AppCompatActivity{
    Button Location;
    Button BHK;
    Button MaxPrice;
    Button Search;
    public static ArrayList<House> houses = new ArrayList<House>();
    private static final String[] BHKS = {"1 BHK", "2 BHK", "3 BHK"};
    private static final String[] locations = {"Churchgate", "Dadar", "Chembur"};
    private static final String[] Prices = {"5 Lacs", "10 Lacs", "15 Lacs","20 Lacs","25 Lacs","30 Lacs","35 Lacs","40 Lacs"};
    int checkedBHK = 0;
    int checkedLocation = 0;
    int checkedPrice = 0;
    int param_BHK = 0;
    JSONArray area_name;
    JSONArray flat_name;
    JSONArray price_name;
    JSONArray carpet_name;
    JSONArray bath_name;
    JSONArray description;
    public static String param_location = "";
    int param_price = 0;
    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_filter);
        Location = findViewById(R.id.location);
        BHK = findViewById(R.id.bhk);
        Search = findViewById(R.id.submit_filters);
        MaxPrice = findViewById(R.id.maxPrice);
        ArrayList<House> houses = new ArrayList<House>();
        Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Select Loaction")
                        .setSingleChoiceItems(locations,checkedLocation, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // TODO: user specific action
                                checkedLocation = which;
                                Location.setText(locations[which]);
                                param_location = locations[which];
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

        BHK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Select BHK")
                        .setSingleChoiceItems(BHKS,checkedBHK, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // TODO: user specific action
                                checkedBHK = which;
                                BHK.setText(BHKS[which]);
                                param_BHK = Integer.parseInt(Character.toString(BHKS[which].charAt(0)));
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

        MaxPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Select Maximum Range Price")
                        .setSingleChoiceItems(Prices, checkedPrice,new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO: user specific action
                                checkedPrice = which;
                                MaxPrice.setText(Prices[which]);
                                String temp = Prices[which];
                                String[] temps = temp.split(" ");
                                temp = temps[0];
                                String s = "00000";
                                temp = temp + s;
                                param_price = Integer.parseInt(temp);
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!param_location.isEmpty()) {
                    RequestQueue queue = Volley.newRequestQueue(context);
                    String url = "http://192.168.0.5:8000/House_Property/" + param_BHK + "/" + param_location + "/" + param_price + "/";
                    Toast.makeText(context, url, Toast.LENGTH_LONG).show();
                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("ABC", response.toString());
                                    if (response.length() != 0) {
                                        try {
                                            area_name = response.getJSONArray("Areaname");
                                            flat_name = response.getJSONArray("Flatname");
                                            price_name = response.getJSONArray("Price");
                                            carpet_name = response.getJSONArray("Carpet");
                                            bath_name = response.getJSONArray("Bathrooms");
                                            description = response.getJSONArray("Description");
                                            createHouseList();
                                        } catch (JSONException e) {

                                            Log.d("ER", e.getMessage());
                                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    } else {

                                        Toast.makeText(context, "No houses found. Please change your filters", Toast.LENGTH_LONG).show();
                                    }
                                    // Display the first 500 characters of the response string.
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                    queue.add(jsonRequest);
                }
                else
                    {
                        Toast.makeText(context, "Select Location", Toast.LENGTH_LONG).show();
                    }
            }
        });
    }

    public void createHouseList() throws JSONException {
        if(price_name.length()==0)
        {
            Toast.makeText(context,"No houses found. Please change your filters",Toast.LENGTH_LONG).show();
        }
        else{
            for(int i =0 ; i< price_name.length();i++)
            {
                houses.add(new House(area_name.getString(i),flat_name.getString(i),price_name.getString(i),carpet_name.getString(i),bath_name.getString(i),description.getString(i)));
            }
            Set<House> s = new LinkedHashSet<House>(houses);
            houses = new ArrayList<House>(s);
            Intent intent = new Intent(this,HouseProperty.class);
            startActivity(intent);
        }
    }
}
