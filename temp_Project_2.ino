#include <ESP8266WiFi.h>    //esp8266 library
#include <FirebaseArduino.h>     //firebase library
#include <DHT.h>         // dht11 temperature and humidity sensor library
#define FIREBASE_HOST "nodemcu-test-b3159-default-rtdb.firebaseio.com"  // the project name address from firebase id
#define FIREBASE_AUTH "89O0PzUSc0vghjgrkhzcPTOHc3iN3AZaUjRIVzyh"  // the secret key generated from firebase

#define WIFI_SSID "Act_Aravindaan"                  // wifi name 
#define WIFI_PASSWORD "aravi@123"                 //password of wifi 
 
#define DHTPIN 2       //D4        
#define DHTTYPE DHT11      
#define greenLed 5 //D1
#define redLed 4 //D2

String redStatus,greenStatus;
        
DHT dht(DHTPIN, DHTTYPE);                                                     

void setup() {
  pinMode(greenLed,OUTPUT);
  pinMode(redLed,OUTPUT);
  Serial.begin(9600);
  delay(1000);                
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
  Serial.println(WiFi.localIP());                  //print local IP address
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);     // connect to firebase
  dht.begin();                            //Start reading dht sensor
  Firebase.setString("Fahrenheit/Fahrenheit","");
  Firebase.setString("Celsius/Celsius","");
  Firebase.setString("Humidity/Humidity","");
  Firebase.setString("greenLed","off");
  Firebase.setString("redLed","off");
 
  
}

void loop() { 
  float h = dht.readHumidity();       // Reading temperature or humidity 
  float t = dht.readTemperature();   // Read temperature as Celsius (the default)
  float c;
    
  if (isnan(h) || isnan(t)) {  // Check if any reads failed and exit early (to try again).
    Serial.println(F("Failed to read from DHT sensor!"));
    return;
  }
  
  Serial.print("Humidity: ");  Serial.print(h);
  String fireHumid = String(h) + String("%");    //convert integer humidity to string humidity 
  Serial.print("%  Temperature: ");  Serial.print(t);  Serial.println("°C ");
  String fireTemp = String(t) + String("°C"); //convert integer temperature to string temperature
  c=( t * 1.8 ) + 32;
  String fireC = String(c);

  Firebase.setString("Fahrenheit/Fahrenheit",fireC);         //setup path and send readings
  Firebase.setString("Humidity/Humidity",fireHumid); //setup path and send readings
  Firebase.setString("Celsius/Celsius",fireTemp);

  redStatus=Firebase.getString("redLed/redLed");
  greenStatus=Firebase.getString("greenLed/greenLed");

  
  if(greenStatus == "on"){
    digitalWrite(greenLed,HIGH);
    
  }
  else if(redStatus == "on"){
    digitalWrite(redLed,HIGH);
    
  }
  else if(greenStatus == "off" && redStatus == "off"){
    digitalWrite(greenLed,LOW);
     digitalWrite(redLed,LOW);
}
   delay(4000);
 }
