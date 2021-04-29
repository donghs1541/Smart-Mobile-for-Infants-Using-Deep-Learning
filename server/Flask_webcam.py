from flask import Flask, render_template, Response
import cv2
import socket
import threading
import numpy

def recvall(sock, count):
    buf = b''
    while count:
        newbuf = sock.recv(count)
        if not newbuf: return None
        buf += newbuf
        count -= len(newbuf)
    return buf

ip = "113.198.234.39"
port = 55555

soc = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
soc.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
soc.bind((ip, port))
print('Socket bind complete')
soc.listen(5)
conn, addr = soc.accept()

app = Flask(__name__)


def gen_frames():  # generate frame by frame from camera

    while True:

        length = recvall(conn, 16)
        stringData = recvall(conn, int(length))
        data = numpy.frombuffer(stringData, dtype='uint8')
        frame = cv2.imdecode(data, 1)

        ret, buffer = cv2.imencode('.jpg', frame)
        frame = buffer.tobytes()
        yield (b'--frame\r\n'

                   b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n')


@app.route('/video_feed')
def video_feed():
    """Video streaming route. Put this in the src attribute of an img tag."""
    return Response(gen_frames(),mimetype='multipart/x-mixed-replace; boundary=frame')


@app.route('/')
def index():
    """Video streaming home page."""
    return render_template('index.html')


if (__name__ == '__main__'):

    app.run(host='0.0.0.0', port=57575)