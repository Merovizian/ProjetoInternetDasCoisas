#include "BluetoothSerial.h"

#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif

BluetoothSerial SerialBT;

void setup() {
  pinMode(15, OUTPUT);
  pinMode(2, OUTPUT);
  pinMode(4, OUTPUT);

  Serial.begin(115200);
  SerialBT.begin("Casa Inteligente"); //Bluetooth device name
  Serial.println("The device started, now you can pair it with bluetooth!");
}

void loop() {

/// PROGRAMA

  if (SerialBT.available()) {
    char comandoRecebido = SerialBT.read();
    delay(10);

    Serial.print("COMANDO RECEBIDO: ");
    Serial.println(comandoRecebido);
    

    switch(comandoRecebido){
      case 'B':
        digitalWrite(2, HIGH);
        digitalWrite(4, LOW);
        digitalWrite(15, LOW);
        delay(100);

        break;
      case 'G':
        digitalWrite(2, LOW);
        digitalWrite(4, HIGH);
        digitalWrite(15, LOW);
        delay(100);

        break;
      case 'R':
        digitalWrite(2, LOW);
        digitalWrite(4, LOW);
        digitalWrite(15, HIGH);
        delay(100);      
        break;
      case 'C':
        digitalWrite(2, LOW);
        digitalWrite(4, LOW);
        digitalWrite(15, HIGH);
        delay(100);      
        break;
    } 
  }

}
