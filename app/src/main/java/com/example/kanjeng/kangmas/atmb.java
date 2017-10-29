package com.example.kanjeng.kangmas;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class atmb extends AppCompatActivity {

    TextView ip,port;
    EditText txttanggal,txtpan;
    Firebase fbip,fbport;
    String valueip;
    int valueport;

    public static final String tag_judul = "judul";
    public static final String tag_mmdd = "mmdd";
    public static final String tag_timestamp = "timestamp";
    public static final String tag_mti = "mti";
    public static final String tag_msg_dir = "msg_dir";
    public static final String tag_proc = "proc";
    public static final String tag_de_002 = "de_002";
    public static final String tag_de_003 = "de_003";
    public static final String tag_de_004 = "de_004";
    public static final String tag_de_011 = "de_011";
    public static final String tag_de_032 = "de_032";
    public static final String tag_de_037 = "de_037";
    public static final String tag_de_039 = "de_039";
    public static final String tag_de_041 = "de_041";
    public static final String tag_de_100 = "de_100";
    public static final String tag_de_125 = "de_125";
    public static final String tag_de_127 = "de_127";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atmb);

        Firebase.setAndroidContext(this);

        txttanggal = (EditText) findViewById(R.id.editTgl);
        txtpan = (EditText) findViewById(R.id.editPan);

        fbip = new Firebase("https://fir-a6630.firebaseio.com/IP");
        fbport = new Firebase("https://fir-a6630.firebaseio.com/PORT");

        fbip.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                valueip = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        fbport.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                valueport = dataSnapshot.getValue(int.class);
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

    public void kirim(View view){
        Socket sok = null;
        try {
//            sok = new Socket("192.168.1.101",2000);
            sok = new Socket(valueip,valueport);
            Log.d("teskoneksi","setelah konek");
            DataInputStream dis = new DataInputStream(sok.getInputStream());
            DataOutputStream dos = new DataOutputStream(sok.getOutputStream());

            String msg = stringToJson("produk","atmb","tanggal",txttanggal.getText().toString(),"pan",txtpan.getText().toString());
            dos.writeUTF(msg);

            msg = dis.readUTF();
            ArrayList<HashMap<String, String>> resultList = ParseJSON(msg);

            Intent hasil = new Intent(this, atmb_hasil.class);
            hasil.putExtra("arraylist",resultList);
            Log.d("teskoneksi","setelah dapat hasil");
            startActivity(hasil);
            Log.d("teskoneksi","mau close");
            sok.close();
        }
        catch (IOException e) {
            Log.d("teskoneksi","Gagal Connected to server");
            Toast.makeText(getApplicationContext(), "Gagal terhubung ke server",
                    Toast.LENGTH_LONG).show();
        }
    }

    private String stringToJson (String key0,String value0,String key1,String value1,String key2,String value2){
        String message;
        JSONObject json = new JSONObject();
        try {
            json.put(key0, value0);
            json.put(key1, value1);
            json.put(key2, value2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        message = json.toString();
        return message;
    }

    private ArrayList<HashMap<String, String>> ParseJSON(String json) {
        if (json != null) {
            try {
                ArrayList<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();
                JSONObject jsonObj = new JSONObject(json);

                JSONArray students = jsonObj.getJSONArray(tag_judul);

                for (int i = 0; i < students.length(); i++) {
                    JSONObject c = students.getJSONObject(i);

                    String mmdd = c.getString(tag_mmdd);
                    String timestamp = c.getString(tag_timestamp);
                    String mti = c.getString(tag_mti);
                    String msg_dir = c.getString(tag_msg_dir);
                    String proc = c.getString(tag_proc);
                    String de_002 = c.getString(tag_de_002);
                    String de_003 = c.getString(tag_de_003);
                    String de_004 = c.getString(tag_de_004);
                    String de_011 = c.getString(tag_de_011);
                    String de_032 = c.getString(tag_de_032);
                    String de_037 = c.getString(tag_de_037);
                    String de_039 = c.getString(tag_de_039);
                    String de_041 = c.getString(tag_de_041);
                    String de_100 = c.getString(tag_de_100);
                    String de_125 = c.getString(tag_de_125);
                    String de_127 = c.getString(tag_de_127);

                    HashMap<String, String> student = new HashMap<String, String>();

                    student.put(tag_mmdd, mmdd);
                    student.put(tag_timestamp, timestamp);
                    student.put(tag_mti, mti);
                    student.put(tag_msg_dir, msg_dir);
                    student.put(tag_proc, proc);
                    student.put(tag_de_002, de_002);
                    student.put(tag_de_003, de_003);
                    student.put(tag_de_004, de_004);
                    student.put(tag_de_011, de_011);
                    student.put(tag_de_032, de_032);
                    student.put(tag_de_037, de_037);
                    student.put(tag_de_039, de_039);
                    student.put(tag_de_041, de_041);
                    student.put(tag_de_100, de_100);
                    student.put(tag_de_125, de_125);
                    student.put(tag_de_127, de_127);

                    resultList.add(student);
                }
                return resultList;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Log.e("ServiceHandler", "No data received from HTTP request");
            return null;
        }
    }

}