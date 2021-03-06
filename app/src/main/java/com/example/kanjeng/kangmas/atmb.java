package com.example.kanjeng.kangmas;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
//import org.apache.commons.codec.binary.Base64;
import android.support.v7.app.AlertDialog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import static org.apache.commons.codec.binary.Base64.encodeBase64;

public class atmb extends AppCompatActivity {

    TextView ip,port;
    EditText txttanggal,txtpan,mUser,mPassword;
    Firebase fbip,fbport;
    String valueip;
    int valueport;
    ProgressBar mprogressbar;

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
        mprogressbar=(ProgressBar) findViewById(R.id.progressBar);
        mprogressbar.setVisibility(View.GONE);

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

    public void loginatmb(View view) {
        if(!txttanggal.getText().toString().isEmpty() && !txtpan.getText().toString().isEmpty()){
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(atmb.this);
            View mView = getLayoutInflater().inflate(R.layout.dialog_login_atmb, null);
            mUser = (EditText)mView.findViewById(R.id.etUser);
            mPassword = (EditText)mView.findViewById(R.id.etPassword);
            Button mLogin = (Button)mView.findViewById(R.id.btnLogin);

            mBuilder.setView(mView);
            AlertDialog dialog = mBuilder.create();
            dialog.show();
        } else
        {
            Toast.makeText(atmb.this,"Lengkapi data yang kosong",Toast.LENGTH_SHORT).show();
        }
    }

    public void cekdansendatmb(View view){
        if(!mUser.getText().toString().isEmpty() && !mPassword.getText().toString().isEmpty()){
//            mprogressbar.setVisibility(View.VISIBLE);
            kirim();
        } else
        {
            Toast.makeText(atmb.this,"Lengkapi data yang kosong",Toast.LENGTH_SHORT).show();
        }
    }

    public void kirim(){
        Socket sok = null;
        try {
            sok = new Socket(valueip,valueport);
            DataInputStream dis = new DataInputStream(sok.getInputStream());
            DataOutputStream dos = new DataOutputStream(sok.getOutputStream());

            String msg = stringToJson("produk","atmb","tanggal",txttanggal.getText().toString(),"pan",txtpan.getText().toString(),"user",mUser.getText().toString(),"password",mPassword.getText().toString());
            String req = encrypt("Bar12345Bar12345","RandomInitVector",msg);
            dos.writeUTF(req);

            String respon = dis.readUTF();
            msg = decrypt("Bar12345Bar12345","RandomInitVector",respon);
            Log.d("respon",msg);
            if (msg.equals("error_1")) {Toast.makeText(getApplicationContext(), "User atau Password Anda salah", Toast.LENGTH_LONG).show();}
            else {
                ArrayList<HashMap<String, String>> resultList = ParseJSON(msg);

                Intent hasil = new Intent(this, atmb_hasil.class);
                hasil.putExtra("arraylist", resultList);
                startActivity(hasil);
            }
            sok.close();
        }
        catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Gagal terhubung ke server",
                    Toast.LENGTH_LONG).show();
        }
    }

    public static String encrypt(String key, String initVector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            System.out.println("encrypted string: "+ new String(encodeBase64(encrypted)));

            return new String(encodeBase64(encrypted));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String decrypt(String key, String initVector, String encrypted)
    {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(android.util.Base64.decode(encrypted, android.util.Base64.DEFAULT));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private String stringToJson (String key0,String value0,String key1,String value1,String key2,String value2, String key3,String value3,String key4,String value4){
        String message;
        JSONObject json = new JSONObject();
        try {
            json.put(key0, value0);
            json.put(key1, value1);
            json.put(key2, value2);
            json.put(key3, value3);
            json.put(key4, value4);
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
