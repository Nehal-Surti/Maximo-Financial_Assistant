package com.example.maximo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {
    Button Gold,Income,House,Loans,Bonds,Stocks,Ports,Funds;
    Context context = this;
    String answer;
    SpotsDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Gold = findViewById(R.id.gold);
        Loans = findViewById(R.id.loans);
        House = findViewById(R.id.house);
        Income = findViewById(R.id.income);
        Bonds = findViewById(R.id.bonds);
        Stocks = findViewById(R.id.stocks);
        Ports =findViewById(R.id.portfolio);
        Funds =findViewById(R.id.mutual);
        progressDialog = new SpotsDialog(context,R.style.Custom);

        Funds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MutualFunds_Input.class);
                startActivity(intent);
            }
        });

        Ports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Portfolio.class);
                startActivity(intent);
            }
        });
        Gold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                RequestQueue queue = Volley.newRequestQueue(context);
                String url = "http://192.168.0.5:8000/Gold/";
                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("ABC", response.toString());
                                if (response.length() != 0) {
                                    try {
                                        answer = response.getString("Answer");
                                        Intent intent = new Intent(context,Gold.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("today",answer);
                                        intent.putExtra("gold",bundle);
                                        progressDialog.dismiss();
                                        startActivity(intent);
                                    } catch (JSONException e) {
                                        progressDialog.dismiss();
                                        Log.d("ER", e.getMessage());
                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(context, "No loans found", Toast.LENGTH_LONG).show();
                                }
                                // Display the first 500 characters of the response string.
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                queue.add(jsonRequest);
            }
        });

        House.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(context,HouseFilter.class);
                startActivity(intent1);
            }
        });

        Income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(context,IncomeTax.class);
                startActivity(intent2);
            }
        });

        Loans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(context,Loans_Home.class);
                startActivity(intent3);
            }
        });

        Bonds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(context,Bonds.class);
                startActivity(intent3);
            }
        });

        Stocks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(context,Stock_Predict_Input.class);
                startActivity(intent3);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
