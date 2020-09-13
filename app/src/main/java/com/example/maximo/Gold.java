package com.example.maximo;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
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
        current.setText("Rs. " + bundle.getString("today"));
        todayRate = Integer.parseInt(bundle.getString("today"));

        calculate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                weight = Weight.getText().toString();
                months = Period.getText().toString();
                Log.d("DDD","Hi");
                if(weight.isEmpty() || months.isEmpty())
                {
                    Toast.makeText(context,"Please fill the inputs",Toast.LENGTH_LONG).show();
                }
                else
                {
                    String date = getDate(Integer.parseInt(months));
                    progressDialog.show();
                    RequestQueue queue = Volley.newRequestQueue(context);
                    String url = "http://192.168.0.5:8000/Gold/"+Integer.parseInt(weight)+"/"+date+"/"+Integer.parseInt(bundle.getString("today"))+"/";
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
                                                amount = Integer.parseInt(bundle.getString("today")) * w;
                                            }
                                            else
                                            {
                                                amount = Integer.parseInt(bundle.getString("today")) * (w/10);
                                            }
                                            today.setText("Rs. " + String.valueOf(amount));
                                            future.setText("Rs. "+String.valueOf(answer));
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getDate(int m)
    {
        Date date = new Date();
        int year = 0;
        int months = 0;
        String v,s;
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int Year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DATE);
        if(day<10)
        {
            v = "0" + String.valueOf(day);
        }
        else
        {
            v = String.valueOf(day);
        }
        if(month<10)
        {
            s = "0" + String.valueOf(month);
        }
        else
        {
            s = String.valueOf(month);
        }
        String temp = String.valueOf(Year)+"-"+s+"-"+ v;
        LocalDate date1 = LocalDate.parse(temp);
        LocalDate date2 = date1.plusMonths(m);
        Log.d("DDDDD",String.valueOf(date2));
        return date2.toString();
    }
}
