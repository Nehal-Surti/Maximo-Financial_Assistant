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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class IncomeTaxPart2 extends AppCompatActivity {
    public int deduced;
    public int mediclaim;
    public int disable_dependent;
    public int disable;
    Button next;
    LinearLayout deductions;
    EditText LIC;
    EditText PPF;
    EditText Principal_House;
    EditText Pension;
    EditText NPS;
    EditText Education;
    EditText Mediclaim;
    EditText Disable_Dependent;
    int disableClaim=0;
    Context context = this;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income_part2);
        next = findViewById(R.id.part2next);
        deductions = findViewById(R.id.deductions);
        LIC = findViewById(R.id.LIC);
        PPF = findViewById(R.id.PPF);
        Principal_House = findViewById(R.id.principal);
        Pension = findViewById(R.id.pension);
        NPS = findViewById(R.id.nps);
        Education = findViewById(R.id.education);
        Mediclaim = findViewById(R.id.mediclaim);
        Disable_Dependent = findViewById(R.id.disable);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deduced==0)
                {
                    deductions.setVisibility(View.VISIBLE);
                    int C = getSection80C();
                    int NPS = getNPS();
                    int education_loan = getEducation();
                    int medi_claim = getMediclaim();
                    if(disable==1)
                    {
                        disableClaim = 75000;
                    }
                    else if(disableClaim==0)
                    {
                        disableClaim = getDisable_dependent();
                    }

                    int total_deductions = C + NPS + education_loan + medi_claim + disableClaim;
                    Log.d("BBB",String.valueOf(total_deductions));
                    IncomeTax.net_salary = IncomeTax.net_salary - total_deductions;
                    Log.d("BBB",String.valueOf(IncomeTax.net_salary));
                    Intent intent = new Intent(context,IncomeFinal.class);
                    startActivity(intent);
                }
            }
        });
    }

    public int getDisable_dependent()
    {
        String dependent = Disable_Dependent.getText().toString();
        if(!dependent.isEmpty())
        {
            int d = Integer.parseInt(dependent);
            if(disable_dependent==1)
            {
                return Math.min(100000,d);
            }
            else if(disable_dependent==0)
            {
                return Math.min(d,40000);
            }
        }
        return 0;
    }

    public int getMediclaim()
    {
      String claim = Mediclaim.getText().toString();
      int medi = 0;
      if(!claim.isEmpty())
      {
          medi = Integer.parseInt(claim);
          if(mediclaim==0)
          {
              return Math.min(medi,25000);
          }
          else if(mediclaim==1)
          {
              return Math.min(medi,50000);
          }
      }
      return medi;
    }
    public int getEducation()
    {
        String loan = Education.getText().toString();
        if(loan.isEmpty())
        {
            return 0;
        }

        return Integer.parseInt(loan);
    }

    public int getNPS()
    {
        int np;
        String nps = NPS.getText().toString();
        if(nps.isEmpty())
        {
            return 0;
        }
        else
        {
           np = Integer.parseInt(nps);
        }
        return Math.min(np,50000);
    }

    public int getSection80C()
    {
        int C1,C2,C3,C4;
        String lic = LIC.getText().toString();
        String ppf = PPF.getText().toString();
        String principal = Principal_House.getText().toString();
        String pension = Pension.getText().toString();
        if(lic.isEmpty())
        {
            C1 = 0;
        }
        else
        {
            C1 = Integer.parseInt(lic);
        }

        if(ppf.isEmpty())
        {
            C2 = 0;
        }
        else
        {
            C2 = Integer.parseInt(ppf);
        }

        if(principal.isEmpty())
        {
            C3 = 0;
        }
        else
        {
            C3 = Integer.parseInt(principal);
        }

        if(pension.isEmpty())
        {
            C4 = 0;
        }
        else
        {
            C4 = Integer.parseInt(pension);
        }
        int temp = (C1+C2+C3+C4);
        return Math.min(temp, 150000);
    }

    public void onDeduced(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.deducedYes:
                if (checked) {
                    deduced = 1;
                    deductions.setVisibility(View.GONE);
                }
                break;
            case R.id.deducedNo:
                if (checked) {
                    deduced = 0;
                    deductions.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    public void onMediclaim(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.above60_medical:
                if (checked)
                    mediclaim = 1;
                break;
            case R.id.below60_medical:
                if (checked)
                    mediclaim = 0;
                break;
        }
    }

    public void onDisableDependent(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.above60_disable:
                if (checked)
                    disable_dependent = 1;
                break;
            case R.id.below60_disable:
                if (checked)
                    disable_dependent = 0;
                break;
        }
    }

    public void onDisable(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.self_yes:
                if (checked)
                    disable = 1;
                break;
            case R.id.self_no:
                if (checked)
                    disable = 0;
                break;
        }
    }

}