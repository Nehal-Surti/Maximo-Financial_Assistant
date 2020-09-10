package com.example.maximo;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Stock_Goal_Input extends AppCompatActivity {
    EditText Target,Year;
    Button Recommend;
    Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_goal);
        Target = findViewById(R.id.target);
        Year = findViewById(R.id.stock_year);
        Recommend = findViewById(R.id.recommend);

        Recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tar = Target.getText().toString();
                String year = Year.getText().toString();

                if(tar.isEmpty() || year.isEmpty())
                {
                    Toast.makeText(context,"Please fill the information",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
