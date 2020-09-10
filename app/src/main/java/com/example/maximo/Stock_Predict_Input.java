package com.example.maximo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import dmax.dialog.SpotsDialog;

public class Stock_Predict_Input extends AppCompatActivity {
    EditText number,years;
    Button Predict,Name;
    Context context;
    public static String name;
    int check;
    SpotsDialog progressDialog;
    public static int num = 1,days;
    public static String today,future;
    private static final String[] stocks = {"icici","hdfc","bajaj","cipla","gail","hul","itc","ongc","tcs","titan"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_name_input);
        Predict = findViewById(R.id.calc_emi3);
        number = findViewById(R.id.time_input2);
        years = findViewById(R.id.sav_input2);
        Name = findViewById(R.id.funds_input2);
        progressDialog = new SpotsDialog(context,R.style.Custom);

        Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Select Loaction")
                        .setSingleChoiceItems(stocks,check, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // TODO: user specific action
                                check = which;
                                Name.setText(stocks[which]);
                                name = stocks[which];
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

        Predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String no = number.getText().toString();
                String year = years.getText().toString();
                if(year.isEmpty() || name.isEmpty())
                {
                    Toast.makeText(context,"Please Enter the information",Toast.LENGTH_LONG).show();
                }
                else
                {
                    num = Integer.parseInt(no);
                    days = Integer.parseInt(year);
                    progressDialog.show();
                    RequestQueue queue = Volley.newRequestQueue(context);
                    String url = "http://192.168.0.5:8000/Stocks/" + Integer.parseInt(year) + "/" + name + "/";
                    Toast.makeText(context, url, Toast.LENGTH_LONG).show();
                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("ABC", response.toString());
                                    if (response.length() != 0) {
                                        try {
                                            today = response.getString("Today");
                                            future = response.getString("Future");
                                            progressDialog.dismiss();
                                            Intent intent = new Intent(context,Stocks_Predict.class);
                                            startActivity(intent);
                                        } catch (JSONException e) {
                                            progressDialog.dismiss();
                                            Log.d("ER", e.getMessage());
                                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(context, "No houses found. Please change your filters", Toast.LENGTH_LONG).show();
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
            }
        });
    }
}
