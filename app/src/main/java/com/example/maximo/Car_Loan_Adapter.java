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

public class Car_Loan_Adapter extends RecyclerView.Adapter<Car_Loan_Adapter.MyViewHolder> {
    private ArrayList<Car_Loan> loans;
    private Context context;


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView bankName;
        public TextView fix;
        public TextView rate;
        public TextView tenure;
        public TextView road;
        public TextView ltv;
        public Button emi;

        public MyViewHolder(View v) {
            super(v);
            bankName = v.findViewById(R.id.cartextViewBankName);
            fix = v.findViewById(R.id.fix);
            rate = v.findViewById(R.id.cartextViewInterest);
            tenure = v.findViewById(R.id.carmaxtenure);
            road = v.findViewById(R.id.road);
            ltv = v.findViewById(R.id.cartextViewLVR);
            emi = v.findViewById(R.id.check_car_emi);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public Car_Loan_Adapter(Context context,ArrayList<Car_Loan> loans) {
        this.loans = loans;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_car_loan,parent,false));
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.bankName.setText(loans.get(position).getBank());
        holder.ltv.setText("Loan-to-value: " +loans.get(position).getLtv());
        holder.fix.setText(loans.get(position).getFix());
        holder.tenure.setText("Max Tenure: " +loans.get(position).getMaxTenure());
        holder.road.setText(loans.get(position).getRoad());
        holder.rate.setText("Interest: "+ loans.get(position).getRate()+"% p.a");
        holder.emi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("rate",loans.get(position).getRate());
                bundle.putString("tenure",loans.get(position).getMaxTenure().split(" ")[0]);
                bundle.putString("road",loans.get(position).getRoad());
                bundle.putString("ltv",loans.get(position).getLtv());
                Intent intent = new Intent(context,CarEMICalculator.class);
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
