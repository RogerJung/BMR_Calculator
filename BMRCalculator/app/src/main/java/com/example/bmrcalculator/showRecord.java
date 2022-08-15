package com.example.bmrcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class showRecord extends AppCompatActivity {

    private Button cancel;
    private Button save;
    TextView tname;
    TextView tbmi;
    TextView tbmr;

    String name;
    String gender;
    String age;
    String height;
    String weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_record);


        Intent it = this.getIntent();
        if (it != null) {
            Bundle bundle = it.getExtras();
            if (bundle != null) {
                String inputStr = bundle.getString("name");
                String inputAge = bundle.getString("age");
                String inputGender = bundle.getString("gender");
                String inputHeight = bundle.getString("height");
                String inputWeight = bundle.getString("weight");
                if (inputStr != null && !inputStr.equals("")) {
                    name = inputStr;
                    age = inputAge;
                    height = inputHeight;
                    weight = inputWeight;
                    gender = inputGender;
                }
            }
        }

        float fbmi= 0.0F;
        fbmi = Float.parseFloat(weight) / ((Float.parseFloat(height) / 100) * (Float.parseFloat(height) / 100));
        double dbmr=0.0;
        if (gender.equals("Male")) {
            dbmr = 66 + (13.7 * Float.parseFloat(weight) + 5 * Float.parseFloat(height) - 6.8 * Float.parseFloat(age));
        }else {
            dbmr = 655 + (9.6 * Float.parseFloat(weight) + 1.8 * Float.parseFloat(height) - 4.7 * Float.parseFloat(age));
        }
        DecimalFormat fnum = new DecimalFormat("##0.00");
        String bmr = fnum.format(dbmr);
        String bmi = fnum.format(fbmi);
        tname = (TextView) findViewById(R.id.nameindex);
        tbmi = (TextView) findViewById(R.id.bmiindex);
        tbmr = (TextView) findViewById(R.id.bmrindex);
        tname.setText(name);
        tbmi.setText(bmi);
        tbmr.setText(bmr);

        WebView webView = (WebView) findViewById(R.id.webview);
        if (fbmi > 30 || fbmi < 18.5){
            String x = "<!DOCTYPE html><style>*,body,html,img{border:0;margin:0;padding:0;}</style><html><body><img src=\"https://media1.giphy.com/media/Ty9Sg8oHghPWg/200w.gif?cid=82a1493bfhz3j5gwsr3n7yjca0631yl2v3ydl54ljbvllhgn&rid=200w.gif&ct=g\" alt=\"sad\" style=\"height: 150px\"></body></html>";
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);//不加上白边
            webView.loadData(x, "text/html", "utf-8");
        } else {
            String x = "<!DOCTYPE html><style>*,body,html,img{border:0;margin:0;padding:0;}</style><html><body><img src=\"https://media3.giphy.com/media/nF0FFZLoES0YUurQVR/giphy.gif?cid=ecf05e4759t827c9ehmzdinw54evrd995x5dx3mcvm9epj7p&rid=giphy.gif&ct=g\" alt=\"sad\" style=\"height: 150px\"></body></html>";
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);//不加上白边
            webView.loadData(x, "text/html", "utf-8");
        }


        cancel = (Button)findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(showRecord.this, MainActivity.class);
                startActivity(intent);
            }
        });

        save = (Button)findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> Sites = new HashMap<String, String>();
                Sites.put("name", name);
                Sites.put("gender", gender);
                Sites.put("age", age);
                Sites.put("height", height);
                Sites.put("weight", weight);
                Sites.put("bmr", bmr);
                insertRecord(Sites);
                Intent intent = new Intent();
                intent.setClass(showRecord.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private  void executeHttpPost(String path, HashMap<String, String> map) {
        try {
            URL urlobj = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) urlobj.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.connect();

            Map<String, String> data = new HashMap<String, String>();
            data = map;
            JSONObject response = new JSONObject(data);
            Log.i("response: ",response.toString());
            OutputStream os = conn.getOutputStream();
            DataOutputStream objout = new DataOutputStream(os);
            objout.writeBytes(response.toString());
            objout.flush();
            os.close();
            objout.close();
            int code=conn.getResponseCode();
            if (code==200){
                System.out.println("success");
            }else{
                Log.i("error: ", String.valueOf(code));
            }
            conn.disconnect();
        }  catch (IOException e) {
            Log.d("Save Record", "Record saved failed");
            e.printStackTrace();
        }
    }

    private synchronized  void  insertRecord(HashMap<String, String> map) {
        Thread thread = new Thread((new Runnable() {

            HashMap<String, String> _map;

            @Override
            public void run() {
                String path = "http://10.0.2.2/BMRCalculator/insert.php";
                executeHttpPost(path,_map);
                Log.d("internet thread", "end");
            }

            public Runnable init(HashMap<String, String> map){
                _map = map;
                return this;
            }
        }.init(map)));
        thread.start();
        while (thread.isAlive()) {

        }
        Log.d("insertRecord()", "End");
    }
}