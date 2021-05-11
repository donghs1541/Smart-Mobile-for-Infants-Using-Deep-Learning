package com.example.capstoneproject;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Frag1 extends Fragment {

    TextView textResponse,babyage,current_data;
    EditText editMsg;
    Button buttonSend;
    String SenSorValue;
    String str;
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

        textResponse = (TextView)view.findViewById(R.id.response);
        babyage = (TextView)view.findViewById(R.id.babyage);
        current_data = (TextView)view.findViewById(R.id.current_data);

        //현재 시간 구하기
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = sdf.format(date);

        //태어난지 얼마나 되었는지 날짜 차이 구하기
        String date1 = getTime;
        String date2 = "2020-02-10";

        try{
            // date1, date2 두 날짜를 parse()를 통해 Date형으로 변환.
            Date FirstDate = sdf.parse(date1);
            Date SecondDate = sdf.parse(date2);

            // Date로 변환된 두 날짜를 계산한 뒤 그 리턴값으로 long type 변수를 초기화 하고 있다.
            // 연산결과 -950400000. long type 으로 return 된다.
            long calDate = FirstDate.getTime() - SecondDate.getTime();

            // Date.getTime() 은 해당날짜를 기준으로1970년 00:00:00 부터 몇 초가 흘렀는지를 반환해준다.
            // 이제 24*60*60*1000(각 시간값에 따른 차이점) 을 나눠주면 일수가 나온다.
            long calDateDays = calDate / ( 24*60*60*1000);

            calDateDays = Math.abs(calDateDays);
            str = Long.toString(calDateDays);
            str = "태어난지" + str + "일째";
        }
        catch(ParseException e)
        {
            // 예외 처리
        }

        //Textview 부분 색깔 강조
        SpannableStringBuilder ssb = new SpannableStringBuilder(str);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#01AFF1")), 5, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        current_data.setText(getTime);
        babyage.setText(ssb);






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
