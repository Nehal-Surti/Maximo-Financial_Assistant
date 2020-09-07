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

public class Home_Loan_Adapter extends RecyclerView.Adapter<Home_Loan_Adapter.MyViewHolder> {
    private ArrayList<Home_Loan> loans;
    private Context context;


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView bankName;
        public TextView fee;
        public TextView rate;
        public TextView tenure;
        public TextView age;
        public TextView ltv;
        public Button emi;

        public MyViewHolder(View v) {
            super(v);
            bankName = v.findViewById(R.id.textViewBankName);
            fee = v.findViewById(R.id.textViewRrocessing);
            rate = v.findViewById(R.id.textViewInterest);
            tenure = v.findViewById(R.id.maxtenure);
            age = v.findViewById(R.id.maxAge);
            ltv = v.findViewById(R.id.textViewLVR);
            emi = v.findViewById(R.id.check_emi);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public Home_Loan_Adapter(Context context,ArrayList<Home_Loan> loans) {
        this.loans = loans;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_home_loan,parent,false));
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.bankName.setText(loans.get(position).getBank());
        holder.ltv.setText(loans.get(position).getLtv());
        holder.age.setText(loans.get(position).getMaxAge());
        holder.tenure.setText(loans.get(position).getMaxTenure());
        holder.fee.setText(loans.get(position).getFee());
        holder.rate.setText(loans.get(position).getRate());
        holder.emi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("rate",loans.get(position).getRate());
                bundle.putString("tenure",loans.get(position).getMaxTenure());
                bundle.putString("ltv",loans.get(position).getLtv());
                Intent intent = new Intent(context,EducationEMICalculator.class);
                intent.putExtra("other",bundle);
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
