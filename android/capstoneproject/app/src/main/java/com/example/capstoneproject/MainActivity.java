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

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Frag1 frag1;
    private Frag2 frag2;
    private Frag3 frag3;
    private Frag4 frag4;
    private Frag5 frag5;

    private String SendSignal = "000"; // 서버에 시그널 보내는 변수


    String Message = "android";
    String txtRecevie = "";
    byte[] buffer = new byte[1024]; //읽어오는 버퍼 크기
    Socket client;
    OutputStream  outputStream;
    ByteArrayOutputStream byteArrayOutputStream;
    PrintWriter out;
    InputStream inputStream;
    BufferedReader reader ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        frag5 = new Frag5();


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

                        out.write(SendSignal);
                        out.flush();

                        if (SendSignal =="000") { //센서값 받기 
                            int size = inputStream.read(buffer);
                            txtRecevie = new String(buffer, 0, size, "UTF-8");
                            System.out.println("asdasdasdasd" + txtRecevie);
                            frag1.setSenSorValue(txtRecevie);
                            try{
                                Thread.sleep(3000); // 3초지연
                            }
                            catch (Exception e){
                                e.printStackTrace(); //오류 출력
                            }


                        }
                        else if (SendSignal=="111"){ //CCTV일때
                            System.out.println("기다리기");
                        }

                    }
                }
                catch (IOException e){
                    System.out.print("IOException: " + e.toString());
                }

            }};worker.start();


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
                startActivity(new Intent(getApplicationContext(),Frag5.class));
                break;
        }


    }
}