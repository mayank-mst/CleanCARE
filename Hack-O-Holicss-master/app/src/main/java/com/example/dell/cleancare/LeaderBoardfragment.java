package com.example.dell.cleancare;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LeaderBoardfragment extends Fragment {
    RecyclerView recyclerView3;
    dashAdapter adapter;
    List<dashlist> productList;
    String lastcleantime;
    String status1;
    String ccount;
    String icount;
    String lastcleantime2;
    String status12;
    String ccount2;
    String icount2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_leaderboard,container,false);
        FirebaseDatabase.getInstance().getReference("washroomStatus").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren())
                {
                    if(data.getKey().equals("1"))
                    {
                        lastcleantime=data.child("lastCleanedTime").getValue().toString();
                        status1=data.child("status").getValue().toString();



                    }

                    if(data.getKey().equals("2"))
                    {
                        lastcleantime2=data.child("lastCleanedTime").getValue().toString();
                        status12=data.child("status").getValue().toString();

                    }

                }
                /*adapter = new dashAdapter(dashlist1);
                recyclerViewl.setAdapter(adapter);
*/


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference("feedback-counts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data1:dataSnapshot.getChildren())
                {
                    if(data1.getKey().equals("washroom1"))
                    {
                        ccount=data1.child("cleanedCount").getValue().toString();
                        icount=data1.child("issueCount").getValue().toString();

                    }

                    if(data1.getKey().equals("washroom2"))
                    {
                        ccount2=data1.child("cleanedCount").getValue().toString();
                        icount2=data1.child("issueCount").getValue().toString();

                    }

                    /*dashlist1.add(new dashlist(lastcleantime,ccount,icount,ThingSpeakFetch.counter));
                    //Set<dashlist> set=new HashSet<dashlist>();
                        adapter = new dashAdapter(dashlist1);
                        recyclerViewl.setAdapter(adapter);
*/


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;
        
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productList = new ArrayList<>();
        recyclerView3 = (RecyclerView) view.findViewById(R.id.recyclerViewl);
        recyclerView3.setHasFixedSize(true);
        recyclerView3.setLayoutManager(new LinearLayoutManager(getContext()));
        productList.add(new dashlist("Washroom 1","11:00 am","5","6","7"));
        productList.add(new dashlist("Washroom 2","1:00 pm", "4", "3","9"));
        adapter = new dashAdapter(productList);
        recyclerView3.setAdapter(adapter);
    }
}
