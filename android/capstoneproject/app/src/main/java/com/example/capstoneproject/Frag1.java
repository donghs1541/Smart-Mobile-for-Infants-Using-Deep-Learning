package com.example.capstoneproject;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;






public class Frag1 extends Fragment {

    TextView textResponse;
    EditText editMsg;
    Button buttonSend;
    String SenSorValue;

    public final String CHANNEL_ID = "my_notification_channel";
    public final int NOTIFICATION_ID = 101;
    private View view;

    public String getSenSorValue(){ //센서값 보내주기
        return SenSorValue;
    }


    public void setSenSorValue(String SenSorValue){  //센서값 받기

        textResponse.setText(SenSorValue);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


    view = inflater.inflate(R.layout.frag1,container,false);

        editMsg = (EditText)view.findViewById(R.id.msg);
        buttonSend = (Button)view.findViewById(R.id.send);
        textResponse = (TextView)view.findViewById(R.id.response);

        Button btn_noti = (Button)view.findViewById(R.id.noti);
        btn_noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayNotification(v);
            }
        });
    return view;

    }

    //알림설정
    public void displayNotification(View v){
        createNotificationChaanel();

        //알림설정
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(),CHANNEL_ID);
        builder.setSmallIcon((R.drawable.alram_icon));
        builder.setContentTitle("2021.05.10 오마이걸 컴백");
        builder.setContentText("Dun Dun Dance");
        builder.setPriority(NotificationManagerCompat.IMPORTANCE_DEFAULT);
        builder.setAutoCancel(true);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getActivity());
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
            NotificationManager notificationManager = (NotificationManager)getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);

            //알림매니저에 채널등록
            notificationManager.createNotificationChannel(notificationChannel);

        }
    }

}
