package com.example.dell.cleancare;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Homefragmentworker extends Fragment {
    private FirebaseAuth firebaseAuth;
    String washroom,time;
    RecyclerView recyclerView2;
    home_adapter adapter2;
    List<homelist> homelists;
    String id;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_worker,container,false);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String email = user.getEmail();
        String newemail = email.replaceAll("@","");
        String nemail = newemail.replaceAll("\\.","");
        String mail = nemail.replaceAll("gmailcom","");


        //FirebaseDatabase.getInstance().getReference("email").child(mail);
        FirebaseDatabase.getInstance().getReference("email").child(mail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    try {
                        Map<String,String> map = (Map)data.getValue();

                        washroom = map.get("Washroom");
                        time = map.get("Time");

                        homelists.add(new homelist(washroom, time));




                    }
                    catch (NullPointerException e)
                    {

                    }
                }


                adapter2 = new home_adapter(homelists);
                recyclerView2.setAdapter(adapter2);




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;

    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homelists = new ArrayList<>();
        recyclerView2 = (RecyclerView) view.findViewById(R.id.recyclerViewhome);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
//        homelists.add(new homelist(washroom,time));
//
//        adapter2 = new home_adapter(homelists);
//        recyclerView2.setAdapter(adapter2);

    }


}
