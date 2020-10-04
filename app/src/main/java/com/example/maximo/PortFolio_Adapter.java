package com.example.maximo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PortFolio_Adapter extends RecyclerView.Adapter<PortFolio_Adapter.MyViewHolder> {
    private ArrayList<String> ports;
    private Context context;


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView portName;
        public TextView portDesc;
        public Button portExplore;
        LinearLayout linearLayout;

        public MyViewHolder(View v) {
            super(v);
            linearLayout = v.findViewById(R.id.port_layout);
            portName = v.findViewById(R.id.port_Name);
            portDesc = v.findViewById(R.id.port_desc);
            portExplore = v.findViewById(R.id.port_explore);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PortFolio_Adapter(Context context,ArrayList<String> ports) {
        this.ports = ports;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.portfolio_cards,parent,false));
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.portName.setText("");
        holder.portDesc.setText(ports.get(position));
        if(ports.get(position).contains("Gold"))
        {
            holder.linearLayout.setBackgroundResource(R.drawable.gold_layer);
        }
        if(ports.get(position).contains("Bonds"))
        {
            holder.linearLayout.setBackgroundResource(R.drawable.bond_layer);
        }
        if(ports.get(position).contains("Fixed"))
        {
            holder.linearLayout.setBackgroundResource(R.drawable.fd_layer);
        }
        if(ports.get(position).contains("stocks"))
        {
            holder.linearLayout.setBackgroundResource(R.drawable.stock_layer);
        }
        if(ports.get(position).contains("Mutual"))
        {
            holder.linearLayout.setBackgroundResource(R.drawable.mutual_layer);
        }
        holder.portExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return ports.size();
    }
}
