package com.example.maximo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import dmax.dialog.SpotsDialog;

public class Gold extends AppCompatActivity {
    EditText Weight,Period;
    TextView today,current,future;
    Button calculate;
    int todayRate;
    String weight,months;
    Context context = this;
    SpotsDialog progressDialog;
    String answer;
    LinearLayout linearLayout;
    int amount;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gold);
        Weight = findViewById(R.id.weights_gold);
        Period = findViewById(R.id.period_gold);
        today = findViewById(R.id.today_price);
        progressDialog = new SpotsDialog(context,R.style.Custom);
        current = findViewById(R.id.current_price_gold);
        future = findViewById(R.id.future_value);
        calculate = findViewById(R.id.calculate_gold);
        linearLayout = findViewById(R.id.gold_layout);
        Intent intent = getIntent();
        final Bundle bundle = intent.getBundleExtra("gold");
        today.setText(bundle.getString("answer"));
        todayRate = Integer.parseInt(bundle.getString("answer"));

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weight = Weight.getText().toString();
                months = Period.getText().toString();

                if(weight.isEmpty() || months.isEmpty())
                {
                    Toast.makeText(context,"Please fill the inputs",Toast.LENGTH_LONG).show();
                }
                else
                {
                    String date = getDate(Integer.parseInt(months));
                    progressDialog.show();
                    RequestQueue queue = Volley.newRequestQueue(context);
                    String url = "http://192.168.0.5:8000/Gold/"+Integer.parseInt(weight)+"/"+date+"/"+Integer.parseInt(bundle.getString("answer")+"/");
                    Toast.makeText(context, url, Toast.LENGTH_LONG).show();
                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("ABC", response.toString());
                                    if (response.length() != 0) {
                                        try {
                                            answer = response.getString("Answer");
                                            int w = Integer.parseInt(weight);
                                            if(w<10)
                                            {
                                                amount = Integer.parseInt(bundle.getString("answer")) * w;
                                            }
                                            else
                                            {
                                                amount = Integer.parseInt(bundle.getString("answer")) * (w/10);
                                            }
                                            current.setText(String.valueOf(amount));
                                            future.setText(String.valueOf(answer));
                                            progressDialog.dismiss();
                                            linearLayout.setVisibility(View.VISIBLE);
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

    public String getDate(int m)
    {
        Date date = new Date();
        int year = 0;
        int months = 0;
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int Year = calendar.get(Calendar.YEAR);
        while(m>0)
        {
            if(m>12)
            {
                m=m%12;
                year = year + m/12;
            }
            if(m<12)
            {
                months = m;
                m=0;

            }
        }
        Year = Year + year;
        month=month+months;
        while(month>12)
        {
            Year = Year + months/12;
            months = months%12;
        }
        int day = calendar.get(Calendar.DATE);
        String v,s;
        if(day<10)
        {
            v = "0" + String.valueOf(day);
        }
        else
        {
            v = String.valueOf(day);
        }
        if(day<10)
        {
            s = "0" + String.valueOf(month);
        }
        else
        {
            s = String.valueOf(month);
        }
        String temp = String.valueOf(Year)+"-"+s+"-"+ v;
        return temp;
    }
}
