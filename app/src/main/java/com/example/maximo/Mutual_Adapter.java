package com.example.maximo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Mutual_Adapter extends RecyclerView.Adapter<Mutual_Adapter.MyViewHolder> {

    private ArrayList<MutualFunds> mutualFunds;
    private Context context;


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name;
        public TextView rating;
        public TextView rate1;
        public TextView return1;
        public TextView rate3;
        public TextView return3;
        public TextView rate5;
        public TextView return5;
        public TextView rate10;
        public TextView return10;

        public MyViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.fund_Name);
            rating = v.findViewById(R.id.fund_rating);
            rate1 = v.findViewById(R.id.rate_1);
            rate3 = v.findViewById(R.id.rate_3);
            rate5 = v.findViewById(R.id.rate_5);
            rate10 = v.findViewById(R.id.rate_10);
            return1 = v.findViewById(R.id.return_1);
            return3 = v.findViewById(R.id.return_3);
            return5 = v.findViewById(R.id.return_5);
            return10 = v.findViewById(R.id.return_10);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public Mutual_Adapter(Context context,ArrayList<MutualFunds> mutualFunds) {
        this.mutualFunds = mutualFunds;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.mutual_cards,parent,false));
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
       holder.name.setText(mutualFunds.get(position).getName());
       holder.rating.setText("Rating: " + mutualFunds.get(position).getRating() + "/5");
       if(mutualFunds.get(position).getRate_1().equals("0"))
       {
           holder.rate1.setText("--");
       }
       else
       {
           holder.rate1.setText(mutualFunds.get(position).getRate_1() + "%");
       }
        if(mutualFunds.get(position).getRate_3().equals("0"))
        {
            holder.rate3.setText("--");
        }
        else
        {
            holder.rate3.setText(mutualFunds.get(position).getRate_3() + "%");
        }
        if(mutualFunds.get(position).getRate_5().equals("0"))
        {
            holder.rate5.setText("--");
        }
        else
        {
            holder.rate5.setText(mutualFunds.get(position).getRate_5() + "%");
        }
        if(mutualFunds.get(position).getRate_10().equals("0"))
        {
            holder.rate10.setText("--");
        }
        else
        {
            holder.rate10.setText(mutualFunds.get(position).getRate_10() + "%");
        }

        if(mutualFunds.get(position).getReturn_1().equals("0.0"))
        {
            holder.return1.setText("--");
        }
        else
        {
            holder.return1.setText(mutualFunds.get(position).getReturn_1());
        }
        if(mutualFunds.get(position).getReturn_3().equals("0.0"))
        {
            holder.return3.setText("--");
        }
        else
        {
            holder.return3.setText(mutualFunds.get(position).getReturn_3());
        }
        if(mutualFunds.get(position).getReturn_5().equals("0.0"))
        {
            holder.return5.setText("--");
        }
        else
        {
            holder.return5.setText(mutualFunds.get(position).getReturn_5());
        }
        if(mutualFunds.get(position).getReturn_10().equals("0.0"))
        {
            holder.return10.setText("--");
        }
        else
        {
            holder.return10.setText(mutualFunds.get(position).getReturn_10());
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mutualFunds.size();
    }
}
