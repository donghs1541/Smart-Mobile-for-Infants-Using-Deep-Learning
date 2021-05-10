package com.example.capstoneproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.*;
import java.net.*;
import java.net.UnknownHostException;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity{

    public final String CHANNEL_ID = "my_notification_channel";
    public final int NOTIFICATION_ID = 101;

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Frag1 frag1;
    private Frag2 frag2;
    private Frag3 frag3;
    private Frag4 frag4;


    String Message = "android";
    String txtRecevie = "";
     byte[] buffer = new byte[1024]; //읽어오는 버퍼 크기
     Socket client;
     OutputStream  outputStream;
     ByteArrayOutputStream byteArrayOutputStream;
     PrintWriter out;
    InputStream inputStream;
    TextView textResponse;
    EditText editMsg;
    Button buttonSend;
    BufferedReader reader ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        editMsg = (EditText)findViewById(R.id.msg);
        buttonSend = (Button)findViewById(R.id.send);
        textResponse = (TextView)findViewById(R.id.response);

        buttonSend.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Thread sendworker = new Thread(){
                    public void run(){

                        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream)), true);  //out 객체 초기화

                        Message = editMsg.getText().toString();
                        out.write(Message);
                        out.flush();

                    }
                };sendworker.start();
            }
        });

        final Handler handler = new Handler()
        {
            public void handleMessage(android.os.Message msg)
            {
                textResponse.setText(txtRecevie);
            }
        };






        bottomNavigationView = findViewById(R.id.bottomNani);
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.action_airplane:
                        setFrag(0);
                        break;
                    case R.id.action_activity:
                        setFrag(1);
                        break;
                    case R.id.action_music:
                        setFrag(2);
                        break;
                    case R.id.action_call:
                        setFrag(3);
                        break;

                }

            }
        });

        frag1 = new Frag1();
        frag2 = new Frag2();
        frag3 = new Frag3();
        frag4 = new Frag4();

        setFrag(0); //첫 프래그먼트 화면 지정

        Thread worker = new Thread(){
            @Override
            public void run(){
                try {
                    Message ="android";
                    client = new Socket("113.198.234.39", 55000);
                    outputStream = client.getOutputStream();   // 소켓통신 outputstream선언
                    inputStream = client.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    byteArrayOutputStream = new ByteArrayOutputStream(1024); // 바이트 어레이 아웃스트림 객체 초기화
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream)), true);  //out 객체 초기화

                    out.write(Message);
                    out.flush();

                    while (true){

                        System.out.println("asdasdaasdasdasdsadasdasdsadsadasdsadsasdasd");
                        int size = inputStream.read(buffer);
                        txtRecevie = new String(buffer,0,size,"UTF-8");
                        System.out.println("asdasdasdasd"+txtRecevie);

                        Message message = handler.obtainMessage();
                        handler.sendMessage(message);


                    }
                }
                catch (IOException e){
                    System.out.print("IOException: " + e.toString());
                }

            }};worker.start();




        Button btn_noti = findViewById(R.id.noti);
        btn_noti.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                displayNotification(v);
            }
        });

    }
//알림설정
    public void displayNotification(View v){
        createNotificationChaanel();

        //알림설정
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID);
        builder.setSmallIcon((R.drawable.alram_icon));
        builder.setContentTitle("낳음당한년");
        builder.setPriority(NotificationManagerCompat.IMPORTANCE_DEFAULT);
        builder.setAutoCancel(true);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID,builder.build());

    }
    //채널설정
private void createNotificationChaanel(){
        //오레오부터 알림을 채널에 등록해야함
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "채널이름";
            String description = "채널설명";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,name,importance);
            notificationChannel.setDescription(description);

            //알림매니저생성
            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

            //알림매니저에 채널등록
            notificationManager.createNotificationChannel(notificationChannel);

    }
}


//프래그먼트 교체가 일어나는 실행문
    private void setFrag(int n){
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        switch (n){
            case 0:
                ft.replace(R.id.main_frame,frag1);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.main_frame,frag2);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.main_frame,frag3);
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.main_frame,frag4);
                ft.commit();
                break;
        }


    }
}