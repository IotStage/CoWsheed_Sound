package com.example.mainscreenactivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.app.Activity;

public class MainActivity extends Activity {

    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    TextView textPHP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textPHP = (TextView) findViewById(R.id.textPHP);
        //Make call to AsyncRetrieve
        new AsyncRetrieve().execute();
    }
    //Converting a string of hex character to bytes
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2){
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    private class AsyncRetrieve extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
        HttpURLConnection conn;
        URL url = null;

        //this method will interact with UI, here display loading message
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        // This method does not interact with UI, You need to pass result to onPostExecute to display
        @Override
        protected String doInBackground(String... params) {
            try {
                // Enter URL address where your php file resides
                url = new URL("http://10.130.1.1/presa1.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
               // Toast.makeText(MainActivity.this, "serveur indisponibles", Toast.LENGTH_LONG).show();
                return e.toString();


            } finally {
                conn.disconnect();
            }


        }

        // this method will interact with UI, display result sent from doInBackground method
        @Override
        protected void onPostExecute(String result) {

            pdLoading.dismiss();
            //if(result.equals("Success! This message is from PHP")) {
            String message;
            String affichage = "Liste des Messages recu \n";
            int repere = 0;
             message = result.toString();
            for(int cpt = 0; cpt < message.length();cpt++){
                if(message.charAt(cpt) == '.'){
                    affichage += message.substring(repere,cpt);
                    affichage += "\n";
                    repere=cpt+1;
                }
            }


                textPHP.setText(affichage);
            //}else{
                // you to understand error returned from doInBackground method
                Toast.makeText(MainActivity.this, affichage, Toast.LENGTH_LONG).show();
            //}

        }

    }
}
