#include <SPI.h>
#include <MFRC522.h>
#include <Ethernet.h>
#include <BlynkSimpleEsp8266.h>
#include <ESP8266WiFi.h>
#include <Firebase.h>  
#include <FirebaseArduino.h>  
#include <FirebaseCloudMessaging.h>  
#include <FirebaseError.h>  
#include <FirebaseHttpClient.h>  
#include <FirebaseObject.h>  
 
#define SS_PIN 2
#define RST_PIN 0
#define buzzer 4 
#define led 5


// Set these to run example.
#define FIREBASE_HOST "nodemcu-test-b3159-default-rtdb.firebaseio.com"
#define FIREBASE_AUTH "89O0PzUSc0vghjgrkhzcPTOHc3iN3AZaUjRIVzyh"
#define WIFI_SSID "Act_Aravindaan"
#define WIFI_PASSWORD "aravi@123"
#define BLYNK_PRINT Serial
char auth[] = "_K_cl_ogJxVBP6uaoai_tbXL8lb_hp3N";

MFRC522 mfrc522(SS_PIN, RST_PIN);   // Create MFRC522 instance.
 
void setup() 
{
  
  pinMode(buzzer,OUTPUT);
  pinMode(led,OUTPUT);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to ");
  Serial.print(WIFI_SSID);
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("Connected to ");
  Serial.println(WIFI_SSID);
  Serial.print("IP Address is : ");
  Serial.println(WiFi.localIP());  
  pinMode(LED_BUILTIN,OUTPUT);
  Serial.begin(9600);   // Initiate a serial communication
  digitalWrite(LED_BUILTIN,LOW);
  SPI.begin();      // Initiate  SPI bus
  mfrc522.PCD_Init();   // Initiate MFRC522
  Serial.println("Approximate your card to the reader...");
  Serial.println();
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH); 
  Firebase.setString("Door Status/Door Status",""); 
  Blynk.begin(auth,WIFI_SSID,WIFI_PASSWORD);

}
void loop() 
{
  Blynk.run();
   if (Firebase.failed()) {
     Serial.print("setting /number failed:");
     Serial.println(Firebase.error());  
     return;
 }
  
  // Look for new cards
  if ( ! mfrc522.PICC_IsNewCardPresent()) 
  {
    return;
  }
  // Select one of the cards
  if ( ! mfrc522.PICC_ReadCardSerial()) 
  {
    return;
  }
  //Show UID on serial monitor
  Serial.print("UID tag :");
  String content= "";
  byte letter;
  for (byte i = 0; i < mfrc522.uid.size; i++) 
  {
     Serial.print(mfrc522.uid.uidByte[i] < 0x10 ? " 0" : " ");
     Serial.print(mfrc522.uid.uidByte[i], HEX);
     content.concat(String(mfrc522.uid.uidByte[i] < 0x10 ? " 0" : " "));
     content.concat(String(mfrc522.uid.uidByte[i], HEX));
  }
  Serial.println();
  Serial.print("Message : ");
  content.toUpperCase();
  if (content.substring(1) == "A9 F7 89 6D") //change here the UID of the card/cards that you want to give access
  {
    Serial.println("Authorized access");
    Firebase.setString("Door Status/Door Status","Access Granted");
    digitalWrite(buzzer,LOW);
    digitalWrite(led,HIGH);
    delay(2000);
    digitalWrite(led,LOW);
    Serial.println();
    delay(3000);
  }
 
 else   {
    Serial.println(" Access denied");
    Firebase.setString("Door Status/Door Status","Access Denied");
    digitalWrite(led,LOW);
    digitalWrite(buzzer,HIGH);
    delay(500);
    digitalWrite(buzzer,LOW);
    delay(500);
    digitalWrite(buzzer,HIGH);
    delay(500);
    digitalWrite(buzzer,LOW);
    delay(500);
    digitalWrite(buzzer,HIGH);
    delay(500);
    digitalWrite(buzzer,LOW);
    delay(1000);
  }
} 
