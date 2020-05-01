package com.example.dell.cleancare;


import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MeterAdapter extends RecyclerView.Adapter<MeterAdapter.MeterViewHolder> {
    private List<ThingFetch> Meterlist;
    String s;

    public MeterAdapter(List<ThingFetch> MeterList) {
        this.Meterlist = MeterList;
    }

    @NonNull
    @Override
    public MeterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardmeter, viewGroup, false);
        MeterViewHolder meterViewHolder = new MeterViewHolder(view);

        return meterViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull final MeterViewHolder meterViewHolder, int i) {
        final ThingFetch thingfetch = Meterlist.get(i);
        meterViewHolder.count2.setText(String.valueOf(thingfetch.getCounter()));
        meterViewHolder.clean_btn.setBackgroundColor(Color.parseColor("#d3d3d3"));
        meterViewHolder.avg_btn.setBackgroundColor(Color.parseColor("#d3d3d3"));
        meterViewHolder.dirty_btn.setBackgroundColor(Color.parseColor("#D81B60"));

        //meterViewHolder.status.setText();
        try{
            FirebaseDatabase.getInstance().getReference("washroomStatus").child("1").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    s = dataSnapshot.child("status").getValue().toString();
                    if (ThingSpeakFetch.gsensor != "") {
                        if (Float.parseFloat(ThingSpeakFetch.gsensor) < MeterFragment.mindirtythresh) {
                            meterViewHolder.clean_btn.setBackgroundColor(Color.parseColor("#55AD0A"));
                            meterViewHolder.avg_btn.setBackgroundColor(Color.parseColor("#D3D3D3"));
                            meterViewHolder.dirty_btn.setBackgroundColor(Color.parseColor("#D3D3D3"));
                            FirebaseDatabase.getInstance().getReference("washroomStatus").child("1").child("status").setValue("cleaned");


                        }
                        if (String.valueOf(thingfetch.getSensor()).equalsIgnoreCase("2")) {
                            meterViewHolder.clean_btn.setBackgroundColor(Color.parseColor("#D3D3D3"));
                            meterViewHolder.avg_btn.setBackgroundColor(Color.parseColor("#ffdf77"));
                            meterViewHolder.dirty_btn.setBackgroundColor(Color.parseColor("#D3D3D3"));
                        }
                        if (Float.parseFloat(ThingSpeakFetch.gsensor) > MeterFragment.mindirtythresh) {
                            meterViewHolder.clean_btn.setBackgroundColor(Color.parseColor("#D3D3D3"));
                            meterViewHolder.avg_btn.setBackgroundColor(Color.parseColor("#D3D3D3"));
                            meterViewHolder.dirty_btn.setBackgroundColor(Color.parseColor("#D81B60"));
                            MeterFragment fragment = new MeterFragment();

                            FirebaseDatabase.getInstance().getReference("washroomStatus").child("1").child("status").setValue("Uncleaned");
                        }
                        if (String.valueOf(thingfetch.getId()).equalsIgnoreCase("704326")) {
                            meterViewHolder.sensor2.setText("Washroom No. 1");
                        }
                        if (String.valueOf(thingfetch.getId()).equalsIgnoreCase("714434")) {
                            meterViewHolder.sensor2.setText("Washroom No. 2");
                        }
                        setWashroomStatus(meterViewHolder, s);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        catch (Exception e)
        {

        }


        //meterViewHolder.sensor2.setText(String.valueOf(thingfetch.getSensor()));

    }

    private void setWashroomStatus(@NonNull MeterViewHolder meterViewHolder, String s) {
        if(s.equalsIgnoreCase("cleaned"))
        {
            meterViewHolder.status.setText("Cleaned");
        }
        else
        {
            meterViewHolder.status.setText("Not Cleaned");
        }
    }

    @Override
    public int getItemCount() {
        return Meterlist.size();
    }

    public class MeterViewHolder extends RecyclerView.ViewHolder {

        TextView count2, sensor2, status;
        Button clean_btn,avg_btn,dirty_btn;

        public MeterViewHolder(@NonNull View itemView) {
            super(itemView);
            count2 = (TextView) itemView.findViewById(R.id.btn_count);
            sensor2 = (TextView) itemView.findViewById(R.id.name1);
            clean_btn = (Button) itemView.findViewById(R.id.clean);
            avg_btn = (Button) itemView.findViewById(R.id.avg);
            dirty_btn = (Button) itemView.findViewById(R.id.dirty);
            status = (TextView) itemView.findViewById(R.id.status_id);

          }
    }


}
