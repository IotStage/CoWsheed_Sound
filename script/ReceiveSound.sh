#!/bin/sh
nom=$1
numero=$2
message=$3
contenu=`cat son.txt`
data=$contenu$message
echo $data > son.txt
if [ $nom -lt 1 ];then
        mysql -uroot -proot -e "insert into lora.receiveSound(nom,numero,message
        echo "" > son.txt
fi