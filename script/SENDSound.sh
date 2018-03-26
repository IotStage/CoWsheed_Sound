#!/bin/sh

read index<index.txt
read curseur<curseur.txt
index=$(($index + 1))

if [ $curseur -lt 1 ];then
        id=$(mysql -uroot -proot -N -e "SELECT id FROM lora.sendSound WHERE sendSound.id=$index")
        if [ -z $id ];then
            echo "0"
        else
                nom=$(mysql -uroot -proot -N -e "SELECT nom FROM lora.sendSound WHERE sendSound.id=$index")
                if [ -z $nom ];then
                nom="0"
                fi
                nom="0"
                numero="1"
                message=$(mysql -uroot -proot -N -e "SELECT message FROM lora.sendSound WHERE sendSound.id=$index")
                if [ ${#message} -gt 60 ];then
                         message=$(echo $message | cut -c1-60)
                         echo 61 > curseur.txt
                         echo "1-"$numero"-"$message
                else
                echo $index > index.txt


                echo $nom"-"$numero"-"$message
                fi
                message=""
        fi
else
        nom="*"
        numero="1"
        message=$(mysql -uroot -proot -N -e "SELECT message FROM lora.sendSound WHERE sendSound.id=$index")
        if [ -z $message ];then
                echo "01"
        else
                messages=$(echo $message| cut -c$curseur-$(($curseur+59)))
                curseur=$(($curseur+60))
                echo $curseur > curseur.txt

                if [ ${#message} -lt $curseur ];then

                        echo $index > index.txt
                        echo "0" > curseur.txt
                        echo "0-"$numero"-"$messages
                else
                        echo "1-"$numero"-"$messages
                fi
        fi
fi