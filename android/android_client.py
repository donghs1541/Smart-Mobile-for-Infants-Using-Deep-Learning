import socket
import argparse
import threading

port = 55000

##################안드로이드
def handle_receive(num, client_socket, user):
    while 1:
        try:
            data = client_socket.recv(1024)
        except:
            print("연결 끊김")
            break
        data = data.decode()
        #print(data)

        if (not user in data) and data != '111' and data != '000':
            print("pi to android value :" ,data)

def handle_send(num, client_socket):
    while 1:
        data = input()
        client_socket.sendall(data.encode())
        if data == "/종료":
            break
    client_socket.close()


if __name__ == '__main__':

    host = "113.198.234.39"
    port = 55000
    user = "client"

    client_socket = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
    client_socket.connect((host, port))
    client_socket.sendall(user.encode())


    receive_thread = threading.Thread(target=handle_receive, args=(1, client_socket, user))
    receive_thread.daemon = True
    receive_thread.start()

    send_thread = threading.Thread(target=handle_send, args=(2, client_socket))
    send_thread.daemon = True
    send_thread.start()

    send_thread.join()
    receive_thread.join()




