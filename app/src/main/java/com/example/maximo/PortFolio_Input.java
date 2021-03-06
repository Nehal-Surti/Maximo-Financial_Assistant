package com.example.maximo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
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
import pl.droidsonroids.gif.GifImageView;

public class PortFolio_Input extends AppCompatActivity {
    GifImageView gifImageView ;
    static ArrayList<String> ports = new ArrayList<>();
    Context context = this;
    EditText goal_input,sal_input,sav_input,funds_input,time_input;
    Button create;
    JSONArray answer;
    RatingBar ratingBar;
    SpotsDialog progressDialog;
    int annual_salary,tenure,annual_investment,goal_amount,risk,funds;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio_main);
        goal_input = findViewById(R.id.goal_input);
        sal_input = findViewById(R.id.sal_input);
        sav_input = findViewById(R.id.sav_input);
        funds_input = findViewById(R.id.funds_input);
        time_input = findViewById(R.id.time_input);
        create = findViewById(R.id.create_port);
        ratingBar = findViewById(R.id.ratingBar);
        gifImageView = findViewById(R.id.gifImage);
        progressDialog = new SpotsDialog(context,R.style.Custom);

        if(Portfolio.GOAL.equals("House"))
        {
            gifImageView.setBackgroundResource(R.drawable.house_gif);
        }
        if(Portfolio.GOAL.equals("Car"))
        {
            gifImageView.setBackgroundResource(R.drawable.car_gif);
        }
        if(Portfolio.GOAL.equals("Degree"))
        {
            gifImageView.setBackgroundResource(R.drawable.degree_gif);
        }
        if(Portfolio.GOAL.equals("Money"))
        {
            gifImageView.setBackgroundResource(R.drawable.money_gif);
        }
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                annual_salary = Integer.parseInt(String.valueOf(sal_input.getText()));
                tenure = Integer.parseInt(String.valueOf(time_input.getText()));
                if(String.valueOf(sav_input.getText()).isEmpty())
                {
                    annual_investment = 0;
                }
                else
                {
                    annual_investment = Integer.parseInt(String.valueOf(sav_input.getText()));
                }
                if(String.valueOf(funds_input.getText()).isEmpty())
                {
                    funds = 0;
                }
                else
                {
                    funds = Integer.parseInt(String.valueOf(funds_input.getText()));
                }
                goal_amount = Integer.parseInt(String.valueOf(goal_input.getText()));
                goal_amount = goal_amount - funds;
                risk = (int) Math.round(ratingBar.getRating());
//                risk = Integer.parseInt(String.valueOf(ratingBar.getRating()));
                progressDialog.show();
                RequestQueue queue = Volley.newRequestQueue(context);
                String url = "http://192.168.0.5:8000/Portfolio/" + tenure + "/" + goal_amount + "/" + annual_salary + "/" + annual_investment + "/" + 7 + "/" + risk;
                Log.d("Port",url);
                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("ABC", response.toString());
                                if (response.length() != 0) {
                                    try {
                                        answer = response.getJSONArray("Answer");
                                        create_ports();
                                    } catch (JSONException e) {
                                        progressDialog.dismiss();
                                        Log.d("ER", e.getMessage());
                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    progressDialog.dismiss();
                                }
                                // Display the first 500 characters of the response string.
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Port","Gadbad");
                        progressDialog.dismiss();
                        Log.d("Port",error.toString());
                        Toast.makeText(context, error.toString() , Toast.LENGTH_LONG).show();
                    }
                });
                jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                        100000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(jsonRequest);

            }
        });
    }

    public void create_ports() throws JSONException {
        ports.clear();
        for(int i=0;i<answer.length();i++)
        {
            ports.add(answer.getString(i));
        }
        progressDialog.dismiss();
        Intent intent = new Intent(context,Portfolio_List.class);
        startActivity(intent);
    }

}
