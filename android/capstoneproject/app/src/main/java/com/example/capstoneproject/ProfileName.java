package com.example.capstoneproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ProfileName extends AppCompatActivity {
    private TextView CurrentNameLabel;
    private TextView ChangeNameLabel;
    private TextView CurrentName;
    private TextView ChangeName;
    private Button ChangeButton;
    private String jsonString;
    private Frag1 frag1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_name);
        frag1 = new Frag1();
        CurrentNameLabel = (TextView)findViewById(R.id.SettingsCurrentNameLabel);
        ChangeNameLabel = (TextView)findViewById(R.id.SettingsChangeNameLabel);
        CurrentName = (TextView)findViewById(R.id.SettingsCurrentName);
        ChangeName = (TextView)findViewById(R.id.SettingsChangeName);
        ChangeButton=(Button) findViewById(R.id.ChangeNameButton);


        final JsonParse jsonParse = new JsonParse();      // AsyncTask 생성
        jsonParse.execute("http://113.198.234.49:7776/info_load_name.php");     // AsyncTask 실행

        ChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final JsonParse jsonParse = new JsonParse();      // AsyncTask 생성
                jsonParse.execute("http://113.198.234.49:7776/info_insert.php");     // AsyncTask 실행




            }
        });
    }

    public class JsonParse extends AsyncTask<String, Void, String> {
        String TAG = "JsonParseTest";
        @Override
        protected String doInBackground(String... strings) {
            // execute의 매개변수를 받아와서 사용
            String url = strings[0];
            try {
                String selectData = "Data=" + ChangeName.getText().toString();
                // 따옴표 안과 php의 post [ ] 안이 이름이 같아야 함
                URL serverURL = new URL(url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) serverURL.openConnection();

                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(selectData.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
                // 어플에서 데이터 전송

                int responseStatusCode = httpURLConnection.getResponseCode();

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                } // 연결 상태 확인

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();
                CurrentName.setText(sb.toString().trim());
                Log.d(TAG, sb.toString().trim());

                return sb.toString().trim();        // 받아온 JSON 의 공백을 제거
            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                return null;
            }
        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

    }
}