package com.example.kanjeng.kangmas;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static org.apache.commons.codec.binary.Base64.encodeBase64;

public class payment extends AppCompatActivity {
    String produk;
    Firebase fbip,fbport;
    String valueip;
    int valueport;
    EditText tglpay,nmrpay,mUserPayment,mPasswordPayment;

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
    public static final String tag_de_063 = "de_063";
    public static final String tag_de_048 = "de_048";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_payment);

        Firebase.setAndroidContext(this);

        tglpay = (EditText) findViewById(R.id.edittanggalpay);
        nmrpay = (EditText) findViewById(R.id.editnomorpay);

        Spinner mySpinner = (Spinner)findViewById(R.id.spinnerProduk);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(payment.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.produk));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    produk="0";
                }
                if (i==1){
                    produk="IsatPre";
                }
                if (i==2){
                    produk="IsatPost";
                }
                if (i==3){
                    produk="XLPre";
                }
                if (i==4){
                    produk="XLPost";
                }
                if (i==5){
                    produk="SmartfrenPre";
                }
                if (i==6){
                    produk="SmartfrenPost";
                }
                if (i==7){
                    produk="AxisPre";
                }
                if (i==8){
                    produk="MNC";
                }
                if (i==9){
                    produk="TselPre";
                }
                if (i==10){
                    produk="TselPost";
                }
                if (i==11){
                    produk="EsiaPre";
                }
                if (i==12){
                    produk="EsiaPost";
                }
                if (i==13){
                    produk="PLN";
                }
                if (i==14){
                    produk="PGN";
                }
                if (i==15){
                    produk="PBB";
                }
                if (i==16){
                    produk="Citi";
                }
                if (i==17){
                    produk="Adira";
                }
                if (i==18){
                    produk="FIF";
                }
                if (i==19){
                    produk="summit";
                }
                if (i==20){
                    produk="baf";
                }
                if (i==21){
                    produk="ui";
                }
                if (i==22){
                    produk="takaful";
                }
                if (i==23){
                    produk="pru";
                }
                if (i==24){
                    produk="garuda";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

    public void loginpayment(View view) {
        if(!tglpay.getText().toString().isEmpty() && !nmrpay.getText().toString().isEmpty()){
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(payment.this);
            View mView = getLayoutInflater().inflate(R.layout.dialog_login_payment, null);
            mUserPayment = (EditText)mView.findViewById(R.id.etUserPayment);
            mPasswordPayment = (EditText)mView.findViewById(R.id.etPasswordPayment);
            Button mLoginPayment = (Button)mView.findViewById(R.id.btnLoginPayment);

            mBuilder.setView(mView);
            AlertDialog dialog = mBuilder.create();
            dialog.show();
        } else
        {
            Toast.makeText(payment.this,"Lengkapi data yang kosong",Toast.LENGTH_SHORT).show();
        }
    }
    public void cekdansendpayment(View view){
        if(!mUserPayment.getText().toString().isEmpty() && !mPasswordPayment.getText().toString().isEmpty()){
            kirimPayment();
        } else
        {
            Toast.makeText(payment.this,"Lengkapi data yang kosong",Toast.LENGTH_SHORT).show();
        }
    }

    public void kirimPayment(){
        Socket sok = null;
        try {
            sok = new Socket(valueip,valueport);
            Log.d("teskoneksi","setelah konek");
            DataInputStream dis = new DataInputStream(sok.getInputStream());
            DataOutputStream dos = new DataOutputStream(sok.getOutputStream());

            String msg = stringToJson("produk",produk,"tanggal",tglpay.getText().toString(),"nomor",nmrpay.getText().toString(),"user",mUserPayment.getText().toString(),"password",mPasswordPayment.getText().toString());
            String req = encrypt("Bar12345Bar12345","RandomInitVector",msg);
            dos.writeUTF(req);

            String respon = dis.readUTF();
            msg = decrypt("Bar12345Bar12345","RandomInitVector",respon);
            if (msg.equals("error_1")) {Toast.makeText(getApplicationContext(), "User atau Password Anda salah", Toast.LENGTH_LONG).show();}
            else {
                ArrayList<HashMap<String, String>> resultList = ParseJSON(msg);

                Intent hasil = new Intent(this, payment_hasil.class);
                hasil.putExtra("arraylist", resultList);
                startActivity(hasil);
            }
            sok.close();
        }
        catch (IOException e) {
            Log.d("teskoneksi","Gagal Connected to server");
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
                    String de_063 = c.getString(tag_de_063);
                    String de_048 = c.getString(tag_de_048);

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
                    student.put(tag_de_063, de_063);
                    student.put(tag_de_048, de_048);

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
