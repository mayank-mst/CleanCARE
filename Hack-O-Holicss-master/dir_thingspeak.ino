#include <ESP8266WiFi.h>;
#include <WiFiClient.h>;
#include <ThingSpeak.h>;

const char* ssid = "sid"; //Your Network SSID
const char* password = "1223334444"; //Your Network Password

//LED
//int ledPin = 13; // GPIO13---D7 of NodeMCU--red
                            //G--Orange

// US Sensor
const int trigPin=2; //D4
const int echoPin=0; //D3
int counter = 0;
int currentState = 0;
int previousState = 0;
float resistance;
float NH3;
//int k=1;

// To convert read resistance into ohms
#define RLOAD 10.0
// R0 for AIR
#define r0Air 1
// R0 for CO **measured with 24hrs of exposure**
#define r0CO 69.65
// R0 for CO2 **realized 24 hrs of exposure**
#define r0CO2 553.232
// R0 for Ammonium **measured with 24hrs of exposure**
#define r0NH4 164.8282
// R0 for Acetone **measured with 24hrs of exposure**
#define scaleFactorCO 662.9382
#define exponentCO 4.0241
// Parameters Equation for CO2
#define scaleFactorCO2 116.6020682
#define exponentCO2 2.769034857
// Parameters Equation for NH4
#define scaleFactorNH4 102.694
#define exponentNH4 2.48818
#define ppm 1000000.0

WiFiClient client;
unsigned long myChannelNumber = 704326; //Your Channel Number (Without Brackets)
const char * myWriteAPIKey = "MDQGZ1VRMSVCV4VJ"; //Your Write API Key

int smokeA0 = A0;

void setup(){
  Serial.begin(9600);
  delay(10);

  //LED
  //pinMode(ledPin, OUTPUT);
  //digitalWrite(ledPin, LOW);
  
// Connect to WiFi network
  WiFi.begin(ssid, password);
  ThingSpeak.begin(client);
  pinMode(smokeA0, INPUT);
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
}
  
float getResistence(){  
   int val = analogRead(smokeA0);
   return ((1023. * RLOAD * 5.)/((float)val * 5.)) - RLOAD;   
}

/*float getCOPPM(float res){
    return scaleFactorCO * pow((res/r0CO), -exponentCO);
  }
float getCO2PPM(float res){
  return scaleFactorCO2 * pow((res/r0CO2), -exponentCO2);
}*/
float getNH4PPM(float res){
  return scaleFactorNH4 * pow((res/r0NH4), -exponentNH4);
}


void loop() { 

 long duration, distance;
 digitalWrite(trigPin, LOW); 
 delayMicroseconds(2); 
 digitalWrite(trigPin, HIGH);
 delayMicroseconds(10); 
 digitalWrite(trigPin, LOW);
 duration = pulseIn(echoPin, HIGH);
   delay(1000);
 distance = duration/58.2;
 if (distance <= 20){ //cm
  currentState = 1;
 }else {
    currentState = 0;
  }
 delay(10);
 
 if(currentState != previousState){
  if(currentState == 1){
    counter = counter + 1;
    /*resistance=getResistence();
    NH3=getNH4PPM(resistance);
     Serial.printf("No of People: %d\nNH3: %f",counter,NH3);
    Serial.printf("\n");*/
    }
  }      

    resistance=getResistence();
    NH3=(float)getNH4PPM(resistance)/ppm;
    
     Serial.printf("No of People: %d\nNH3: %f",counter,NH3);
    Serial.printf("\n");
    /*Serial.print("CO(in PPM): ");
     Serial.println(CO);
     Serial.print("CO2(in PPM): ");
     Serial.println(CO2);
     Serial.print("NH3(in PPM): ");
     Serial.println(NH3);*/

    /*    if(counter>3 && k<=1){
      digitalWrite(ledPin, HIGH);
       delay(5000);
       digitalWrite(ledPin, LOW);
       k++;
    } */

ThingSpeak.setField( 1, NH3);
ThingSpeak.setField( 2, counter);
ThingSpeak.writeFields(myChannelNumber,myWriteAPIKey);
 
  //delay(2000);
}
