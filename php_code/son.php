<?php
        $serveur="localhost:/var/run/mysqld.sock";
        $login="root";
        $pass="root";
        $base="lora";
        mysql_connect($serveur,$login,$pass)  or die("erreur de connexion au serveur");
        mysql_selectdb($base) or die("erreur de connexion a la base de donnees");
        if(isset($_REQUEST['lat'])){
                $nom = $_REQUEST['lat'];
                $num = $_REQUEST['long'];
                $message = $_REQUEST['rssi'];

                $query = "insert into sendSound (nom,numero,message) values('$nom','$num','$message')";
                $result = mysql_query($query);
        }
        else{
                $message="";
                $query = "SELECT message FROM sendSound ORDER BY 'id'";
                $result = mysql_query($query);
                while( $row = mysql_fetch_row($result)){
                        $message=$row[0];

                }
                echo $message;
        }
?>