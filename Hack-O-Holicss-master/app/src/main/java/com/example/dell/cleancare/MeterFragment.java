package com.example.dell.cleancare;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.ArrayList;
import java.util.List;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MeterFragment extends Fragment implements IUpdateReadings {
    RecyclerView recyclerView1;
    MeterAdapter madapter;
    List<ThingFetch> meterjList;
    IUpdateReadings updateReadings;
    private FirebaseAuth firebaseAuth;
    String e;
    String f;
    String id;
    int t = 0;
    int t2 = 0;
    String floorId;
    String washroomId;
    String phone;
    Button settings_btn;
    String pplmax,gasmax;

    double minavgthresh =  0.0;
    double mincleanthresh = 0.0;
    static Double mindirtythresh = 0.0;
    double maxavgthresh , maxcleanthresh , maxdirtythresh;

    public static Fragment newInstance1() {
        Fragment frag = new MeterFragment();
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_meter, container, false);
        settings_btn=(Button)v.findViewById(R.id.settingsbutton);
        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),meter_settings.class);
                startActivity(intent);
            }
        });
        updateReadings = (IUpdateReadings) this;
        FirebaseDatabase.getInstance().getReference("threshold").child("dummy").setValue("dummy");
        FirebaseDatabase.getInstance().getReference("threshold").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()) {
                    if (data.getKey().equals(" 704326")) {
                        gasmax = data.child("gasthresh").getValue().toString();
                        pplmax = data.child("pplthresh").getValue().toString();

                        mindirtythresh = Double.parseDouble(gasmax);

                        /*gasmax= map4.get("gasthresh");
                    pplmax = map4.get("pplthresh");*/
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getThingSpeakData(view, "");

            }
        }, 100);

        FirebaseDatabase.getInstance().getReference("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    if (data.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "").substring(0, FirebaseAuth.getInstance().getCurrentUser().getEmail().indexOf("@")))) {
                        floorId = data.child("Floor").getValue().toString();
                        getWashroomId(floorId, view);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        System.out.println(washroomId);




        FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "").substring(0, FirebaseAuth.getInstance().getCurrentUser().getEmail().indexOf("@")))
                .child("triggerSms").setValue("");
        FirebaseDatabase.getInstance().getReference("Floors")
                .child("triggerSms").setValue("");




    }
    public void getWashroomId(final String floorId, final View view) {
        FirebaseDatabase.getInstance().getReference("Floors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                meterjList = new ArrayList<>();
                recyclerView1 = (RecyclerView) view.findViewById(R.id.recyclerView1);
                recyclerView1.setHasFixedSize(true);
                recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));


                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    if(data.getKey().equals(floorId)) {
                        washroomId = data.getValue().toString().replace("{","").replace("}", "").replace("ID=", "").substring(17);

                        phone = data.getValue().toString().replace("{","").replace("}", "").replace("ID=", "").substring(6, 16);

                        if(washroomId.equals(" 704326")) {
                            meterjList.add(new ThingFetch(ThingSpeakFetch.id, Integer.toString(t), ThingSpeakFetch.counter));
                        } else {
                            meterjList.add(new ThingFetch(ThingSpeakFetch2.id, Integer.toString(t2), ThingSpeakFetch2.counter));
                        }
                        madapter = new MeterAdapter(meterjList);
                        recyclerView1.setAdapter(madapter);
                        getThingSpeakData(view, washroomId);

                    }}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void getThingSpeakData(View view, final String washroomId) {

        //   final float minavgthresh = (float) 1.0, maxavgthresh = (float) 2.0, mincleanthresh = (float) 0.0, maxcleanthresh = (float) 1.0, mindirtythresh = (float) 2.0, maxdirtythresh = (float) 100000.0;
        meterjList = new ArrayList<>();
        recyclerView1 = (RecyclerView) view.findViewById(R.id.recyclerView1);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 20 milliseconds
                ThingSpeakFetch process = new ThingSpeakFetch();
                process.execute();
                ThingSpeakFetch2 process2 = new ThingSpeakFetch2();
                process2.execute();
                handler.postDelayed(this, 3000);


            }
        }, 20);
        try {

            float sensor = (float) 0.0;
            String washroomName = "";

            if(washroomId.equals(" 704326")) {
                sensor = Float.parseFloat(ThingSpeakFetch.gsensor);
            } else if(washroomId.equals("714434")) {
                sensor = Float.parseFloat(ThingSpeakFetch2.gsensor);
            }


           if(sensor < mindirtythresh) {
               t=1;
           }
           if(ThingSpeakFetch.gsensor != "") {
            if ((Double.parseDouble(ThingSpeakFetch.gsensor) > mindirtythresh) || Integer.parseInt(ThingSpeakFetch.counter) > Integer.parseInt(pplmax)) {

                String alert = "Washrrom dirty.Threshold reached";
                t = 3;
                if (washroomId.equals(" 704326")) {
                    washroomName = "Washroom 1";
                    buildAlert("Washroom 1", "1", floorId);

                } else if(washroomId.equals("714434")) {
                    washroomName = "Washroom 2";
                    buildAlert("Washroom 2","2",floorId);

                }}

                FirebaseDatabase.getInstance().getReference(("Washrooms/"+ washroomName)).child("triggerSms").setValue("");


                //String timeStamp = new SimpleDateFormat("kk").format(Calendar.getInstance().getTime());





                /*NotificationManager notif=(NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notify=new Notification.Builder(getActivity().getApplicationContext()).setContentTitle("mayank").setContentText("Please clean washroom").
                        setContentTitle("Alert").setSmallIcon(R.drawable.meters).build();

                notify.flags |= Notification.FLAG_AUTO_CANCEL;
                notif.notify(0, notify);*/

            }


            //Second Washroom

            float sensor2 = (float) 0.0;


            if (!ThingSpeakFetch2.gsensor.equalsIgnoreCase("")) {
                sensor2 = Float.parseFloat(ThingSpeakFetch2.gsensor);
            }

            if(sensor2 < mindirtythresh) {
                t2 = 1;
            }
            if (sensor2 > mindirtythresh) {
                t2 = 3;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (washroomId.equals("704326")) {

            meterjList.add(new ThingFetch(ThingSpeakFetch.id, Integer.toString(t), ThingSpeakFetch.counter));

        } else {
            meterjList.add(new ThingFetch(ThingSpeakFetch2.id, Integer.toString(t2), ThingSpeakFetch2.counter));

        }
//        try
//        {
//            firebaseAuth = FirebaseAuth.getInstance();
//            final FirebaseUser user = firebaseAuth.getCurrentUser();
//
//            FirebaseDatabase.getInstance().getReference("Users").child(user.getEmail().replace(".", "").substring(0, user.getEmail().indexOf("@")))
//                  .child("triggerSms").setValue("");
//
//            FirebaseDatabase.getInstance().getReference("Users").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    for (DataSnapshot data : dataSnapshot.getChildren()) {
//                        Map<String, String> map2 = (Map) data.getValue();
//                        e = map2.get("Email");
//                        if (user.getEmail().equalsIgnoreCase(e)) {
//                            f = map2.get("Floor");
//                            FirebaseDatabase.getInstance().getReference("Floors").child(f).child("dummy").setValue("");
//                            System.out.println(meterjList);
//                        //    ThingFetch thingFetchData = getWashroomID(f);
//                          //  meterjList.add(thingFetchData);
//
//
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });


        //  meterjList.add(th)

    }
