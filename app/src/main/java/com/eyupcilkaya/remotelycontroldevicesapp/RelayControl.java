package com.eyupcilkaya.remotelycontroldevicesapp;


import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RelayControl {

    DatabaseReference myRef;
    FirebaseDatabase database;
    String relay = "";

    public RelayControl() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                relay = snapshot.child("Relay").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public String getRelay() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                relay = snapshot.child("Relay").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return relay;
    }

    public void setRelay(String relay) {
        myRef.child("Relay").setValue(relay);
    }
}
