package com.example.dell.cleancare;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class home_adapter extends RecyclerView.Adapter<home_adapter.HomeViewHolder> {
    private List<homelist>homeList;

    public home_adapter(List<homelist> homeList) {
        this.homeList = homeList;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardhome, viewGroup, false);
        HomeViewHolder homeViewHolder = new HomeViewHolder(view);

        return homeViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder homeViewHolder, int i) {
        homelist Product = homeList.get(i);
        homeViewHolder.name2.setText(Product.getName_home());
        homeViewHolder.time_work.setText(String.valueOf(Product.getWork_time()));

    }

    @Override
    public int getItemCount() {
        return homeList.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {

        TextView time_work, name2;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            time_work = itemView.findViewById(R.id.work_time);
            name2 = itemView.findViewById(R.id.name_home);
        }
    }
}