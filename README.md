# Smart-Mobile-for-Infants-Using-Deep-Learning
딥러닝으로 애기 뒤집기 파악 


구성품 및 사용한 기술 스텍
  라즈베리파이 (라즈비안 os)
    온도/습도센서 (dht-22)
    미세먼지센서
    카메라 
    Pycharm(Python)
    Yolov5 // https://github.com/ultralytics/yolov5  yolov5 모델
  안드로이드(앱)
    Android Studio(JAVA)
  서버(컴퓨터) 
    Pycharm(Python)
    Maria DB (RDB)
    PHP (Web Streaming)
    
  
![first_icon](https://user-images.githubusercontent.com/68945145/116649473-47e2f700-a9ba-11eb-94b0-ddfc351e02aa.png)



모빌 <-> 서버 <-> 안드로이드 통신 

모빌 Client.py

미세먼지 센서를 이용한 미세먼지 측정
온/습도 센서를 이용한 실내 온습도 측정
카메라 : Yolov5를 이용한 얼굴인식을 통해 아기 뒤집기 감지

Server.py
클라이언트 간 Data 송수신 중계
모빌에서 받은 카메라 내용을 Web Live Streaming함(CCTV 기능) 앱 사용자가 CCTV기능을 볼 수 있음 . 

Android

미세먼지 및 온습도 확인(모빌 내의)
영아 문제 여부 확인 및 알림(뒤집기판단을 알람으로 통함 (질식사예방))
CCTV기능을 통해 모빌에 비추어지는 영아 상태확인
사용자 맞춤 정보 제공(아기 정보)





v1.0

android_clinet.py 안드로이드용(앱 ) 소켓통신 잘되는지확인 (아직 파이썬인데 자바로 바꿀예정)
pi_clinet.py (라즈베리파이 모빌위에 달꺼 기능 )(아직 덜함)
server.py(서버  라즈베리파이와 앱의 통신기능 해줌

v2.0

라즈베리파이에 센서를 입력하여 센서값을 출력 (성공)(미세먼지센서, 온도/습도센서)
라즈베리파이에 yolov5를 이용하여 (pytorch필요 1.7.0임 numpy 등등 필요) // https://github.com/ultralytics/yolov5(yolov5링크)
사람과 얼굴 두가지의 사람형태를 학습하여 적용함

v2.1
라즈베리파이 client 수정

v2.2 
안드로이드 앱 기능 추가 

