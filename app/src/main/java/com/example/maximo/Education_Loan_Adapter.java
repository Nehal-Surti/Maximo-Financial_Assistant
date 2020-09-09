package com.example.maximo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Education_Loan_Adapter extends RecyclerView.Adapter<Education_Loan_Adapter.MyViewHolder> {
    private ArrayList<Education_Loan> loans;
    private Context context;


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView bankName;
        public TextView india;
        public TextView abroad;
        public Button emi;

        public MyViewHolder(View v) {
            super(v);
            bankName = v.findViewById(R.id.EduBankName);
            india = v.findViewById(R.id.IndiaInterest);
            abroad = v.findViewById(R.id.AbroadInterest);
            emi = v.findViewById(R.id.check_edu_emi);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public Education_Loan_Adapter(Context context,ArrayList<Education_Loan> loans) {
        this.loans = loans;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_education_loan,parent,false));
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
       holder.bankName.setText(loans.get(position).getBank());
       holder.india.setText("India Rate: " + loans.get(position).getIndiaRate());
       holder.abroad.setText("Abroad Rate: " + loans.get(position).getAbroadRate());
        holder.emi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("India",loans.get(position).getIndiaRate());
                bundle.putString("Abroad",loans.get(position).getAbroadRate());
                Intent intent = new Intent(context,EducationEMICalculator.class);
                intent.putExtra("rates",bundle);
                context.startActivity(intent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return loans.size();
    }
}
