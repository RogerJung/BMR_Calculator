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
import android.widget.Toast;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class editProfile extends AppCompatActivity {

    EditText alterName;
    EditText alterAge;
    EditText alterHeight;
    EditText alterWeight;
    RadioButton alterMale;
    RadioButton alterFemale;

    String gender1;
    String oldname;

    private Button cancel;
    private Button send;
    private RadioGroup radioGroup;

    private void selectRadioButton() {
        RadioButton rb = (RadioButton)editProfile.this.findViewById(radioGroup.getCheckedRadioButtonId());
        gender1 = (String) rb.getText();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initViewElement();

        //get data from db
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
                    oldname = inputStr;
                    alterName.setText(inputStr);
                    alterAge.setText(inputAge);
                    alterHeight.setText(inputHeight);
                    alterWeight.setText(inputWeight);
                    if (inputGender.equals("Male")){
                        alterMale.setChecked(true);
                    } else {
                        alterFemale.setChecked(true);
                    }
                }
            }
        }

        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                selectRadioButton();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(editProfile.this, MainActivity.class);
                startActivity(intent);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = alterName.getText().toString();
                String age = alterAge.getText().toString();
                String height = alterHeight.getText().toString();
                String weight = alterWeight.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("oldname", oldname);
                bundle.putString("name", name);
                bundle.putString("age", age);
                bundle.putString("gender", gender1);
                bundle.putString("height", height);
                bundle.putString("weight", weight);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(editProfile.this, updateRecord.class);
                startActivity(intent);
            }
        });
    }

    private void initViewElement() {
        cancel = (Button)findViewById(R.id.cancel);
        send = (Button)findViewById(R.id.send);
        alterName = (EditText) findViewById(R.id.altername);
        alterAge = (EditText) findViewById(R.id.alterAge);
        alterHeight = (EditText) findViewById(R.id.alterHeight);
        alterWeight = (EditText) findViewById(R.id.alterWeight);
        alterMale = (RadioButton) findViewById(R.id.alterMale);
        alterFemale = (RadioButton) findViewById(R.id.alterFemale);
    }
}