package com.example.capstoneproject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;



public class Frag2 extends Fragment {

    private  OnTimePickerSetListener onTimePickerSetListener;

    public interface OnTimePickerSetListener{

        void onTimePickerSet(String signal);
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof Frag2.OnTimePickerSetListener){
            onTimePickerSetListener =  (OnTimePickerSetListener) context;
        }
        else{
            throw new RuntimeException(context.toString()+" 임플리먼트 해줘라");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        onTimePickerSetListener = null;
    }

    private View view;
    private WebView mWebView; // 웹뷰 선언
    private WebSettings mWebSettings; //웹뷰세팅
    private Button cctvon, cctvoff;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag2, container, false);

        cctvon = (Button)view.findViewById(R.id.cctvon);
        cctvoff = (Button)view.findViewById(R.id.cctvoff);

        cctvon.setOnClickListener(new View.OnClickListener() { //cctv on 버튼
            @Override
            public void onClick(View v) {
                onTimePickerSetListener.onTimePickerSet("111");
                mWebView.reload();
            }
        });

        cctvoff.setOnClickListener(new View.OnClickListener() { //cctv off 버튼 
            @Override
            public void onClick(View v) {
                onTimePickerSetListener.onTimePickerSet("000");
                mWebView.reload();
            }
        });

        mWebView = (WebView)view.findViewById(R.id.webview_login);
        mWebView.setWebViewClient(new WebViewClient());
        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl("http://113.198.234.39:57575"); // 주소를 바꿔야함()





        return view;

    }



}
