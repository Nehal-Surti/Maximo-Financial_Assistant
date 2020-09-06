package com.example.maximo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class IncomeTaxPart1 extends AppCompatActivity {

    EditText RentReceived;
    EditText PropertyTax;
    EditText InterestHomeLoan;
    EditText VacantMonths;
    TextView Information;
    Button Next;
    Context context = this;
    public static int income_home_property;
    int rent_received;
    int property_tax;
    int interest;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income_part1);
        Information = findViewById(R.id.hpinfo);
        RentReceived = findViewById(R.id.rent_received);
        PropertyTax = findViewById(R.id.property_tax);
        VacantMonths = findViewById(R.id.vacant);
        InterestHomeLoan = findViewById(R.id.interest_home_loan);
        Next = findViewById(R.id.part1next);

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int select = onRadioButtonClicked(v);
                if(select==9)
                {
                    Intent intent1 = new Intent(context,IncomeTaxPart2.class);
                    startActivity(intent1);
                }
                else if(select==0)
                {
                    String homeloan = InterestHomeLoan.getText().toString();
                        if(!homeloan.isEmpty())
                        {
                            interest = Integer.parseInt(homeloan) * 12;
                            if(interest > 200000)
                            {
                                income_home_property = -200000;
                            }
                            else {
                                 income_home_property = -interest;
                                }
                        }
                        else
                        {
                            Toast.makeText(context,"Please enter your monthly home loan interest or enter 0",Toast.LENGTH_LONG).show();
                        }
                }
                else if(select==1)
                {
                    String homeloan = InterestHomeLoan.getText().toString();
                    String property = PropertyTax.getText().toString();
                    String rentamount = RentReceived.getText().toString();
                    String vacant = VacantMonths.getText().toString();
                    if(!rentamount.isEmpty())
                        {
                            int vacate = Integer.parseInt(vacant);
                            rent_received = Integer.parseInt(rentamount) * (12-vacate);
                            if(!property.isEmpty())
                            {
                                property_tax = Integer.parseInt(property) * 12;
                                if(!homeloan.isEmpty())
                                {
                                    interest = Integer.parseInt(homeloan) * 12;
                                    income_home_property = rent_received - property_tax - interest;
                                    IncomeTax.net_salary = IncomeTax.net_salary + income_home_property;
                                    Intent intent1 = new Intent(context,IncomeTaxPart2.class);
                                    startActivity(intent1);
                                }
                                else
                                {
                                    Toast.makeText(context,"Please enter your monthly home loan interest or enter 0",Toast.LENGTH_LONG).show();
                                }
                            }
                            else {
                                Toast.makeText(context,"Please enter your monthly property tax paid",Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            Toast.makeText(context,"Please enter your monthly rent recieved",Toast.LENGTH_LONG).show();
                        }
                }
            }
        });

        Information.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String url = "https://economictimes.indiatimes.com/wealth/tax/how-to-calculate-income-from-house-property-for-income-tax-purposes/articleshow/58528370.cms";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return false;
            }
        });
    }

    public int onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        int id = 9;
        switch(view.getId()) {
            case R.id.rent:
                if (checked)
                    id = 1;
                    break;
            case R.id.self:
                if (checked)
                    id = 0;
                    break;
        }
        return id;
    }
}
