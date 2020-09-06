package com.example.maximo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EMIAdapter extends RecyclerView.Adapter<EMIAdapter.MyViewHolder> {
    private ArrayList<EMIDetails> details;
    private Context context;


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView year;
        public TextView interest;
        public TextView princi;
        public TextView remain;

        public MyViewHolder(View v) {
            super(v);
            year = v.findViewById(R.id.show_emi_year);
            interest = v.findViewById(R.id.show_emi_interest);
            princi = v.findViewById(R.id.show_emi_principal);
            remain = v.findViewById(R.id.show_emi_remaining);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EMIAdapter(Context context,ArrayList<EMIDetails> details) {
        this.details = details;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.emi_details,parent,false));
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.remain.setText(details.get(position).getRemaining());
        holder.princi.setText(details.get(position).getPrincipal());
        holder.interest.setText(details.get(position).getInterest());
        holder.year.setText(details.get(position).getInterest());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return details.size();
    }
}
