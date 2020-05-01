package com.example.dell.cleancare;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Homefragmentsupervisor extends Fragment {

    EditText p;
    Spinner name;
    Button update;
    Spinner assign_time;
    Spinner washroom0;
    String id;
    String check;
    String floorNum;





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_home_supervisor, container, false);



        Spinner spinner = (Spinner) v.findViewById(R.id.time1);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.Time, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        final Spinner spin1 = (Spinner) v.findViewById(R.id.washroom_id);
        final Spinner spin2 = (Spinner) v.findViewById(R.id.worker_name);

// Create an ArrayAdapter using the string array and a default spinner layout




// Specify the layout to use when the list of choices appears


// Apply the adapter to the spinner

        //p = (EditText) v.findViewById(R.id.phone_id);
        //name = (EditText) v.findViewById(R.id.worker_name);
        name = (Spinner) v.findViewById(R.id.worker_name);
        washroom0 = (Spinner) v.findViewById(R.id.washroom_id);
        update = (Button) v.findViewById(R.id.assign_id);
        assign_time = (Spinner) v.findViewById(R.id.time1);

        String userid = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "").substring(0, FirebaseAuth.getInstance().getCurrentUser().getEmail().indexOf("@"));
//        FirebaseDatabase.getInstance().getReference("7299930321").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                final List<String> worker = new ArrayList<String>();
//
//                for (DataSnapshot workerSnapshot: dataSnapshot.getChildren()) {
//                    String w = workerSnapshot.child("Name").getValue(String.class);
//                    worker.add(w);
//                }
//
//                Spinner workSpinner = (Spinner) v.findViewById(R.id.worker_name);
//                ArrayAdapter<String> workAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, worker);
//                workAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                workSpinner.setAdapter(workAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        FirebaseDatabase.getInstance().getReference("Users/" + userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if(data.getKey().equals("Floor")) {
                        floorNum = data.getValue().toString();
                        setSpinner(floorNum,spin1,spin2);

                    }}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //String phone = null;

                String workerName = null;


                //phone = p.getText().toString().trim();


                String time = (String) assign_time.getSelectedItem();
                String washroom = (String) washroom0.getSelectedItem();
                workerName = (String) name.getSelectedItem() ;
                Map<String,String> map1 = new HashMap<>();
                map1.put("Name",workerName);
                map1.put("Time",time);
                FirebaseDatabase.getInstance().getReference("Washrooms").child(washroom).setValue(map1);
                String washroomName = null;
                String phone1 = null;
//                String newemail = email.replaceAll("@", "");
//                String nemail = newemail.replaceAll("\\.", "");
//                String mail = nemail.replaceAll("gmailcom", "");
                if (washroom.equalsIgnoreCase("Washroom 1")) {
                    id = "704326";
                    washroomName = "1";
                    phone1 = "7299930321";


                } else if (washroom.equalsIgnoreCase("Washroom 2")) {
                    id = "714434";
                    washroomName = "2";
                    phone1 = "9384826732";
                }



                //assign Assign = new assign(block,floor,washroom);
                Map<String, String> map = new HashMap<>();

                map.put("Washroom", washroom);
                map.put("Time", time);
                map.put("WashroomID", id);
                //FirebaseDatabase.getInstance().getReference("Phone").child(phone).child(UUID.randomUUID().toString()).setValue(map);
                Toast.makeText(getContext(), "Task is Assigned", Toast.LENGTH_LONG).show();
                //p.setText(null);
                String smsMessage = workerName + " :आपको सुपरवाइजर द्वारा वॉशरूम " + washroomName + " को टाइम स्लॉट " + time + "फ्लोर " + floorNum + "में आवंटित किया जाता है।";
//                FirebaseDatabase.getInstance().getReference("Washrooms").child(id).child(time).child("name").setValue(workerName);
//                FirebaseDatabase.getInstance().getReference("Washrooms").child(id).child(time).child("phone").setValue(phone1);

                    SmsManager smsManager = SmsManager.getDefault();
                    ArrayList<String> parts = smsManager.divideMessage(smsMessage);
                    smsManager.sendMultipartTextMessage(phone1, null, parts, null, null);



            }


        });


        return v;

    }
    public void setSpinner(final String floorNum, Spinner spin1, Spinner spin2)
    {
        ArrayAdapter<CharSequence> adap1;
        ArrayAdapter<CharSequence> adap2;
        if(floorNum.equalsIgnoreCase("1"))
        {
            adap1 = ArrayAdapter.createFromResource(getContext(),
                    R.array.Washroom1, android.R.layout.simple_spinner_item);
            adap1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin1.setAdapter(adap1);
            adap2 = ArrayAdapter.createFromResource(getContext(),
                    R.array.Worker_Name1, android.R.layout.simple_spinner_item);
            adap2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin2.setAdapter(adap2);
        }
        else if(floorNum.equalsIgnoreCase("2"))
        {
            adap1 = ArrayAdapter.createFromResource(getContext(),
                    R.array.Washroom2, android.R.layout.simple_spinner_item);
            adap1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin1.setAdapter(adap1);
            adap2 = ArrayAdapter.createFromResource(getContext(),
                    R.array.Worker_Name2, android.R.layout.simple_spinner_item);
            adap2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin2.setAdapter(adap2);
        }

    }


}