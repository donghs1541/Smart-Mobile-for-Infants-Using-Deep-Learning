package com.example.capstoneproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ProfileName extends AppCompatActivity {
    private TextView CurrentNameLabel;
    private TextView ChangeNameLabel;
    private TextView CurrentName;
    private TextView ChangeName;
    private Button ChangeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_name);

        CurrentNameLabel = (TextView)findViewById(R.id.SettingsCurrentNameLabel);
        ChangeNameLabel = (TextView)findViewById(R.id.SettingsChangeNameLabel);
        CurrentName = (TextView)findViewById(R.id.SettingsCurrentName);
        ChangeName = (TextView)findViewById(R.id.SettingsChangeName);
        ChangeButton = (Button)findViewById(R.id.send);

        ChangeButton.setOnClickListener(new View.OnClickListener() {  //이름 변경하는 리스너(db)
            @Override
            public void onClick(View v) {
                ChangeName_db(v);
            }
        });

    }
    private void ChangeName_db(){


    }


}