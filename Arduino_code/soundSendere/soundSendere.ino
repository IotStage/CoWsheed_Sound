
#include "SX1276.h"
#include <Console.h>
#include <SPI.h>
#include <OneWire.h>
#include <Bridge.h>

#define BAUDRATE 115200
int e;
char message1 [80];
String text="";
uint8_t pl;
uint8_t pl1;
int i=0;
uint8_t mode=1;
void setup() {
  // Initialize the Bridge and the Console
  Bridge.begin(BAUDRATE);
  Console.begin();
  //while (!Console) ; // Wait for console port to be available
  Console.println("Start Sketch");

  sx1276.ON();
  e = sx1276.setMode(mode);
  e = sx1276.setChannel(CH_10_868);
  e = sx1276.setPower('M');
  e = sx1276.setNodeAddress(6);
   Console.println(F("SX1276 successfully configured"));
}
 
void loop() {
Process recupdata;
  //recupdata.begin("pwd");
  //recupdata.addParameter("/root/script.sh");
  //recupdata.run();
  recupdata.runShellCommand("ls -la /root");
  i=0;
  text="";
  while (recupdata.available()) {
    char c =(char)recupdata.read();
    Console.print(recupdata.read());
    //message1[i] = c;
    text +=String(c);
    //i++;
  }
  Console.println(text);
  char buff[text.length()+1];
  text.toCharArray(buff, text.length()+1);
  sx1276.sendPacketTimeout(2, buff);

//    Console.print(message1);
//    if(message1[0]=='0'){ // Si il n'ya rien a envoyer de nouveau
//      Console.println("ras");
//      Console.println(message1);
//    }
//    else{
//           Console.println("something");
//           Console.println(message1);
//           pl=strlen((char*)(&message1));  
//           e = sx1276.sendPacketTimeout(2, (uint8_t*)(&message1[i]), pl, 10000);
//        }

delay(3000);
}
