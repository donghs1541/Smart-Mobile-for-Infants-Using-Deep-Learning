import socket
import cv2
import numpy
import dht_22
import microdust
from pathlib import Path
import numpy
import cv2
from face_detection.detect2 import detect

soc = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
soc.connect(("113.198.234.39", 55000))
user = 'pi'
soc.sendall(user.encode())
cam = cv2.VideoCapture(0)  # 저장돼있는 영상 전송
while True:
    print("recving")
    choice_byte = soc.recv(1024) # the number means how the response can be in bytes
    choice = choice_byte.decode("utf8")  # the return will be in bytes, so decode
    print("recevied",choice)
    #soc.sendall(choice.encode())
    if choice == '000': # 센서값만 보내는 경우
        if True:
            ret, frame = cam.read()
            if ret:
                baby_face_result = detect(frame)
                print(baby_face_result)


            temp, humidity = dht_22.getdht22() #dht22 센서받기
            dust = microdust.getDust()  #미세먼지 값 받기
            result_send = str(temp)+" "+str(humidity)+" "+dust+" "+baby_face_result #최종값 더함
            soc.send(result_send.encode("utf8")) # we must encode the string to bytes

    elif choice == '111': #영상만 보내는 경우
        while True:
            choice_byte = soc.recv(1024) # the number means how the response can be in bytes
            choice = choice_byte.decode("utf8")  # the return will be in bytes, so decode
            if choice == '444':
                break
            print(choice)
            if cam.isOpened():
                ret, frame = cam.read()
                if ret:
                    encode_param = [int(cv2.IMWRITE_JPEG_QUALITY), 10]

                    result, frame = cv2.imencode('.jpg', frame, encode_param)
                    # frame을 String 형태로 변환
                    data = numpy.array(frame)
                    stringData = data.tostring()

                    soc.sendall((str(len(stringData))).encode().ljust(16) + stringData)
