package com.example.kanjeng.kangmas;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class atmb_hasil extends AppCompatActivity {

    private ListView listview;
    private SimpleAdapter adapter;
    private ArrayList<HashMap<String,String>> mylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atmb_hasil);

        mylist = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("arraylist");
        System.out.println("...serialized data.."+mylist);

        listview = (ListView)findViewById(R.id.listview);

        adapter = new SimpleAdapter(this,mylist,R.layout.template_layout_atmb,new String[]{"mmdd","timestamp","mti","msg_dir","proc","de_002","de_003","de_004","de_011","de_032","de_037","de_039","de_041","de_100","de_125","de_127"},new int[]{R.id.mmdd,R.id.timestamp,R.id.mti,R.id.msg_dir,R.id.proc,R.id.de_002,R.id.de_003,R.id.de_004,R.id.de_011,R.id.de_032,R.id.de_037,R.id.de_039,R.id.de_041,R.id.de_100,R.id.de_125,R.id.de_127})
        {
            @Override
            public View getView (int position, View convertView, ViewGroup parent)
            {
                View v = super.getView(position, convertView, parent);
                if(position %2==0)
                {
                    v.setBackgroundColor(Color.parseColor("#FFDDAA"));
                }
                else
                {
                    v.setBackgroundColor(Color.WHITE);
                }

                return v;
            }
        };
        listview.setAdapter(adapter);
    }

    public void clickAtmb (View view){
        Intent atmb = new Intent(this, atmb.class);
        startActivity(atmb);
    }

}
