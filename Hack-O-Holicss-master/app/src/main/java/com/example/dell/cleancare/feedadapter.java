
package com.example.dell.cleancare;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class feedadapter extends RecyclerView.Adapter<feedadapter.FeedViewHolder> {
    private List<flist> productList;

    public feedadapter(List<flist> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.feedcard, viewGroup, false);
        FeedViewHolder feedViewHolder = new FeedViewHolder(view);

        return feedViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder productViewHolder, int i) {
        flist Product = productList.get(i);
       // productViewHolder.name.setText(Product.getNum());
        productViewHolder.num1.setText(String.valueOf(Product.getNum1()));
        productViewHolder.num2.setText(String.valueOf(Product.getNum2()));
        productViewHolder.num3.setText(String.valueOf(Product.getNum3()));
        productViewHolder.num4.setText(String.valueOf(Product.getNum4()));

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class FeedViewHolder extends RecyclerView.ViewHolder {

        TextView num1,num2,num3,num4, name;
       // Button skip, clean;

        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);

            num1 = itemView.findViewById(R.id.clean_num);
            num2 = itemView.findViewById(R.id.elect_num);
            num3 = itemView.findViewById(R.id.plum_num);
            num4 = itemView.findViewById(R.id.tissue_num);


        }
    }
}