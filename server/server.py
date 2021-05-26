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

img_recv = '333'
def recv_img():
    global img_recv
    recv_text = user_list[b'android'].recv(1024)
    recv_text = recv_text.decode()
    if recv_text == '123':
        lock.acquire()
        img_recv = '444'
        lock.release()


def handle_receive(user):
    while 1:
        choice = user_list[b'android'].recv(1024)
        choice = choice.decode()

        user_list[b'pi'].sendall(choice.encode())
        if choice == "000":

            data = user_list[b'pi'].recv(1024)
            string = data.decode("utf-8").rstrip()
            vysl = string.encode("utf8")
            user_list[b'android'].sendall(vysl)


            user_list[b'android'].sendall(bytes(string.encode()))
            print("android to pi : ", string)
            print("andtopi_temp :" ,andtopi_temp)


        elif choice =='111':
            global img_recv
            threading.Thread(target=recv_img, args=()).start()
            if img_recv == '333':
                gen_frames()

            user_list[b'pi'].sendall('444'.encode())
            img_recv = '333'
    user_list[b'pi'].close()
    user_list[b'android'].close()


def gen_frames():  # generate frame by frame from camera
    global img_recv
    while img_recv == "333":
        try:
            user_list[b'pi'].sendall(img_recv.encode())


            length = recvall(user_list[b'pi'], 16)
            stringData = recvall(user_list[b'pi'], int(length))
            data = numpy.frombuffer(stringData, dtype='uint8')
            frame = cv2.imdecode(data, 1)

            ret, buffer = cv2.imencode('.jpg', frame)
            frame = buffer.tobytes()
            yield (b'--frame\r\n'
    
                       b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n')
        except:
            pass


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

    server_socket.bind(("113.198.234.49", 55000))
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

