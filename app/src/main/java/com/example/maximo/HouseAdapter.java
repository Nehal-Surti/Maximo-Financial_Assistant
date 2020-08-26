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

public class HouseAdapter extends RecyclerView.Adapter<HouseAdapter.MyViewHolder>  {

    private ArrayList<House> houses;
    private Context context;


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView areaView;
        public TextView flatView;
        public TextView priceView;
        public TextView carpetView;
        public TextView bathView;
        public Button clickDetails;

        public MyViewHolder(View v) {
            super(v);
            areaView = v.findViewById(R.id.area_name);
            flatView = v.findViewById(R.id.flat_name);
            priceView = v.findViewById(R.id.price);
            carpetView = v.findViewById(R.id.carpet);
            bathView = v.findViewById(R.id.bath);
            clickDetails = v.findViewById(R.id.clickDetails);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public HouseAdapter(Context context,ArrayList<House> houses) {
        this.houses = houses;
        context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.house_item,parent,false));
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.areaView.setText(houses.get(position).getAreaName());
        holder.flatView.setText(houses.get(position).getFlatName());
        holder.priceView.setText(houses.get(position).getPrice());
        holder.carpetView.setText(houses.get(position).getCarpet());
        holder.bathView.setText(houses.get(position).getBathrooms());
        holder.clickDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("Area",houses.get(position).getAreaName());
                bundle.putString("Flat",houses.get(position).getFlatName());
                bundle.putString("Price",houses.get(position).getPrice());
                bundle.putString("Carpet",houses.get(position).getCarpet());
                bundle.putString("Description",houses.get(position).getDescription());
                Intent intent = new Intent(context,HousePrediction.class);
                intent.putExtra("house",bundle);
                context.startActivity(intent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return houses.size();
    }
}
