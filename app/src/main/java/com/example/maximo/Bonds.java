package com.example.maximo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Bonds extends AppCompatActivity {
    public static ArrayList<String> coupons = new ArrayList<>();
    Button Corporate,Fixed,EC;
    Context context = this;
    static JSONArray answer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bonds_main);
        EC = findViewById(R.id.ec);
        Corporate = findViewById(R.id.wc);
        Fixed = findViewById(R.id.fix);
        EC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,EC_Bonds.class);
                startActivity(intent);
            }
        });
        Corporate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Corp_Bonds.class);
                startActivity(intent);
            }
        });
        Fixed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Fix_Bonds.class);
                startActivity(intent);
            }
        });

    }

    public static void getCoupon(int amount, double rate, int period, int frequency,int num, final Context context)
    {
        coupons.clear();
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://192.168.0.6:8000/Bonds/" + amount + "/" + String.valueOf(rate) + "/" + period + "/" + frequency + "/"+ num+"/";
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ABC", response.toString());
                        if (response.length() != 0) {
                            try {
                                answer = response.getJSONArray("Answer");
                                getList();
                            } catch (JSONException e) {
                                Log.d("ER", e.getMessage());
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(context, "No loans found", Toast.LENGTH_LONG).show();
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

    public static void getList() throws JSONException {
        for(int i = 0; i < answer.length();i++)
        {
            coupons.add(answer.getString(i));
        }
    }
}
