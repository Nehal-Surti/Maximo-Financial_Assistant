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

import dmax.dialog.SpotsDialog;

public class Loans_Home extends AppCompatActivity {
    public static ArrayList<Education_Loan> education_loans;
    Button EducationLoan;
    SpotsDialog progressDialog;
    Context context = this;
    JSONArray banks;
    JSONArray indias;
    JSONArray abroad;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loans_home);
        progressDialog = new SpotsDialog(context,R.style.Custom);
        EducationLoan = findViewById(R.id.edu_loans);

        EducationLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                RequestQueue queue = Volley.newRequestQueue(context);
                String url = "http://192.168.0.5:8000/Loans/" + 5 + "/";
                Toast.makeText(context, url, Toast.LENGTH_LONG).show();
                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("ABC", response.toString());
                                if (response.length() != 0) {
                                    try {
                                        banks = response.getJSONArray("BankName");
                                        indias = response.getJSONArray("IndiaRate");
                                        abroad = response.getJSONArray("AbroadRate");
                                        createLoansList();
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
    }

    public void createLoansList() throws JSONException
    {
        for(int i=0;i<banks.length();i++)
        {
            education_loans.add(new Education_Loan(banks.getString(i),indias.get(i).toString(),abroad.get(i).toString()));
        }
        progressDialog.dismiss();
        Intent intent = new Intent(context,Education_Loan_Page.class);
        startActivity(intent);
    }

    public static double getEMI(int amount,double rate,int period)
    {
        double temp = amount * (rate/1200)*(Math.pow((1+rate)/1200,period))/((Math.pow((1+rate)/1200,period))-1);
        return temp;
    }
}
