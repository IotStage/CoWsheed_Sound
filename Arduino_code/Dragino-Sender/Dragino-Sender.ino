/*  
 *  LoRa 868 / 915MHz SX1272 Module
 *  
 *  Copyright (C) Libelium Comunicaciones Distribuidas S.L. 
 *  http://www.libelium.com 
 *  
 *  This program is free software: you can redistribute it and/or modify 
 *  it under the terms of the GNU General Public License as published by 
 *  the Free Software Foundation, either version 3 of the License, or 
 *  (at your option) any later version. 
 *  
 *  This program is distributed in the hope that it will be useful, 
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License 
 *  along with this program.  If not, see http://www.gnu.org/licenses/. 
 *  
 *  Version:           1.0
 *  Design:            David Gascón 
 *  Implementation:    Victor Boria & Luis Miguel Marti
 */
 #define BAUDRATE 115200
// Include the SX1272 and SPI library: 
#include "SX1276.h"
#include <Console.h>
#include <SPI.h>
#include <OneWire.h>
#define PRINTLN                   Serial.println("")              
  #define PRINT_CSTSTR(fmt,param)   Serial.print(F(param))
  #define PRINT_STR(fmt,param)      Serial.print(param)
  #define PRINT_VALUE(fmt,param)    Serial.print(param)
  #define FLUSHOUTPUT               Serial.flush();

int e;
char message1 [60];
int ntc;
int i=0;
boolean receivedFromSerial=false;
uint16_t w_timer=10000;

long startSend,endSend,debut,fin,finT;
uint8_t mode=1;
void setup()
{
  // Open serial communications and wait for port to open:
  Bridge.begin(BAUDRATE);
  Console.begin();
  // Power ON the module
  sx1276.ON();
  while (!Console) ; // Wait for console port to be available
  Console.println("Start Sketch");
  // Set transmission mode and print the result
  e = sx1276.setMode(mode);
  Console.println(F("Setting Mode: state "));
  Console.println(e, DEC);
   sx1276.setMaxCurrent(0x1B);
  sx1276.getMaxCurrent();
  
  // Select frequency channel
  e = sx1276.setChannel(CH_10_868);
  Console.println(F("Setting Channel: state "));
  Console.println(e, DEC);
  
  // Select output power (Max, High or Low)
  e = sx1276.setPower('M');
  Console.println(F("Setting Power: state "));
  Console.println(e, DEC);
  
  // Set the node address and print the result
  e = sx1276.setNodeAddress(6);
  Console.println(F("Setting node address: state "));
  Console.println(e, DEC);
  
  // Print a success message
  Console.println(F("SX1276 successfully configured"));
}

void loop(void)
{
  Console.println("");
  ntc = 123;
  /*message1[0]="d";
  message1[1]="e";
  message1[2]="b";
  message1[3]="u";
  message1[4]="t";
  message1[48]="f";
  message1[49]="i";
  message1[50]="n";
  for (ntc=5; ntc<48; ntc++)
     message1[ntc]='#';
     message1[ntc]='\0';*/   
  while(ntc<1000){
  sprintf(message1,"%d",ntc);
 // if (receivedFromSerial){
    uint8_t pl=strlen((char*)(&message1[i]));
    startSend=millis();
    e = sx1276.sendPacketTimeout(1, (uint8_t*)(&message1[i]), pl, 10000); 
    endSend=millis();
    Console.println(message1);
    Console.println(e); 

    ntc++; 
    delay(10000);
    }

}

