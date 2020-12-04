package com.example.maximo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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

public class MutualFunds_Input extends AppCompatActivity {
    public static ArrayList<MutualFunds> mutualFunds = new ArrayList<>();
    EditText regular_input, growth_input, tax_input;
    Button regular,taxmf,growthmf;
    String amount;
    String risk = "NA";
    Context context = this;
    int id;
    SpotsDialog progressDialog;
    JSONArray names,ratings,rate_1,rate_3,rate_5,rate_10,return_1,return_3,return_5,return_10;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mutual_main);
        regular_input = findViewById(R.id.regular_input);
        growth_input = findViewById(R.id.growth_input);
        tax_input = findViewById(R.id.taxmf_input);
        regular = findViewById(R.id.regular);
        taxmf = findViewById(R.id.taxmf);
        growthmf = findViewById(R.id.growth);
        progressDialog = new SpotsDialog(context,R.style.Custom);

        regular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = regular_input.getText().toString();
                if(amount.isEmpty())
                {
                    Toast.makeText(context," Enter amount ", Toast.LENGTH_LONG).show();
                }
                else
                {
                    progressDialog.show();
                    RequestQueue queue = Volley.newRequestQueue(context);
                    String url = "http://192.168.0.5:8000/MutualFunds/"+Integer.parseInt(amount)+"/"+2+"/"+1+"/";
                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("ABC", response.toString());
                                    if (response.length() != 0) {
                                        try {
                                            names = response.getJSONArray("Name");
                                            ratings = response.getJSONArray("Rating");
                                            rate_1 = response.getJSONArray("1(%)");
                                            rate_3 = response.getJSONArray("3(%)");
                                            rate_5 = response.getJSONArray("5(%)");
                                            rate_10 = response.getJSONArray("10(%)");
                                            return_1 = response.getJSONArray("1Returns");
                                            return_3 = response.getJSONArray("3Returns");
                                            return_5 = response.getJSONArray("5Returns");
                                            return_10 = response.getJSONArray("10Returns");
                                            createList();
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

            }
        });

        growthmf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = growth_input.getText().toString();
                if(amount.isEmpty() || risk.equals("NA"))
                {
                    Toast.makeText(context," Enter amount or select the risk type ", Toast.LENGTH_LONG).show();
                }
                else
                {
                    progressDialog.show();
                    RequestQueue queue = Volley.newRequestQueue(context);
                    String url = "http://192.168.0.5:8000/MutualFunds/"+Integer.parseInt(amount)+"/"+risk+"/";
                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("ABC", response.toString());
                                    if (response.length() != 0) {
                                        try {
                                            names = response.getJSONArray("Name");
                                            ratings = response.getJSONArray("Rating");
                                            rate_1 = response.getJSONArray("1(%)");
                                            rate_3 = response.getJSONArray("3(%)");
                                            rate_5 = response.getJSONArray("5(%)");
                                            rate_10 = response.getJSONArray("10(%)");
                                            return_1 = response.getJSONArray("1Returns");
                                            return_3 = response.getJSONArray("3Returns");
                                            return_5 = response.getJSONArray("5Returns");
                                            return_10 = response.getJSONArray("10Returns");
                                            createList();
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

            }
        });

        taxmf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = tax_input.getText().toString();
                if(amount.isEmpty())
                {
                    Toast.makeText(context," Enter amount ", Toast.LENGTH_LONG).show();
                }
                else
                {
                    progressDialog.show();
                    RequestQueue queue = Volley.newRequestQueue(context);
                    String url = "http://192.168.0.5:8000/MutualFunds/"+Integer.parseInt(amount)+"/"+1+"/"+1+"/";
                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("ABC", response.toString());
                                    if (response.length() != 0) {
                                        try {
                                            names = response.getJSONArray("Name");
                                            ratings = response.getJSONArray("Rating");
                                            rate_1 = response.getJSONArray("1(%)");
                                            rate_3 = response.getJSONArray("3(%)");
                                            rate_5 = response.getJSONArray("5(%)");
                                            rate_10 = response.getJSONArray("10(%)");
                                            return_1 = response.getJSONArray("1Returns");
                                            return_3 = response.getJSONArray("3Returns");
                                            return_5 = response.getJSONArray("5Returns");
                                            return_10 = response.getJSONArray("10Returns");
                                            createList();
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

            }
        });
    }

    public void onRiskSelected(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.low:
                if (checked)
                    risk = "low";
                break;
            case R.id.medium:
                if (checked)
                    risk = "medium";
                break;
            case R.id.high:
                if (checked)
                    risk = "high";
                break;
        }
    }

    public void createList() throws JSONException {
        mutualFunds.clear();
        for(int i=0; i < names.length() ; i++)
        {
            mutualFunds.add(new MutualFunds(names.getString(i),ratings.getString(i),rate_1.getString(i),return_1.getString(i),rate_3.getString(i),return_3.getString(i),rate_5.getString(i),return_5.getString(i),rate_10.getString(i),return_10.getString(i)));
        }
        Intent intent = new Intent(context,Mutual_List.class);
        startActivity(intent);
    }
}
