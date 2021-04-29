import time
import Adafruit_DHT
sensor = Adafruit_DHT.DHT22
pin=18

def getdht22():
    try:
        h,t=Adafruit_DHT.read_retry(sensor,pin)
        if h is not None and t is not None:

            print("temp:{0:0.1f}*C Humidity = {1:0.1f}%".format(t,h))
            return t,h

        else:
            print("Read error")

    except KeyboardInterrupt:
        print("exit")

