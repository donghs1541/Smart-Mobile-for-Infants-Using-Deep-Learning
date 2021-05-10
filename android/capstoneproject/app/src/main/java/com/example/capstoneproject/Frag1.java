package com.example.capstoneproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;





public class Frag1 extends Fragment {

    TextView textResponse;
    EditText editMsg;
    Button buttonSend;
    String Message = "android";
    PrintWriter out;
    OutputStream outputStream;

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    view = inflater.inflate(R.layout.frag1,container,false);



    return view;

    }
}
