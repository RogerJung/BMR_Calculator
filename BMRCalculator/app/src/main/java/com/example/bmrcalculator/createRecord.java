package com.example.bmrcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class createRecord extends AppCompatActivity {

    private Button cancel;
    private Button send;
    private RadioGroup radioGroup;
    String gender1;

    private void selectRadioButton() {
        RadioButton rb = (RadioButton)createRecord.this.findViewById(radioGroup.getCheckedRadioButtonId());
        gender1 = (String) rb.getText();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_record);

        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                selectRadioButton();
            }
        });

        cancel = (Button)findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(createRecord.this, MainActivity.class);
                startActivity(intent);
            }
        });

        send = (Button)findViewById(R.id.send);
        EditText createName = (EditText) findViewById((R.id.editName));
        EditText createAge = (EditText) findViewById(R.id.editAge);
        EditText createHeight = (EditText) findViewById(R.id.editHeight);
        EditText createWeight = (EditText) findViewById(R.id.editWeight);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = createName.getText().toString();
                String age = createAge.getText().toString();
                String height = createHeight.getText().toString();
                String weight = createWeight.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("age", age);
                bundle.putString("gender", gender1);
                bundle.putString("height", height);
                bundle.putString("weight", weight);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(createRecord.this, showRecord.class);
                startActivity(intent);
            }
        });
    }
}