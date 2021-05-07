package com.example.capstoneproject;

import androidx.appcompat.app.AppCompatActivity;

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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends Activity {
    EditText input1;
    Button receive;
    TextView output;

    String IpAddress = "113.198.234.39";
    int Port = 55000;
    String Message = "android";
    String txtRecevie = "";
     byte[] buffer = new byte[1024]; //읽어오는 버퍼 크기
     int bytesRead;  //
     Socket client;
     OutputStream  outputStream;
     ByteArrayOutputStream byteArrayOutputStream;
     PrintWriter out;
    InputStream inputStream;
    TextView textResponse;
    EditText editTextAddress, editTextPort, editMsg;
    Button buttonSend, buttonClear;
    BufferedReader reader ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
*/
        editMsg = (EditText)findViewById(R.id.msg);
        buttonSend = (Button)findViewById(R.id.send);
        buttonClear = (Button)findViewById(R.id.clear);
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








    }
}