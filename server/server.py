import socket
from flask import Flask, render_template, Response
import argparse
import threading
import time
import numpy
import cv2

host = "113.198.234.39"
port = 55000
user_list = {}
notice_flag = 0
andtopi_temp = "000"
frame = None

lock = threading.Lock()
app = Flask(__name__)

def recvall(sock, count):
    buf = b''
    while count:
        newbuf = sock.recv(count)
        if not newbuf: return None
        buf += newbuf
        count -= len(newbuf)
    return buf

choice = '123'
choice2 = False
def recv_img():
    global choice
    global choice2
    while True:
        recv_text = user_list[b'android'].recv(1024)
        recv_text = recv_text.decode()
        print("리시브 이미지",choice)
        if recv_text == '123':
            user_list[b'pi'].sendall('444'.encode())
            lock.acquire()
            choice = '444'
            choice2 = False
            lock.release()
            break


def handle_receive(user):
    global choice
    global choice2
    global frame
    while True:
        choice = user_list[b'android'].recv(1024)
        choice = choice.decode()

        print("안드로이드 -> 서버 신호 :", choice)
        user_list[b'pi'].sendall(choice.encode())

        if choice == "000" or choice == "000000" or choice == "111000":
            Check = True
            while Check:
                try:
                    data = user_list[b'pi'].recv(1024)
                    string = data.decode("utf-8").rstrip()
                    vysl = string.encode("utf8")
                    user_list[b'android'].sendall(vysl)
                    print("android to pi : ", string)
                    Check = False
                except Exception as e :
                    print("오류",e)

        elif choice =='111' or choice == '000111' or choice == '000000111':
            print("cctv시작", choice)
            threading.Thread(target=recv_img, args=()).start()
            while True:
                print("영상전송 시작", choice, choice2)
                # user_list[b'pi'].sendall(choice.encode())

                length = recvall(user_list[b'pi'], 16)
                stringData = recvall(user_list[b'pi'], int(length))
                data = numpy.frombuffer(stringData, dtype='uint8')
                frame = cv2.imdecode(data, 1)

                ret, buffer = cv2.imencode('.jpg', frame)

                lock.acquire()
                frame = buffer.tobytes()
                choice2 = True
                lock.release()

                if choice =='444':
                    print("cctv종료", choice)
                    user_list[b'pi'].sendall('444'.encode())
                    choice = '000'
                    break;
    user_list[b'pi'].close()
    user_list[b'android'].close()


def gen_frames():  # generate frame by frame from camera
    global choice
    global choice2
    global frame
    while True:
        try:
            if choice == '111' and choice2 == True:
                yield (b'--frame\r\n'b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n')
        except Exception:
            print(Exception)


@app.route('/video_feed')
def video_feed():
    """Video streaming route. Put this in the src attribute of an img tag."""
    return Response(gen_frames(),mimetype='multipart/x-mixed-replace; boundary=frame')


@app.route('/')
def index():
    """Video streaming home page."""
    return render_template('index.html')

def accept_func():


    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)

    server_socket.bind(("113.198.234.39", 55000))
    server_socket.listen(5)
    try:
        client_socket, addr = server_socket.accept()
        print("socket 접속 성공")
    except KeyboardInterrupt:
        for user, con in user_list:
            con.close()
        server_socket.close()
        print("Keyboard interrupt")

    user = client_socket.recv(1024)
    print(user)
    user_list[user] = client_socket
    print(user_list[user])

    try:
        client_socket, addr = server_socket.accept()
        print("socket 접속 성공")
    except KeyboardInterrupt:
        for user, con in user_list:
            con.close()
        server_socket.close()
        print("Keyboard interrupt")

    user = client_socket.recv(1024)
    print(user)
    user_list[user] = client_socket
    print(user_list)



    handle_receive(user)



threading.Thread(target=accept_func, args=()).start()

app.run(host='0.0.0.0', port=57575)

