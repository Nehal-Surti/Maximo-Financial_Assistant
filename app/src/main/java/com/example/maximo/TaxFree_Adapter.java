package com.example.maximo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;

public class TaxFree_Adapter extends RecyclerView.Adapter<TaxFree_Adapter.MyViewHolder> {
    private ArrayList<TaxFreeBonds> bonds;
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
            Name = v.findViewById(R.id.tax_Name);
            value = v.findViewById(R.id.tax_value);
            rate = v.findViewById(R.id.tax_coupon);
            years = v.findViewById(R.id.tax_years);
            ytm = v.findViewById(R.id.tax_ytm);
            coupon = v.findViewById(R.id.tax_coupon_month);
            total = v.findViewById(R.id.tax_total_coupon);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TaxFree_Adapter(Context context,ArrayList<TaxFreeBonds> bonds) {
        this.bonds = bonds;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_tax_bonds,parent,false));
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
       holder.Name.setText(bonds.get(position).getName());
       holder.years.setText("Matures in " + bonds.get(position).getMaturity() + " years");
       holder.rate.setText("Coupon rate:" + bonds.get(position).getCoupon() + "%");
       holder.value.setText("Buyin Value: Rs. " + String.valueOf(Double.parseDouble(bonds.get(position).getValue().replace(",",""))*Tax_Free.no_of_bonds));
       holder.ytm.setText("Yield-to-Maturity: " + bonds.get(position).getYtm());
       float r = Float.parseFloat(bonds.get(position).getCoupon());
       float year_coupon = (1000 * r) / 100;
       float coupon = (year_coupon * 12 / 12) * Tax_Free.no_of_bonds;
       holder.coupon.setText("Coupon Payment of Rs. "+ String.valueOf(coupon)+ " every 12 months");
       float total_coupon = (year_coupon * (Integer.parseInt(bonds.get(position).getMaturity()) *12) / 12) * Tax_Free.no_of_bonds;
       holder.total.setText("Total Coupon over the maturity: Rs. " + String.valueOf(total_coupon));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return bonds.size();
    }
}
