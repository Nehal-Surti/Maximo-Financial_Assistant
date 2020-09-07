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

public class Home_Loan_Input extends AppCompatActivity {
    public static ArrayList<Home_Loan> home_loans;
    EditText Salary;
    EditText Age;
    EditText Obligations;
    LinearLayout linearLayout;
    int profession = 9;
    Button Eligibility,CheckLoans;
    TextView Afford;
    String amount, age, obligations;
    Context context = this;
    JSONArray banks,fee,rate,tenure,maxAge,ltv;
    SpotsDialog progressDialog;
    double emi = 0.0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_loan_input);
        Salary = findViewById(R.id.home_salary_input);
        Age = findViewById(R.id.home_age_input);
        Obligations = findViewById(R.id.home_obli_input);
        Eligibility = findViewById(R.id.check_home_eli);
        linearLayout = findViewById(R.id.show_home_eli);
        Afford = findViewById(R.id.home_afford_Emi);
        progressDialog = new SpotsDialog(context,R.style.Custom);
        CheckLoans = findViewById(R.id.check_homeloans);


        Eligibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = Salary.getText().toString();
                age = Age.getText().toString();
                obligations = Obligations.getText().toString();
                if(amount.isEmpty() || age.isEmpty())
                {
                    Toast.makeText(context,"Please enter the information",Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(profession==9)
                    {
                        Toast.makeText(context,"Please choose a profession",Toast.LENGTH_LONG).show();
                    }
                    if(profession==1)
                    {
                        if(Integer.parseInt(amount) < 10000)
                        {
                            Toast.makeText(context,"Not Eligible since salary is less than 10,000",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            if(obligations.isEmpty())
                            {
                                emi = eligible(Integer.parseInt(amount),Integer.parseInt(age),0);
                                afterEligibility(emi);
                            }
                            else
                            {
                                emi = eligible(Integer.parseInt(amount),Integer.parseInt(age),Integer.parseInt(obligations));
                                afterEligibility(emi);
                            }
                        }
                    }

                    if(profession==0)
                    {
                        if(Integer.parseInt(amount) < 8333)
                        {
                            Toast.makeText(context,"Not Eligible since salary is less than 8,333",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            if(obligations.isEmpty())
                            {
                                emi = eligible(Integer.parseInt(amount),Integer.parseInt(age),0);
                                afterEligibility(emi);
                            }
                            else
                            {
                                emi = eligible(Integer.parseInt(amount),Integer.parseInt(age),Integer.parseInt(obligations));
                                afterEligibility(emi);
                            }
                        }
                    }
                }
            }
        });
    }

    public void afterEligibility(double emi)
    {
        if(emi==0.0)
        {
            Toast.makeText(context,"Not Eligible since emi you can afford is less",Toast.LENGTH_LONG).show();
        }
        else
        {
            Afford.setText(String.valueOf(emi));
            linearLayout.setVisibility(View.VISIBLE);
            CheckLoans.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog.show();
                    RequestQueue queue = Volley.newRequestQueue(context);
                    String url = "http://192.168.0.5:8000/Loans/" + 3 + "/";
                    Toast.makeText(context, url, Toast.LENGTH_LONG).show();
                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("ABC", response.toString());
                                    if (response.length() != 0) {
                                        try {
                                            banks = response.getJSONArray("BankName");
                                            fee = response.getJSONArray("Fee");
                                            rate = response.getJSONArray("Rate");
                                            tenure = response.getJSONArray("maxTenure");
                                            maxAge = response.getJSONArray("maxAge");
                                            ltv = response.getJSONArray("LTV");
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
    }

    public void createLoansList() throws JSONException
    {
        for(int i=0;i<banks.length();i++)
        {
            home_loans.add(new Home_Loan(banks.getString(i),rate.getString(i),tenure.getString(i),maxAge.getString(i),fee.getString(i),ltv.getString(i)));
        }
        progressDialog.dismiss();
        Intent intent = new Intent(context,Home_Loan_Page.class);
        startActivity(intent);
    }

    public void onProfession(View view)
    {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.salaried:
                if (checked)
                    profession = 1;
                linearLayout.setVisibility(View.GONE);
                break;
            case R.id.self_employeed:
                if (checked)
                    profession = 0;
                linearLayout.setVisibility(View.GONE);
                break;
        }
    }

    public double eligible(int amount,int age,int other)
    {
        double FOIR = 0.5;
        double temp =0.0;
        double canEMI = amount*FOIR - other;
        if(canEMI > 0)
        {
            if(age-60 > 30)
            {
                age = 30;
            }
            else
            {
                age = age - 60;
            }
            int months = age * 12;
            temp = (canEMI*(Math.pow((1+7.5/1200),months)-1))/(Math.pow((1+7.5/1200),months)*7.5/1200);
        }
        return  temp;
    }
}
