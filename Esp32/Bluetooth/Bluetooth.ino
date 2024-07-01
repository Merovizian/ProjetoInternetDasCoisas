#include "BluetoothSerial.h"

#define led 23
#define ldr 15
#define blue 5
#define green 18
#define red 19

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


  /// LDR
  lumens = analogRead(ldr);

  /// LED PWM
  brilhoLED = map(lumens, 0, 4195, 255, 0);


  if (ativoLED) {
    analogWrite(led, brilhoLED);
  }else {
    analogWrite(led, 0);
  }


  if (SerialBT.available()) {
    char comandoRecebido = SerialBT.read();
    delay(10);

    Serial.print("COMANDO RECEBIDO: ");
    Serial.println(comandoRecebido);


    switch (comandoRecebido) {
      case 'B':
        digitalWrite(blue, HIGH);
        digitalWrite(red, LOW);
        digitalWrite(green, LOW);
        delay(100);

        break;
      case 'R':
        digitalWrite(blue, LOW);
        digitalWrite(red, HIGH);
        digitalWrite(green, LOW);
        delay(100);

        break;
      case 'G':
        digitalWrite(blue, LOW);
        digitalWrite(red, LOW);
        digitalWrite(green, HIGH);
        delay(100);
        break;
      case 'O':
        digitalWrite(blue, LOW);
        digitalWrite(red, LOW);
        digitalWrite(green, LOW);
        delay(100);
        break;
      case 'L':
        ativoLED = !ativoLED;
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