//        catch (Exception e)
//        {
//
//        }
//
//    }

    public void buildAlert(String washroomName, final String washroomId, final String floorId) {

        final String[] name = {""};
        final String[] time = {""};
        FirebaseDatabase.getInstance().getReference("Washrooms/"+ washroomName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    if(data.getKey().equals("Name")) {
                        name[0] = data.getValue().toString();

                    }
                    if(data.getKey().equals("Time")) {
                        time[0] = data.getValue().toString();
                    }
                }

                sendAlert(name[0], time[0], washroomId, floorId);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void sendAlert(String name, String time, String washroomId, String floorId) {
        SmsManager smsManager = SmsManager.getDefault();
        String smsMessage = "हाय " + name +", वॉशरूम आपके लिए टाइम स्लॉट " + time + "फ्लोर " + floorId + " में आवंटित किया गया, वॉशरूम " + washroomId + " गंदा है। आपका काम इसे साफ करना है। जाओ और जितनी जल्दी हो सके इसे साफ करें। साफ बटन पर क्लिक करना न भूलें।\n";


        ArrayList<String> parts = smsManager.divideMessage(smsMessage);
        smsManager.sendMultipartTextMessage(phone, null, parts, null, null);


    }


    @Override
    public void updateSensorReadings(String id, String gsensor, String counter) {
        meterjList.add(new ThingFetch(id, gsensor, counter));
        //  meterjList.add(th)
        madapter = new MeterAdapter(meterjList);
        recyclerView1.setAdapter(madapter);
    }}
/*
    public ThingFetch getWashroomID(String floorNum) {

        FirebaseDatabase.getInstance().getReference("Floors").child(floorNum).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Map<String, String> map3 = (Map) data.getValue();
                    id = map3.get("ID");
                    if (id.equalsIgnoreCase("704326")) {
                       thingFetch = new ThingFetch(ThingSpeakFetch.id, Integer.toString(t), ThingSpeakFetch.counter);
                    } else if (id.equalsIgnoreCase("714434")) {
                        thingFetch = new ThingFetch(ThingSpeakFetch2.id, Integer.toString(t2), ThingSpeakFetch2.counter);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        return thingFetch[0];
    }*/