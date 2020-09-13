package com.example.maximo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Corp_Adapter extends RecyclerView.Adapter<Corp_Adapter.MyViewHolder>{
    private ArrayList<CorpBonds> bonds;
    private Context context;


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView Name;
        public TextView value;
        public TextView rate;
        public TextView years;
        public TextView ytm;
        public TextView coupon;
        public TextView total;

        public MyViewHolder(View v) {
            super(v);
            Name = v.findViewById(R.id.corp_Name);
            value = v.findViewById(R.id.corp_nav);
            rate = v.findViewById(R.id.corp_coupon);
            years = v.findViewById(R.id.corp_risk);
            ytm = v.findViewById(R.id.corp_rating);
            coupon = v.findViewById(R.id.corp_coupon_month);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public Corp_Adapter(Context context,ArrayList<CorpBonds> bonds) {
        this.bonds = bonds;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_corp_bonds,parent,false));
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.Name.setText(bonds.get(position).getName());
        holder.years.setText("Risk: " + bonds.get(position).getRisk());
        holder.rate.setText("Return rate:" + bonds.get(position).getCoupon() + "%");
        holder.value.setText("Latest NAV: " + bonds.get(position).getNav());
        holder.ytm.setText("Rating: " + bonds.get(position).getRating());
        float r = Float.parseFloat(bonds.get(position).getCoupon());
        Log.d("Corp",String.valueOf(r));
        r = (1+r)/100;
        Log.d("Corp",String.valueOf(r));
        double x =r*Corp_Bonds.invest;
        Log.d("Corp",String.valueOf(x));
        Log.d("Corp",String.valueOf(Corp_Bonds.year));
        double amount = Math.pow(x,Double.parseDouble(String.valueOf(Corp_Bonds.year)));
        holder.coupon.setText("Future Value of the Bonds: Rs. " + BigDecimal.valueOf(amount).toPlainString());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return bonds.size();
    }
}
