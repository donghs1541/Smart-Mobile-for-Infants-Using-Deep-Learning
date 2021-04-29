import spidev
import RPi.GPIO as GPIO
import time

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BCM)
GPIO.setup(21,GPIO.OUT)


spi=spidev.SpiDev()

spi.open(0,0)

spi.max_speed_hz=500000

def read_spi_adc(adcChannel):

   adcValue=0

   buff =spi.xfer2([1,(8+adcChannel)<<4,0])

   adcValue = ((buff[1]&3)<<8)+buff[2]

   return adcValue
def getDust():
   try :
      adcChannel=0
      GPIO.output(21,GPIO.LOW)
      time.sleep(0.00028)
      adcValue=read_spi_adc(adcChannel)
      time.sleep(0.00004)
      GPIO.output(21,GPIO.HIGH)
      time.sleep(0.00968)


      calVoltage=adcValue*5/1024


      dust = (calVoltage-0.1)/0.005


      return str(dust)

   except Exception as e:
       print(e)
