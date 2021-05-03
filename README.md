# -
딥러닝으로 애기 뒤집기 파악 

구성품
  라즈베리파이 
    온도/습도센서 
    미세먼지센서
    카메라
  안드로이드(앱)
  서버(컴퓨터)
  
![first_icon](https://user-images.githubusercontent.com/68945145/116649473-47e2f700-a9ba-11eb-94b0-ddfc351e02aa.png)



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
