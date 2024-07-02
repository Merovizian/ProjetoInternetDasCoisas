#include "BluetoothSerial.h"

#define led 23
#define ldr 15
#define blue 5
#define green 18
#define red 19


unsigned long previousMillis = 0; 
const long interval = 5000;        


int lumens = 0, brilhoLED = 0;
bool ativoLED = false;

#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif

BluetoothSerial SerialBT;

void setup() {
  pinMode(ldr, INPUT);     // LDR
  pinMode(led, OUTPUT);    // LED PWM
  pinMode(blue, OUTPUT);   // LED AZUL
  pinMode(red, OUTPUT);    // LED VERMELHO
  pinMode(green, OUTPUT);  // LED VERDE


  digitalWrite(blue, LOW);
  digitalWrite(red, LOW);
  digitalWrite(green, LOW);
  analogWrite(led, 0);

  Serial.begin(115200);
  SerialBT.begin("Casa Inteligente");  //Bluetooth device name
  Serial.println("The device started, now you can pair it with bluetooth!");
}

void loop() {
  unsigned long currentMillis = millis();


  /// LDR
  lumens = analogRead(ldr);

  /// LED PWM
  brilhoLED = map(lumens, 200, 1650, 254, 0);
  if (brilhoLED < 60) brilhoLED = 0;
  if (brilhoLED > 200) brilhoLED = 255;

  if(ativoLED){
    analogWrite(led, brilhoLED);
    String envio = "{" + String(lumens) + "}";

    if (currentMillis - previousMillis >= interval){
      previousMillis = currentMillis;
      SerialBT.println(envio);
    }


  }else{
    analogWrite(led, 0);
  }


  
 

  if (SerialBT.available()) {
    char comandoRecebido = SerialBT.read();
    delay(50);

    Serial.print("COMANDO RECEBIDO: ");
    Serial.println(comandoRecebido);

 

    switch (comandoRecebido) {
      case 'B':
        digitalWrite(blue, HIGH);
        break;
      case 'b':
        digitalWrite(blue, LOW);
        break;

      case 'R':
        digitalWrite(red, HIGH);
        break;
      case 'r':
        digitalWrite(red, LOW);
        break;

      case 'G':
        digitalWrite(green, HIGH);
        break;
      case 'g':
        digitalWrite(green, LOW);
        break;

      case 'L':
        ativoLED = true;
        break;
      case 'l':
        ativoLED = false;
        break;

      case 'O':
        digitalWrite(blue, LOW);
        digitalWrite(red, LOW);
        digitalWrite(green, LOW);
        delay(20);
        break;  
    }
  }




  ///  CONTROLE E TESTAGEM
  Serial.print("LED: ");
  Serial.print(ativoLED);
  Serial.print(" Lumens: ");
  Serial.print(lumens);
  Serial.print(" Brilho LED: ");
  Serial.println(brilhoLED);
  delay(100);
}
