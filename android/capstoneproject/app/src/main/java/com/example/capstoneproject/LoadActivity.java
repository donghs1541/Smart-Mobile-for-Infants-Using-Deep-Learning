package com.example.capstoneproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

public class LoadActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        ImageView imageView1 = (ImageView) findViewById(R.id.imageView) ;
        ImageView imageView2 = (ImageView) findViewById(R.id.imageView2) ;
        imageView1.setImageResource(R.drawable.first_icon);
        imageView2.setImageResource(R.drawable.brandname);

        Handler handler = new Handler();
     handler.postDelayed(new Runnable() {
         @Override
         public void run() {
             Intent intent = new Intent(LoadActivity.this, MainActivity.class);
             startActivity(intent);
             finish();
         }
     },3000);



    }
}


