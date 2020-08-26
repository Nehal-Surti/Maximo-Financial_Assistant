package com.example.maximo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.ramotion.foldingcell.FoldingCell;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

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
    JSONArray answer;
    TextView valuation;
    String location = HouseFilter.param_location;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_pred);
        year = findViewById(R.id.yearsInput);
        predict = findViewById(R.id.predict);
        description = findViewById(R.id.description);
        area = findViewById(R.id.area);
        flat = findViewById(R.id.flat);
        valuation = findViewById(R.id.valuation);
        price_each = findViewById(R.id.price_each);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("house");
        description.setText(bundle.getString("Description"));
        area.setText(bundle.getString("Area"));
        flat.setText(bundle.getString("Flat"));
        price_each.setText(bundle.getString("Price"));
        carpet = bundle.getString("Carpet");
        final FoldingCell fc = (FoldingCell) findViewById(R.id.folding_cell);
        fc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc.toggle(false);
            }
        });

        years = Double.parseDouble(year.getText().toString());

        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(years==0.0)
                {
                    Toast.makeText(context,"Enter years of investment",Toast.LENGTH_LONG).show();
                }
                else
                    {
                        String[] carpets = carpet.split(" ");
                        carpet = carpets[0].replace(",","");
                        int area = Integer.parseInt(carpet);
                        Date date = new Date();
                        Calendar calendar = Calendar.getInstance();
                        int month = calendar.get(Calendar.MONTH);
                        int Year = calendar.get(Calendar.YEAR);
                        Year = Year + Integer.parseInt(years.toString());
                        String time = String.valueOf(Year) + "-0" + String.valueOf(month) + "-01";
                        RequestQueue queue = Volley.newRequestQueue(context);
                        String url = "http://192.168.0.5:8000/House_Property/"+ location + "/" + time + "/" + area + "/";
                        Toast.makeText(context,url,Toast.LENGTH_LONG).show();
                        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d("ABC",response.toString());
                                        if (response.length() != 0) {
                                            try {
                                                answer = response.getJSONArray("Answer");
                                                String value = answer.getString(0);
                                                valuation.setText("Rs. "+value+"/-");
                                            } catch (JSONException e) {
                                                Log.d("ER",e.getMessage());
                                                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                                            }
                                        }
                                        else{

                                            Toast.makeText(context,"Error in prediction",Toast.LENGTH_LONG).show();
                                        }
                                        // Display the first 500 characters of the response string.
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
                        queue.add(jsonRequest);
                }
            }
        });


    }
}
