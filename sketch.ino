#include <Servo.h>
#include <Wire.h>
#include <LiquidCrystal_I2C.h>

// Objetos
Servo servoEntrada;
LiquidCrystal_I2C lcd(0x27, 16, 2);

// Pines
const int pinServoEntrada = 6;  // Servo de entrada
const int ledEntrada = 9;       // LED de entrada
const int ledSalida = 8;        // LED de salida
const int sensorIR = 7;         // Sensor infrarrojo

// Ángulo inicial del servo
int anguloInicial = 0;

void setup() {
  Serial.begin(115200);

  // Configurar servo
  servoEntrada.attach(pinServoEntrada);
  servoEntrada.write(anguloInicial);

  // Configurar LEDs
  pinMode(ledEntrada, OUTPUT);
  pinMode(ledSalida, OUTPUT);

  // Estado inicial: LEDs encendidos
  digitalWrite(ledEntrada, HIGH);
  digitalWrite(ledSalida, HIGH);

  // Configurar sensor IR
  pinMode(sensorIR, INPUT);

  // Inicializar LCD
  lcd.init();
  lcd.backlight();
  lcd.setCursor(0,0);
  lcd.print("Sistema listo");
  delay(1500);
  lcd.clear();
}

void loop() {
  // --- Sensor infrarrojo ---
  int estadoIR = digitalRead(sensorIR);
  if (estadoIR == HIGH) {
    lcd.setCursor(0,0);
    lcd.print("Muestre su QR   ");
  } else {
    lcd.setCursor(0,0);
    lcd.print("Esperando...    ");
  }

  // --- Comandos desde servidor ---
  if (Serial.available() > 0) {
    String comando = Serial.readStringUntil('\n'); 
    comando.trim();

    // --- Entrada válida ---
    if (comando == "1") {
      digitalWrite(ledEntrada, LOW);    // Apagar LED entrada
      servoEntrada.write(90);           // Mover servo
      lcd.clear();
      lcd.setCursor(0,0);
      lcd.print(">> ENTRADA <<");
      lcd.setCursor(0,1);
      lcd.print("Puerta abierta");
      delay(2000);

      servoEntrada.write(anguloInicial); // Regresar servo
      digitalWrite(ledEntrada, HIGH);    // Encender LED entrada
      lcd.clear();
      lcd.setCursor(0,0);
      lcd.print("Entrada fin");
    }

    // --- Salida válida ---
    else if (comando == "2") {
      digitalWrite(ledSalida, LOW);     
      lcd.clear();
      lcd.setCursor(0,0);
      lcd.print(">> SALIDA <<");
      lcd.setCursor(0,1);
      lcd.print("Salida autorizada");
      delay(2000);

      digitalWrite(ledSalida, HIGH);    
      lcd.clear();
      lcd.setCursor(0,0);
      lcd.print("Salida fin");
    }

    // --- Credenciales inválidas ---
    else if (comando == "0") {
      lcd.clear();
      lcd.setCursor(0,0);
      lcd.print("Acceso denegado");
      lcd.setCursor(0,1);
      lcd.print("Credenciales X");
      delay(2000);
      lcd.clear();
    }
  }
}
