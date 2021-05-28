package com.example.capstoneproject;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButtonToggleGroup;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Frag3 extends Fragment {

    private View view;
    private ArrayList<MainData> arrayList;
    private MainAdapter mainAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    view = inflater.inflate(R.layout.frag3,container,false);

    recyclerView = (RecyclerView)view.findViewById(R.id.rv);
    linearLayoutManager = new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(linearLayoutManager);

    arrayList = new ArrayList<>();

    mainAdapter = new MainAdapter(arrayList);
    recyclerView.setAdapter(mainAdapter);



    final Frag3.JsonParse jsonParse = new Frag3.JsonParse();      // AsyncTask 생성
    jsonParse.execute("http://113.198.234.39:7776/info_load_noti.php");     // 이름과 생일을 db에서 불러오는 부분


    return view;

    }



    public class JsonParse extends AsyncTask<String, Void, String> {   // DB날짜 불러오는  클래스
        String TAG = "JsonParseTest";
        String url;
        URL serverURL;
        HttpURLConnection httpURLConnection;
        @Override
        protected String doInBackground(String... strings) {
            // execute의 매개변수를 받아와서 사용
            url = strings[0];
            try {

                serverURL = new URL(url);
                httpURLConnection = (HttpURLConnection) serverURL.openConnection();

                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

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
                Log.d(TAG, sb.toString().trim());

                return sb.toString().trim();        // 받아온 JSON 의 공백을 제거
            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                return null;
            }
        }


        protected void onPostExecute(String value){
            super.onPostExecute(value);
            String[] temp = value.split("&");
            System.out.println("value:"+value);
            System.out.println("temp:"+temp[0]);
            System.out.println("temp1:"+temp[3]);

            for(int i = 0; i < temp.length; i++) {
                MainData mainData = new MainData(R.drawable.alram_icon,"Emergency!",temp[i]);
                arrayList.add(mainData);
                mainAdapter.notifyDataSetChanged();
            }




            }




            }


        }




