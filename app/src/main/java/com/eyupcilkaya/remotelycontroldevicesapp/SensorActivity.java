package com.eyupcilkaya.remotelycontroldevicesapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SensorActivity extends AppCompatActivity {

    TextView nozzleTemp;
    TextView baseTemp;
    TextView progressText;
    ProgressBar progressBar;
    DatabaseReference myRef;
    FirebaseDatabase database;
    Runnable runnable;
    Handler handler;
    String nozzlevalue;
    String basevalue;
    String processvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        nozzleTemp = findViewById(R.id.nozzleTempText);
        baseTemp = findViewById(R.id.baseTempText);
        progressText = findViewById(R.id.percentText);
        progressBar = findViewById(R.id.progress_bar);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("");
        nozzlevalue = "";
        basevalue = "";
        processvalue = "";

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        nozzlevalue = snapshot.child("Base Temp").getValue().toString();
                        basevalue = snapshot.child("Nozzle Temp").getValue().toString();
                        processvalue = snapshot.child("Process").getValue().toString();
                        progressText.setText("%" + processvalue);
                        progressBar.setProgress(Integer.parseInt(processvalue));
                        nozzleTemp.setText(nozzlevalue);
                        baseTemp.setText(basevalue);
                        handler.postDelayed(runnable, 1000);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        };
        handler.post(runnable);

    }
}