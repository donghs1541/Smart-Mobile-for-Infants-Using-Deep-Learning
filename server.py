import socket
import argparse
import threading
import time

host = "113.198.234.49"
port = 55000
user_list = {}
notice_flag = 0

def handle_receive(client_socket, addr, user):
    while 1:
        data = client_socket.recv(1024)
        string = data.decode()
        if string == "/종료" :
            break
        #string = "%s : %s"%(user, string)


        print(string)
        a= 0
        for con in user_list.values():
            a= a + 1
            print(a)
            try:
                con.sendall(string.encode())
            except:
                print("연결이 비 정상적으로 종료된 소켓 발견")
    del user_list[user]
    client_socket.close()


def accept_func():

    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)

    server_socket.bind(("113.198.234.49", 55000))
    server_socket.listen(5)

    while 1:
        try:
            client_socket, addr = server_socket.accept()
        except KeyboardInterrupt:
            for user, con in user_list:
                con.close()
            server_socket.close()
            print("Keyboard interrupt")
            break

        user = client_socket.recv(1024)
        user_list[user] = client_socket

        receive_thread = threading.Thread(target=handle_receive, args=(client_socket, addr,user))
        receive_thread.daemon = True
        receive_thread.start()


if __name__ == '__main__':
    accept_func()

