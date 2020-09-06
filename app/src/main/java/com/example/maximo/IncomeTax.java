package com.example.maximo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class IncomeTax extends AppCompatActivity {
    public static int net_salary;
    EditText BasicSalary;
    Button next;
    Context context = this;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income);
        BasicSalary = findViewById(R.id.basic_salary);
        next = findViewById(R.id.partnext);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String basic = BasicSalary.getText().toString();
                if(!basic.isEmpty())
                {
                    net_salary = Integer.parseInt(basic) * 12;
                    Intent intent = new Intent(context,IncomeTaxPart1.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(context,"Enter your net salary",Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
