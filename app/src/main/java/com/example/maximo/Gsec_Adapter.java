package com.example.maximo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Gsec_Adapter extends RecyclerView.Adapter<Gsec_Adapter.MyViewHolder> {
    private ArrayList<GSec> bonds;
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
            Name = v.findViewById(R.id.gsec_Name);
            value = v.findViewById(R.id.gsec_bid);
            rate = v.findViewById(R.id.gsec_coupon);
            years = v.findViewById(R.id.gsec_years);
            ytm = v.findViewById(R.id.gsec_isn);
            coupon = v.findViewById(R.id.gsec_coupon_month);
            total = v.findViewById(R.id.gsec_total_coupon);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public Gsec_Adapter(Context context,ArrayList<GSec> bonds) {
        this.bonds = bonds;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_gsec,parent,false));
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.Name.setText(bonds.get(position).getName());
        holder.years.setText("Matures in " + bonds.get(position).getMaturity() + " years");
        holder.rate.setText("Coupon rate:" + bonds.get(position).getCoupon() + "%");
        holder.value.setText("Bid: " + bonds.get(position).getBid());
        holder.ytm.setText("ISIN: " + bonds.get(position).getIsn());
        float r = Float.parseFloat(bonds.get(position).getCoupon());
        float year_coupon = (GovSec.invest * r) / 100;
        float coupon = (year_coupon * 6 / 12) * GovSec.no_of_bonds;
        holder.coupon.setText("Coupon Payment of Rs. "+ String.valueOf(coupon)+ " every 12 months");
        float total_coupon = (year_coupon * (Integer.parseInt(bonds.get(position).getMaturity())*12)/12) * GovSec.no_of_bonds;
        holder.total.setText("Total Coupon over the maturity: Rs. " + String.valueOf(total_coupon));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return bonds.size();
    }
}
