package com.example.dell.cleancare;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class dashAdapter extends RecyclerView.Adapter<dashAdapter.DashViewHolder> {
    private List<dashlist> productList;
    CardView card1;

    public dashAdapter(List<dashlist> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public DashViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_dash, viewGroup, false);
        DashViewHolder productViewHolder = new DashViewHolder(view);

        return productViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull DashViewHolder productViewHolder, int i) {
        dashlist Product = productList.get(i);
        productViewHolder.num1.setText(Product.getLast_cleaned_at());
        productViewHolder.num2.setText(Product.getNo_of_time());
        productViewHolder.num3.setText(Product.getNissue());
        productViewHolder.num4.setText(Product.getCount());
        productViewHolder.washroom.setText(Product.getWash());
            if (productViewHolder.washroom.getText().equals("Washroom 1")) {
                card1.setCardBackgroundColor(Color.parseColor("#009933"));
            } else {
                card1.setCardBackgroundColor(Color.parseColor("#ff6666"));

        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class DashViewHolder extends RecyclerView.ViewHolder {

        TextView num1, num2, num3, num4,washroom;


        public DashViewHolder(@NonNull View itemView) {
            super(itemView);
            num1= itemView.findViewById(R.id.cleaned_at_val);
            num2= itemView.findViewById(R.id.cleaned_num_val);
            num3= itemView.findViewById(R.id.issuenum_val);
            num4= itemView.findViewById(R.id.btn_count1);
            card1=itemView.findViewById(R.id.carddash);
            washroom=itemView.findViewById(R.id.wash_id);
        }
    }
}