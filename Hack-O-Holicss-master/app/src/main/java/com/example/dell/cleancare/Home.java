package com.example.dell.cleancare;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity {
    int count;
    Fragment selectedfragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomnav = findViewById(R.id.Bottom_nav);
        bottomnav.setOnNavigationItemSelectedListener(navListener);
        selectedfragment = new LeaderBoardfragment();
        initializeFragment();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:


                            selectedfragment = new Homefragmentsupervisor();


                            break;
                        case R.id.nav_meter:
                            selectedfragment = new MeterFragment();
                            break;
                        case R.id.nav_profile:
                            selectedfragment = new Profilefragment();
                            break;
                        case R.id.nav_leader:
                            selectedfragment = new LeaderBoardfragment();
                    }

                    initializeFragment();
                    return true;
                }

            };

    public void initializeFragment() {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, selectedfragment);
            fragmentTransaction.commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedfragment).commit();
        } catch (Exception e) {

        }
    }
}