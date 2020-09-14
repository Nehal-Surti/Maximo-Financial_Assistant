package com.example.maximo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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

public class EducationEMICalculator extends AppCompatActivity {
    TextView current_emi;
    EditText CourseFee;
    EditText Years;
    JSONArray emi_year;
    JSONArray emi_princi;
    double emi;
    JSONArray emi_interest;
    JSONArray emi_remain;
    double roi = 1;
    SpotsDialog progressDialog;
    int ROI = 9;
    Button Calculate;
    Button Show;
    int amount,year;
    String temp = "";
    LinearLayout linearLayout;
    Context context = this;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.education_emi_calculator);
        CourseFee = findViewById(R.id.course_fee);
        Years = findViewById(R.id.edu_years);
        Calculate = findViewById(R.id.calc_edu_emi);
        linearLayout = findViewById(R.id.show_edu_details);
        Show = findViewById(R.id.show_edu_emi);
        current_emi = findViewById(R.id.current_Edu_Emi);
        progressDialog = new SpotsDialog(context,R.style.Custom);
        Intent intent = getIntent();
        final Bundle bundle = intent.getBundleExtra("rates");

        Calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenure = CourseFee.getText().toString();
                String years = Years.getText().toString();

                if(tenure.isEmpty() || years.isEmpty() || ROI == 9)
                {
                    Toast.makeText(context,"Please fill the information",Toast.LENGTH_LONG).show();
                }
                else
                {
                    amount = Integer.parseInt(tenure);
                    year = Integer.parseInt(years);
                    if(ROI==1)
                    {
                        roi = Double.parseDouble(bundle.getString("India"));
                    }
                    else if(ROI==0)
                    {
                        roi = Double.parseDouble(bundle.getString("Abroad"));
                    }

                    RequestQueue queue = Volley.newRequestQueue(context);
                    String url = "http://192.168.0.6:8000/Loans/" + amount + "/" + String.valueOf(roi) + "/" + year*12 + "/";
                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("ABC", response.toString());
                                    if (response.length() != 0) {
                                        try {
                                            temp = response.getString("Answer");
                                            emi = Double.parseDouble(temp);
                                            current_emi.setText("Rs. " + String.valueOf(emi));
                                            linearLayout.setVisibility(View.VISIBLE);
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
                    Show.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            progressDialog.show();
                            RequestQueue queue = Volley.newRequestQueue(context);
                            String url = "http://192.168.0.6:8000/Loans/" + amount + "/" + Float.parseFloat(String.valueOf(emi)) + "/" + Float.parseFloat(String.valueOf(roi)) + "/" + year*12 + "/";
                            Toast.makeText(context, url, Toast.LENGTH_LONG).show();
                            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.d("ABC", response.toString());
                                            if (response.length() != 0) {
                                                try {
                                                    emi_interest = response.getJSONArray("Interest");
                                                    emi_princi = response.getJSONArray("Principal");
                                                    emi_remain = response.getJSONArray("Remaining");
                                                    emi_year = response.getJSONArray("Year");
                                                    createDetailsList();
                                                } catch (JSONException e) {
                                                    progressDialog.dismiss();
                                                    Log.d("ER", e.getMessage());
                                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(context, "No EMIs found", Toast.LENGTH_LONG).show();
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
            }
        });
    }

    public static String getEMI(int amount,double rate,int period,final Context context)
    {
        final String[] temp = new String[1];
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://192.168.0.6:8000/Loans/" + 5 + "/";
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ABC", response.toString());
                        if (response.length() != 0) {
                            try {
                                temp[0] = response.getString("Answer");
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
        return temp[0];
    }

    public void onROI(View view)
    {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.india:
                if (checked)
                    ROI = 1;
                linearLayout.setVisibility(View.GONE);
                break;
            case R.id.abroad:
                if (checked)
                    ROI = 0;
                linearLayout.setVisibility(View.GONE);
                break;
        }
    }

    public void createDetailsList() throws JSONException
    {
        Loans_Home.details.clear();
        for(int i=0;i<emi_interest.length();i++)
        {
            Loans_Home.details.add(new EMIDetails(emi_year.getString(i),emi_interest.getString(i),emi_princi.getString(i),emi_remain.getString(i)));
        }
        progressDialog.dismiss();
        Intent intent = new Intent(context,EMIScreen.class);
        Bundle bundle = new Bundle();
        bundle.putString("EMI", String.valueOf(emi));
        intent.putExtra("emi",bundle);
        startActivity(intent);
    }
}
