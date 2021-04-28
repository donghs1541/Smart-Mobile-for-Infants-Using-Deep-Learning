import socket
import threading
import cv2
import numpy
import dht_22
import microdust
from pathlib import Path
import numpy
import cv2
from face_detection.detect2 import detect


port = 55000

lock = threading.Lock()
##################pi

receive_value = ""
cam = cv2.VideoCapture(0)  # 저장돼있는 영상 전송]
def handle_receive(num, lient_socket, user):
    while 1:
        try:
            data = client_socket.recv(1024)
        except:
            print("연결 끊김")
            break

        data = data.decode()
        #print(data)
        global receive_value
        lock.acquire()

        if data == "000":
            if cam.isOpened():
                ret, frame = cam.read()
                if ret:
                    baby_face_result = detect(frame)
                    print(baby_face_result)

                temp, humidity = dht_22.getdht22()  # dht22 센서받기
                dust = microdust.getDust()  # 미세먼지 값 받기
                receive_value = str(temp) + " " + str(humidity) + " " + dust + " " + baby_face_result  # 최종값 더함
                if receive_value != None:
                    client_socket.sendall(receive_value.encode())
        elif data == "111":
            if cam.isOpened():
                ret, frame = cam.read()
                if ret:
                    encode_param = [int(cv2.IMWRITE_JPEG_QUALITY), 60]

                    result, frame = cv2.imencode('.jpg', frame, encode_param)
                    # frame을 String 형태로 변환
                    data = numpy.array(frame)
                    receive_value = data.tostring()
                    if receive_value != None:
                        client_socket.sendall((str(len(receive_value))).encode().ljust(16) + receive_value)
        receive_value = None

        lock.release()
    client_socket.close()


if __name__ == '__main__':

    host = "113.198.234.49"
    port = 55000
    user = "client2"

    client_socket = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
    client_socket.connect((host, port))
    client_socket.sendall(user.encode())


    receive_thread = threading.Thread(target=handle_receive, args=(2, client_socket, user))
    receive_thread.daemon = True
    receive_thread.start()
    receive_thread.join()




