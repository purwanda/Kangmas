package com.example.kanjeng.kangmas;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    public void clickAtmb (View view){
        Intent atmb = new Intent(this, atmb.class);
        startActivity(atmb);
    }

    public void clickPayment (View view){
        Intent payment = new Intent(this, payment.class);
        startActivity(payment);
    }

    public void clickSetting (View view){
        Intent setting = new Intent(this, setting.class);
        startActivity(setting);
    }
}
