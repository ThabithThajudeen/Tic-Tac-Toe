package com.example.mad_assignment1_final;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    public MyAdapter(Context context, List<PlayerData> items) {
        //  super();
        this.context = context;
        this.items = items;
    }

    Context context;
    List<PlayerData> items;



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nameView.setText(items.get(position).getPlayerName());
        holder.nameView2.setText(String.valueOf("Wins" + " " + items.get(position).getWins()));
        holder.nameView3.setText(String.valueOf("Losses" + " " + items.get(position).getLosses()));
        holder.nameView4.setText(String.valueOf("Draws" +  " " + items.get(position).getDraws()));
        holder.nameView5.setText(String.valueOf("Total Games Played" + " " + items.get(position).getTotalGames()));
        holder.nameView6.setText(String.valueOf("Win Percentage" + " "  + items.get(position).getWinPercentage()));



    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
