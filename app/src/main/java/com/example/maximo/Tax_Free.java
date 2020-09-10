package com.example.maximo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import dmax.dialog.SpotsDialog;

public class Tax_Free extends AppCompatActivity {
    EditText number;
    TextView info;
    Button check;
    public static int no_of_bonds;
    Context context = this;
    SpotsDialog progressDialog;
    JSONArray symbol,series,rate,ytm,maturity,value;
    public static ArrayList<TaxFreeBonds> taxFreeBonds = new ArrayList<>();
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tax_free_bonds);
        number = findViewById(R.id.tax_number);
        info = findViewById(R.id.taxbond_info);
        check = findViewById(R.id.check_tax);
        progressDialog = new SpotsDialog(context,R.style.Custom);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String no = number.getText().toString();
                if(no.isEmpty())
                {
                    no_of_bonds = 1;
                }
                else
                {
                    no_of_bonds = Integer.parseInt(no);
                }
                progressDialog.show();
                RequestQueue queue = Volley.newRequestQueue(context);
                String url = "http://192.168.0.5:8000/Bonds/" + 1 + "/";
                Toast.makeText(context, url, Toast.LENGTH_LONG).show();
                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("ABC", response.toString());
                                if (response.length() != 0) {
                                    try {
                                        symbol = response.getJSONArray("Symbol");
                                        series = response.getJSONArray("Series");
                                        rate = response.getJSONArray("Coupon");
                                        ytm = response.getJSONArray("YTM");
                                        maturity = response.getJSONArray("Maturity");
                                        value = response.getJSONArray("Value");
                                        createTaxList();
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

        info.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String url = "https://zerodha.com/z-connect/coin/start-investing-in-corporate-bonds";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return false;
            }
        });
    }

    public void createTaxList() throws JSONException
    {
        taxFreeBonds.clear();
        for(int i=0;i<symbol.length();i++)
        {
            taxFreeBonds.add(new TaxFreeBonds(symbol.getString(i),series.getString(i),rate.getString(i),maturity.getString(i),value.getString(i),ytm.getString(i)));
        }
        progressDialog.dismiss();
        Intent intent = new Intent(context,Tax_Free_List.class);
        startActivity(intent);
    }
}
