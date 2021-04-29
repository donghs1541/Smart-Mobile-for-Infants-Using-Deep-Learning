import socket
import argparse
import threading
import time
import numpy
import cv2

host = "113.198.234.49"
port = 55000
user_list = {}
notice_flag = 0
andtopi_temp = "000"

lock = threading.Lock()

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





def handle_receive():
    while 1:
        choice = user_list[b'android'].recv(1024)
        choice = choice.decode()

        user_list[b'pi'].sendall(choice.encode())
        if choice == "000":

            data = user_list[b'pi'].recv(1024)
            string = data.decode()

            user_list[b'android'].sendall(string.encode())
            print("android to pi : ", string)
            print("andtopi_temp :" ,andtopi_temp)


        elif choice =='111':
            global img_recv
            threading.Thread(target=recv_img, args=()).start()
            while img_recv == '333':
                user_list[b'pi'].sendall(img_recv.encode())

                length = recvall(user_list[b'pi'], 16)
                stringData = recvall(user_list[b'pi'], int(length))
                data = numpy.frombuffer(stringData, dtype='uint8')
                decimg = cv2.imdecode(data, 1)
                cv2.imshow('Image', decimg)

                key = cv2.waitKey(1)
                if key == 27:
                    break



            user_list[b'pi'].sendall('444'.encode())
            img_recv = '333'
    user_list[b'pi'].close()
    user_list[b'android'].close()

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
    print(user_list[user])



    threading.Thread(target=handle_receive, args=()).start()


accept_func()

