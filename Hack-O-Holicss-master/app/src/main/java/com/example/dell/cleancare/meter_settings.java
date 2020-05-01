package com.example.dell.cleancare;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class meter_settings extends AppCompatActivity {
    Spinner wash2;
    String check;
    EditText thres_ppl,thres_gas;
    Button submit;
    String floorId;
    String washroomId;
    String gas, ppl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_settings);
        wash2 = (Spinner) findViewById(R.id.washroom_id2);
        thres_gas = (EditText) findViewById(R.id.thres_gas);
        thres_ppl = (EditText) findViewById(R.id.thres_ppl);
        submit = (Button) findViewById(R.id.setthres);
        FirebaseDatabase.getInstance().getReference("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    if (data.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "").substring(0, FirebaseAuth.getInstance().getCurrentUser().getEmail().indexOf("@")))) {
                        floorId = data.child("Floor").getValue().toString();
                        try{
                            ArrayAdapter<CharSequence> adap;
                            if(floorId.equalsIgnoreCase("1"))
                            {

                                adap = ArrayAdapter.createFromResource(meter_settings.this,
                                        R.array.Washroom1, android.R.layout.simple_spinner_item);
                                adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                wash2.setAdapter(adap);
                            }
                            else if(floorId.equalsIgnoreCase("2"))

                            {

                                adap = ArrayAdapter.createFromResource(meter_settings.this,
                                        R.array.Washroom2, android.R.layout.simple_spinner_item);
                                adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                wash2.setAdapter(adap);

                            }


                        }
                        catch (Exception e)
                        {

                        }

                        getWashroomId(floorId);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "").substring(0, FirebaseAuth.getInstance().getCurrentUser().getEmail().indexOf("@")))
                .child("triggerSms").setValue("");
        FirebaseDatabase.getInstance().getReference("Floors")
                .child("triggerSms").setValue("");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> map3 = new HashMap<>();
                ppl = thres_ppl.getText().toString();
                gas = thres_gas.getText().toString();
                map3.put("pplthresh", ppl);
                map3.put("gasthresh", gas);
                FirebaseDatabase.getInstance().getReference("threshold").child(washroomId).setValue(map3);
                Toast.makeText(meter_settings.this, "Success", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getWashroomId(final String floorId) {
        FirebaseDatabase.getInstance().getReference("Floors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    if(data.getKey().equals(floorId)) {
                        washroomId = data.getValue().toString().replace("{","").replace("}", "").replace("ID=", "").substring(17);;


                    }}}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
