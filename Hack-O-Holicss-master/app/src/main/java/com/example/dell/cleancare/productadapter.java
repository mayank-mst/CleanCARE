package com.example.dell.cleancare;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class productadapter extends RecyclerView.Adapter<productadapter.ProductViewHolder> {
    private List<product> productList;

    public productadapter(List<product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card, viewGroup, false);
        ProductViewHolder productViewHolder = new ProductViewHolder(view);

        return productViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i) {
        product Product = productList.get(i);
        productViewHolder.name.setText(Product.getName());
        productViewHolder.num.setText(String.valueOf(Product.getNum()));

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView num, name;
        Button skip, clean;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            num = itemView.findViewById(R.id.num);
            name = itemView.findViewById(R.id.name);
        }
    }
}