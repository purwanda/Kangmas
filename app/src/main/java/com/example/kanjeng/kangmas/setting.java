package com.example.kanjeng.kangmas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class setting extends AppCompatActivity {
    TextView ip,port,keterangan;
    Firebase fbip,fbport,fbket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Firebase.setAndroidContext(this);

        ip = (TextView)findViewById(R.id.ip_setting);
        port = (TextView)findViewById(R.id.port_setting);
        keterangan = (TextView)findViewById(R.id.txtket);

        fbip = new Firebase("https://fir-a6630.firebaseio.com/IP");
        fbport = new Firebase("https://fir-a6630.firebaseio.com/PORT");
        fbket = new Firebase("https://fir-a6630.firebaseio.com/keterangan");

        fbip.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valueip = dataSnapshot.getValue(String.class);
                ip.setText(valueip);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        fbport.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valueport = dataSnapshot.getValue(String.class);
                port.setText(valueport);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        fbket.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valueket = dataSnapshot.getValue(String.class);
                keterangan.setText(valueket);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public void clickBack (View view) {
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
    }
}
