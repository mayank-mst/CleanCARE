package com.example.dell.cleancare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profilefragment extends Fragment {
    private FirebaseAuth firebaseAuth;



    private TextView profile_id;
    private TextView profile_des;
    private Button logout;
    private Button f1;
    RecyclerView recyclerView3;
    feedadapter adapter;
    List<flist> productList;
    String cleancount,electriccount,plumbingcount,tissuecount;
    String email;
    String floo;
    String wa;
    Button reset;
    String issue_time;
    String issue_count;
    String sys_time;
    int issue_time_d;
    int sys_time_d;
    String sms;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_profile,container,false);
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null)
        {
            getActivity().finish();
            startActivity(new Intent(getActivity(),MainActivity.class));
        }
        FirebaseUser user = firebaseAuth.getCurrentUser();
        profile_id = (TextView) v.findViewById(R.id.profile_id);
        profile_id.setText("Welcome "+user.getEmail());



//modif


        logout = (Button) v.findViewById(R.id.logout);
        reset = (Button) v.findViewById(R.id.reset_id);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == logout)
                {
                    firebaseAuth.signOut();
                    getActivity().finish();
                    startActivity(new Intent(getActivity(),MainActivity.class));
                }


            }
        });
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "").substring(0, FirebaseAuth.getInstance().getCurrentUser().getEmail().indexOf('@'))).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                floo = (String) dataSnapshot.child("Floor").getValue();
                if(floo.equalsIgnoreCase("1"))
                {
                    wa = "1";
                    reset(wa);
                }
                else if(floo.equalsIgnoreCase("2"))
                {
                    wa = "2";
                    reset(wa);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //modifications from here



        return v;
    }






    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productList = new ArrayList<>();
        recyclerView3 = (RecyclerView) view.findViewById(R.id.recyclerView3);
        recyclerView3.setHasFixedSize(true);
        recyclerView3.setLayoutManager(new LinearLayoutManager(getContext()));


    }


    public void reset(final String wa) {
        FirebaseDatabase.getInstance().getReference("feedback-counts").child(wa).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                issue_count = dataSnapshot.child("issueCount").getValue().toString();

                issue_time = dataSnapshot.child("issueTime").getValue().toString();
//               issue_time_d = Integer.parseInt(issue_time);
//               issue_time_d = issue_time_d % 100;
                long difference = 0;




//                sys_time_d = Integer.parseInt(sys_time);
//                sys_time_d = sys_time_d%100;


//                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                //sys_time = sdf.format(new Date());
//                issue_time.replaceAll(":","");
//                sys_time.replaceAll(":","");
//                issue_time_d = Double.parseDouble(issue_time);
//                sys_time_d = Double.parseDouble(sys_time
                while (difference<=20000) {
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    sys_time = sdf.format(cal.getTime());
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    Date date1 = null;
                    try {
                        date1 = format.parse(issue_time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Date date2 = null;
                    try {
                        date2 = format.parse(sys_time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    difference = date2.getTime() - date1.getTime();
                    // difference = difference*1000;

                    reset.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map<String, String> dummy = new HashMap<>();
                            dummy.put("issueCount", "");
                            dummy.put("issueTime", "");
                            FirebaseDatabase.getInstance().getReference("feedback-counts").child(wa).setValue(dummy);
                        }
                    });
                }


                if(difference>20000)
                {
                    sms = "Floor Number : "+floo+" Washrrom Number : "+wa+" Issue Count : "+issue_count;
                    SmsManager smsManager = SmsManager.getDefault();
                    ArrayList<String> parts = smsManager.divideMessage(sms);
                    smsManager.sendMultipartTextMessage("9940502913", null, parts, null, null);
                }



            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }}
