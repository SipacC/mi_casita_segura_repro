#include <Servo.h>
Servo servoEntrada;
Servo servoSalida;

int pinEntrada = 9;   
int pinSalida = 10;   
void setup() {
  Serial.begin(115200);              
  servoEntrada.attach(pinEntrada);
  servoSalida.attach(pinSalida);
  servoEntrada.write(0);
  servoSalida.write(0);
}
void loop() {
  if (Serial.available() > 0) {
    String comando = Serial.readStringUntil('\n'); 
    comando.trim();
    if (comando == "1") {   
      servoEntrada.write(90);
      delay(2000);
      servoEntrada.write(0);
    }
    else if (comando == "2") { 
      servoSalida.write(90);
      delay(2000);
      servoSalida.write(0);
    }
  }
}