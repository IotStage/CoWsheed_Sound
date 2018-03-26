
#include "SX1276.h"
#include <Console.h>
#include <SPI.h>
#include <OneWire.h>
//#include <Bridge.h>
//If you use Dragino IoT Mesh Firmware, uncomment below lines.
//For product: LG01. 
#define BAUDRATE 115200
int e;
char message1 [80];
uint8_t pl;
uint8_t pl1;
int i=0;
String nom;
int HEART_LED=A2;
void setup() {
  // Initialize the Bridge and the Console
  Bridge.begin(BAUDRATE);
  Console.begin();
  while (!Console) ; // Wait for console port to be available
  Console.println("Start Sketch");
 
  pinMode(HEART_LED, OUTPUT);

  sx1276.ON();
  sx1276.setMaxCurrent(0x1B);
  sx1276.getMaxCurrent();
  e = sx1276.setMode(1);
  e = sx1276.setChannel(CH_10_868);
  e = sx1276.setPower('M');
  e = sx1276.setNodeAddress(6);
   Console.println(F("SX1276 successfully configured"));
}

void loop() {
 Process recupdata;
  recupdata.begin("sh");
  recupdata.addParameter("/root/sendSound.sh");
  recupdata.run();
   i=0;
    while (recupdata.available() > 0) {
    char c = recupdata.read();
    message1[i] = c;
    i++;
  }
   message1[i]='\0';
  
    //Console.print(message1);
    if(message1[0]=='0'){ // Si il n'ya rien a envoyer de nouveau
      Console.println("ras");
       pl=strlen((char*)(&message1));
  uint8_t check[] = "reservation";
  uint8_t res[] = "ras";
  pl1=strlen((char*)(&check));
           pl=strlen((char*)(&res));  
 //e = sx1276.sendPacketTimeoutACK(2, (uint8_t*)(&check[i]), pl1, 10000); 
 //Console.print(e);
//if (!e) {
      // e = sx1276.sendPacketTimeout(2, (uint8_t*)(&res[i]), pl, 10000);
       //Console.println("sent");
  //  }
    }
    else{
     Console.println("something");
      Console.println(message1);
       digitalWrite(HEART_LED, HIGH);   // turn the HEART_LED on (HIGH is the voltage level)
  delay(500);              // wait for a second
  digitalWrite(HEART_LED, LOW); 
 //pl=strlen((char*)(&message1));
  uint8_t check[] = "reservation";
  pl1=strlen((char*)(&check));
  pl=strlen((char*)(&message1));  
 //e = sx1276.sendPacketTimeoutACK(2, (uint8_t*)(&check[i]), pl1, 10000); 
//if (!e) {
       e = sx1276.sendPacketTimeout(2, (uint8_t*)(&message1[i]), pl, 10000);
   // }
    }
delay(3000);
}
