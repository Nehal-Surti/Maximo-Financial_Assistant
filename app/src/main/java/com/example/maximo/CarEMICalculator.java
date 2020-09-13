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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dmax.dialog.SpotsDialog;

public class CarEMICalculator extends AppCompatActivity {
    TextView current_emi;
    EditText Car;
    EditText Years;
    JSONArray emi_year;
    JSONArray emi_princi;
    double emi;
    JSONArray emi_interest;
    JSONArray emi_remain;
    double roi = 1;
    SpotsDialog progressDialog;
    Button Calculate;
    Button Show;
    int ltv;
    int amount,year;
    LinearLayout linearLayout;
    Context context = this;
    TextView textView,textView1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_emi_calculator);
        Car = findViewById(R.id.car_value);
        Years = findViewById(R.id.car_years);
        textView = findViewById(R.id.t_car);
        Calculate = findViewById(R.id.calc_car_emi);
        linearLayout = findViewById(R.id.show_car_details);
        Show = findViewById(R.id.show_car_emi);
        current_emi = findViewById(R.id.current_car_Emi);
        textView1 = findViewById(R.id.cartextView);
        progressDialog = new SpotsDialog(context,R.style.Custom);
        Intent intent = getIntent();
        final Bundle bundle = intent.getBundleExtra("other");
        textView.setText("Tenure in years (max " + Integer.parseInt(bundle.getString("tenure")) + " years)");
        textView1.setText("Car Value according to " + bundle.getString("road"));

        Calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenure = Car.getText().toString();
                String years = Years.getText().toString();

                if(tenure.isEmpty() || years.isEmpty())
                {
                    Toast.makeText(context,"Please fill the information",Toast.LENGTH_LONG).show();
                }
                else
                {
                    roi = Double.parseDouble(bundle.getString("rate").split("%")[0]);
                    amount = Integer.parseInt(tenure);
                    ltv = Integer.parseInt(bundle.getString("ltv").split("%")[0]);
                    Log.d("BBB",String.valueOf(amount));
                    Log.d("BBB",String.valueOf(ltv));
                    year = Integer.parseInt(years);
                    amount = (amount*ltv)/100;
                    emi = Double.parseDouble(Loans_Home.getEMI(amount,roi,year*12));
                    current_emi.setText("Rs. " + String.valueOf(emi));
                    linearLayout.setVisibility(View.VISIBLE);
                    Show.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            progressDialog.show();
                            RequestQueue queue = Volley.newRequestQueue(context);
                            String url = "http://192.168.0.5:8000/Loans/" + amount + "/" + Float.parseFloat(String.valueOf(emi)) + "/" + Float.parseFloat(String.valueOf(emi)) + "/" + year*12 + "/";
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
