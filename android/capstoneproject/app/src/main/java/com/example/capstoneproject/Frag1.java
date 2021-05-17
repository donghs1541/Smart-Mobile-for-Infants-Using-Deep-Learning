package com.example.capstoneproject;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Frag1 extends Fragment {

    TextView babyage1,babyage2,babyage3,babyage4,current_data,temper_value,humi_value,dust_value,baby_birth,dust_result;
    ImageView dust_image;
    String SenSorValue;
    String str,temp_temper,temp_humi,temp_dust,face_detection_value;
    String babyage1_year,babyage1_mon,babyage2_mon,babyage2_date, babyage3_value,babyage3_value_remain, babyage4_value;
    String dust_contition;
    public final String CHANNEL_ID = "my_notification_channel";
    public final int NOTIFICATION_ID = 101;
    private View view;
    MyHandler mHandler = new MyHandler();

    public String getSenSorValue(){ //센서값 보내주기
        return SenSorValue;
    }

    public void setSenSorValue(String SenSorValue){  //센서값 받기
        temp_temper = SenSorValue.split(" ")[0];
        temp_humi = SenSorValue.split(" ")[1];
        temp_dust = SenSorValue.split(" ")[2];
        face_detection_value = SenSorValue.split(" ")[3];

        // 미세먼지 센서 값 나누기
       if (Float.parseFloat(temp_dust) <= 30.0)
           dust_contition = "good";
       else if (Float.parseFloat(temp_dust) <= 80.0 && Float.parseFloat(temp_dust) > 30.0)
           dust_contition = "normal";
       else if (Float.parseFloat(temp_dust) <= 150.0 && Float.parseFloat(temp_dust) > 80.0)
           dust_contition = "bad";
       else if (Float.parseFloat(temp_dust) > 150.0)
           dust_contition = "verybad";
       else
           dust_contition = "good";



        temp_temper = String.format("%.2f", Float.parseFloat(temp_temper));
        temp_humi = String.format("%.2f", Float.parseFloat(temp_humi));
        temp_dust = String.format("%.2f", Float.parseFloat(temp_dust));
        

        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // UI 작업 수행 가능
                        temper_value.setText(temp_temper);
                        humi_value.setText(temp_humi);
                        dust_value.setText(temp_dust);

                        // 미세먼지 센서 값에 따라 사진 및 텍스트 변경
                        switch (dust_contition){
                            case "good":
                                dust_image.setImageResource(R.drawable.good);
                                dust_result.setTextColor(Color.parseColor("#0100FF"));
                                dust_result.setText("좋음");
                            case "normal":
                                dust_image.setImageResource(R.drawable.normal);
                                dust_result.setTextColor(Color.parseColor("#1DDB16"));
                                dust_result.setText("보통");
                            case "bad":
                                dust_image.setImageResource(R.drawable.bad);
                                dust_result.setTextColor(Color.parseColor("#FFBB00"));
                                dust_result.setText("나쁨");
                            case "verybad":
                                dust_image.setImageResource(R.drawable.verybad);
                                dust_result.setTextColor(Color.parseColor("#FF0000"));
                                dust_result.setText("매우나쁨");
                            default:
                                dust_image.setImageResource(R.drawable.good);
                                dust_result.setTextColor(Color.parseColor("#0100FF"));
                                dust_result.setText("좋음");
                        }
                    }
                });
            }
        }).start();

        if(face_detection_value == "person")
            displayNotification();
        //textResponse.setText(SenSorValue);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


    view = inflater.inflate(R.layout.frag1,container,false);
        babyage1 = (TextView)view.findViewById(R.id.babyage1);
        babyage2 = (TextView)view.findViewById(R.id.babyage2);
        babyage3 = (TextView)view.findViewById(R.id.babyage3);
        babyage4 = (TextView)view.findViewById(R.id.babyage4);
        baby_birth = (TextView)view.findViewById(R.id.baby_birth);
        current_data = (TextView)view.findViewById(R.id.current_data);
        temper_value = (TextView)view.findViewById(R.id.temper_value);
        humi_value = (TextView)view.findViewById(R.id.humi_value);
        dust_value = (TextView)view.findViewById(R.id.dust_value);
        dust_image = (ImageView)view.findViewById(R.id.imageView4);
        dust_result = (TextView)view.findViewById(R.id.dust_result);

        //현재 시간 구하기
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = sdf.format(date);


        //태어난지 얼마나 되었는지 날짜 차이 구하기
        String date1 = getTime;
        String date2 = "2020-07-10";

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

            //날짜 데이터 처리 부분
            babyage1_year = Long.toString( calDateDays/30/12);
            babyage1_mon = Long.toString( (calDateDays/30)%12);
            babyage2_mon = Long.toString( calDateDays/30);
            babyage2_date = Long.toString( calDateDays%30);
            babyage3_value = Long.toString( calDateDays/7);
            babyage3_value_remain = Long.toString( calDateDays%7);

            str = Long.toString(calDateDays);
            str = str + "일";
        }
        catch(ParseException e)
        {
            // 예외 처리
        }

        //Textview 부분 색깔 강조
        SpannableStringBuilder ssb = new SpannableStringBuilder(str);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#01AFF1")), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);




        babyage1.setText("만 " + babyage1_year+"세 "+ babyage1_mon+"개월" );
        babyage2.setText(babyage2_mon+"개월 "+babyage2_date+"일");
        babyage3.setText(babyage3_value + "주 " + babyage3_value_remain + "일");
        babyage4.setText(ssb);
        current_data.setText(getTime);
        baby_birth.setText(date2);


    return view;

    }





    public static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            // 다른 Thread에서 전달받은 Message 처리
            if (msg.what == 1000) {

          }
        }
    }
    //알림설정
    public void displayNotification(){
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
