package com.example.bmrcalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.AlphabeticIndex;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class MainActivity extends AppCompatActivity{

    private Button IntentCreate;


    public static TextView tv;
    public static ListView lv;

    String[] values;
    String[] names;
    String[] ages;
    String[] genders;
    String[] heights;
    String[] weights;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView4);
        lv = findViewById(R.id.list);

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                String url2 = "http://10.0.2.2/BMRCalculator/showList.php";
                try {
                    URL url = new URL(url2); //初始化
                    HttpURLConnection httpURLConnection =
                            (HttpURLConnection) url.openConnection(); //取得連線之物件
                    InputStream inputStream = httpURLConnection.getInputStream();
                    //輸入串流的代表物件InputStream//對取得的資料進行讀取
                    BufferedReader bufferedReader =
                            new BufferedReader(new InputStreamReader(inputStream));
                    //宣告一個型態為BufferedReader的物件變數
                    //new BufferedReader表示以BufferedReader類別建構一個物件
                    // new InputStreamReader(inputStream)
                    //表示接受一個inputStream物件來建構一個InputStreamReader物件
                    String line = bufferedReader.readLine();
                    System.out.println(line);
                    String[] str = line.split("\"");
                    String[] result = new String[str.length/2];
                    int j = 0;
                    for (int i=0; i<str.length - 1; i++){
                        if (i % 2 != 0){
                            result[j] = str[i];
                            j++;
                        }
                    }
                    j = 0;
                    System.out.println(result.length);
                    String[] name = new String[result.length/6];
                    String[] age = new String[result.length/6];
                    String[] gender = new String[result.length/6];
                    String[] height = new String[result.length/6];
                    String[] weight = new String[result.length/6];
                    String[] temp = new String[result.length/6];
                    for (int i = 0; i<result.length - 4; i++){
                        temp[j] = " ";
                        if (i % 6 == 0){
                            name[j] = result[i];
                            age[j] = result[i+2];
                            gender[j] = result[i+3];
                            height[j] = result[i+4];
                            weight[j] = result[i+5];
                        }
                        if (i % 6 == 1){
                            for (int k = 20; k>result[i-1].length();k--){
                                temp[j] = temp[j] + " ";
                            }
                            temp[j] = temp[j] + result[i-1] + "                    |                       " + result[i];
                            j++;
                        }
                    }
                    values = temp;
                    names = name;
                    ages = age;
                    genders = gender;
                    heights = height;
                    weights = weight;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        while (thread.isAlive()) {

        }
        Log.d("queryRecord()", "End");
        ListView listView = (ListView) findViewById(R.id.list);
        ListAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, values);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putString("name", names[i]);
                bundle.putString("age", ages[i]);
                bundle.putString("gender", genders[i]);
                bundle.putString("height", heights[i]);
                bundle.putString("weight", weights[i]);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(MainActivity.this, editProfile.class);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Delete  \"" + names[i] + "\" ?");

                //新增AlertDialog.Builder物件的setPositiveButton()方法
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HashMap<String, String> Sites = new HashMap<String, String>();
                        Sites.put("name", names[i]);
                        deleteRecord(Sites);
                        Toast.makeText(getBaseContext(), names[i] + " is deleted.", Toast.LENGTH_SHORT).show();
                        refresh();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }

                });

                builder.create().show();
                return true;
            }
        });

        IntentCreate = (Button)findViewById(R.id.create);
        IntentCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, createRecord.class);
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

    private synchronized  void  deleteRecord(HashMap<String, String> map) {
        Thread thread = new Thread((new Runnable() {

            HashMap<String, String> _map;

            @Override
            public void run() {
                String path = "http://10.0.2.2/BMRCalculator/deleteData.php";
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
        Log.d("Update()", "End");
    }

    private void refresh() {
        finish();
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
    }
}