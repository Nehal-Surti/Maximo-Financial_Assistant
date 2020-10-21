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
import com.ramotion.foldingcell.FoldingCell;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import dmax.dialog.SpotsDialog;

public class HousePrediction extends AppCompatActivity {
    Double years = 0.0;
    EditText year;
    Button predict;
    Context context = this;
    TextView description;
    TextView area;
    TextView flat;
    TextView price_each;
    String price;
    String carpet;
    String answer;
    TextView valuation;
    Bundle bundle;
    TextView approx;
    LinearLayout prediction;
    SpotsDialog progressDialog;
    String location = HouseFilter.param_location;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_pred);
        year = findViewById(R.id.yearsInput);
        predict = findViewById(R.id.predict);
        prediction = findViewById(R.id.prediction);
        description = findViewById(R.id.description);
        area = findViewById(R.id.area);
        flat = findViewById(R.id.flat);
        valuation = findViewById(R.id.valuation);
        price_each = findViewById(R.id.price_each);
//        approx = findViewById(R.id.approx);
        progressDialog = new SpotsDialog(context,R.style.Custom);
        Intent intent = getIntent();
        bundle = intent.getBundleExtra("house");
        description.setText(bundle.getString("Description"));
        area.setText(bundle.getString("Area"));
        flat.setText(bundle.getString("Flat"));
        price_each.setText(bundle.getString("Price"));
        final FoldingCell fc = (FoldingCell) findViewById(R.id.folding_cell);
        fc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc.toggle(false);
            }
        });
        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                years = Double.parseDouble(year.getText().toString());
                if(years==0.0)
                {
                    Toast.makeText(context,"Enter years of investment",Toast.LENGTH_LONG).show();
                }
                else
                    {
                        progressDialog.show();
                        price = bundle.getString("Price");
                        carpet = bundle.getString("Carpet");
                        String[] prices = price.split(" ");
                        price = prices[3].split("/")[0];
                        price = price.replace(",","");
                        String[] carpets = carpet.split(" ");
                        carpet = carpets[0].replace(",","");
                        int area = Integer.parseInt(carpet);
                        Date date = new Date();
                        Calendar calendar = Calendar.getInstance();
                        int month = calendar.get(Calendar.MONTH);
                        int Year = calendar.get(Calendar.YEAR);
                        Year = Year + Integer.parseInt(year.getText().toString());
                        String time = String.valueOf(Year) + "-0" + String.valueOf(month) + "-01";
                        RequestQueue queue = Volley.newRequestQueue(context);
                        String url = "http://192.168.0.6:8000/House_Property/"+ location + "/" + time + "/" + area + "/" + Integer.parseInt(price) + "/";
                        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d("ABC",response.toString());
                                        if (response.length() != 0) {
                                            try {
                                                answer = response.getString("Answer");
                                                valuation.setText("Rs. "+answer+"/-");
                                                if(answer.length() > 7)
                                                {
                                                    int temp = Integer.parseInt(answer);
                                                    temp = temp/10000000;
//                                                    approx.setText("Approximately " + String.valueOf(temp) + " Crores");
                                                    prediction.setVisibility(View.VISIBLE);
                                                    progressDialog.dismiss();
                                                }
                                                else{
                                                    int temp = Integer.parseInt(answer);
                                                    temp = temp/100000;
//                                                    approx.setText("Approximately " + String.valueOf(temp) + " Crores");
                                                    prediction.setVisibility(View.VISIBLE);
                                                    progressDialog.dismiss();
                                                }
                                            } catch (JSONException e) {
                                                progressDialog.dismiss();
                                                Log.d("ER",e.getMessage());
                                                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                                            }
                                        }
                                        else{
                                            progressDialog.dismiss();
                                            Toast.makeText(context,"Error in prediction",Toast.LENGTH_LONG).show();
                                        }
                                        // Display the first 500 characters of the response string.
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
                        queue.add(jsonRequest);
                }
            }
        });
    }
}
