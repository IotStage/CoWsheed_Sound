package com.example.mainscreenactivity;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;

import android.app.Activity;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Main extends Activity implements OnClickListener, OnCompletionListener{
    
    private Button buttonRecord, buttonPlay, buttonUpload, buttonDownload;
    private EditText editTextAPI;
    private MediaRecorder recorder;
    private String fileName = "test.3gp";
    private String nomFichier = "son";
    private File dataDir,fiche;
    private ProgressDialog pDialog;
    String taille ="0";
    String mot = "rien";
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;


    JSONParser jsonParser = new JSONParser();
    private static String url_create_product = "http://10.130.1.1/son.php";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        trace("start");

        this.dataDir = new File(Environment.getExternalStorageDirectory(), this.getPackageName());
        dataDir.mkdirs();

        buttonDownload = (Button)findViewById(R.id.ButtonDownload);
        buttonDownload.setOnClickListener(this);
        buttonRecord = (Button)findViewById(R.id.ButtonRecord);
        buttonRecord.setOnClickListener(this);
        buttonPlay = (Button)findViewById(R.id.ButtonPlay);
        buttonPlay.setOnClickListener(this);
        buttonUpload = (Button)findViewById(R.id.ButtonUpload);
        buttonUpload.setOnClickListener(this);
        
        editTextAPI = (EditText)findViewById(R.id.EditTextAPI);
    }

    //Converting a bytes array to string of hex character
    public static String byteArrayToHexString(byte[] b) {
        int len = b.length;
        String data = new String();

        for (int i = 0; i < len; i++){
            data += Integer.toHexString((b[i] >> 4) & 0xf);
            data += Integer.toHexString(b[i] & 0xf);
        }
        return data;
    }
    public void onClick(View arg0) {
        switch(arg0.getId()){
        case R.id.ButtonRecord:
            if(recorder == null){
                trace("start record");
                try{
                    this.recorder = new MediaRecorder();
                    this.recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    this.recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
                    this.recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    
                    this.recorder.setOutputFile(new File(dataDir, fileName).getAbsolutePath());
                    this.recorder.prepare();
                    this.recorder.start();
                    this.buttonRecord.setText(R.string.button_record_stop);
                }
                catch(Exception e){
                    e.printStackTrace();
                    error(e.toString());
                }
            }
            else{
                trace("stop record");
                try{
                    this.recorder.stop();
                    this.recorder.release();
                    this.recorder = null;
                    this.buttonRecord.setText(R.string.button_record);
                }
                catch(Exception e){
                    e.printStackTrace();
                    error(e.toString());
                }
            }
            break;
        case R.id.ButtonPlay:
            trace("play");
            try{
                MediaPlayer player = new MediaPlayer();
                player.setDataSource(new File(dataDir, fileName).getAbsolutePath());
                player.prepare();
                player.seekTo(0);
                player.start();
                File f = new File(new File(dataDir, fileName).getAbsolutePath());
                byte[] buffer1 = new byte[(int)f.length()];
                FileInputStream readFile = new FileInputStream (new File(dataDir, fileName).getAbsolutePath());
                readFile.read(buffer1);
                readFile.close();
                mot = byteArrayToHexString(buffer1);
               // mot = afficheUltraRapide(new File(dataDir, fileName).getAbsolutePath());
                int t = mot.length();
                taille = Integer.toString(t);
                Toast.makeText(getApplicationContext(), taille ,Toast.LENGTH_SHORT).show();
                //System.out.println(mot);
                player.setOnCompletionListener(this);
            }
            catch(Exception e){
                e.printStackTrace();
                error(e.toString());
            }
            break;
        case R.id.ButtonUpload:
            trace("upload");
            CreateNewProduct task = new Main.CreateNewProduct();
            task.execute();
            /*try{
                upload(new File(dataDir, fileName), this.editTextAPI.getText().toString());
            }
            catch(Exception e){
                e.printStackTrace();
                error(e.toString());
            }*/
            case R.id.ButtonDownload:
                trace("play");
                //Make call to AsyncRetrieve
                new Main.Takesong().execute();




        }
    }
    class CreateNewProduct extends AsyncTask<String,String,String> {



        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pDialog = new ProgressDialog(Main.this);
            pDialog.setMessage("tentative d'envoi..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub

            //String name = inputName.getText().toString();
            //String price = inputPrice.getText().toString();
            //String description = inputDesc.getText().toString();
            String data = mot.substring(0,20);
            String data2 = mot.substring(20,40);




            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair ("lat",taille));
            params.add(new BasicNameValuePair ("long",data));
            params.add(new BasicNameValuePair("rssi",mot));


            JSONObject json = jsonParser.makeHttpRequest(url_create_product,"POST",params);

            //Log.d("Create Response", json.toString());

            //try{
            //	int success = json.getInt(TAG_SUCCESS);

            //	if(success==1){

            Intent i = new Intent(getApplicationContext(),MainScreenActivity.class);
            startActivity(i);
            //Toast.makeText(getApplicationContext(), "Message envoyé", Toast.LENGTH_SHORT).show();
            finish();

            //	} else {

            //	}

            //} catch(JSONException e){
            //	e.printStackTrace();
            //}


            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            // TODO Auto-generated method stub
            pDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Message envoyé", Toast.LENGTH_SHORT).show();
        }



    }
    public static String afficheUltraRapide(String nomFichier) {
        String retour;
        try{
            File f = new File(nomFichier);
            byte[] buffer = new byte[(int)f.length()];
            DataInputStream in = new DataInputStream(new FileInputStream(f));
            in.readFully(buffer);
            //System.out.println(in);
            for(int i=0;i<buffer.length;i++)
            System.out.println(buffer[i]);
            System.out.println(buffer.length);
            in.close();
            retour = new String(buffer);
            return retour;
        } catch (FileNotFoundException e) {
            System.out.println("Impossible de lire le fichier "+nomFichier+" ! " +e);
            return "";
        } catch (IOException e) {
            System.out.println("Erreur de lecture !" +e);
            return "";
        }
    }
    public void upload(File file, String uri) throws Exception{
        try{
            /*HttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost(uri);
                        
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            entity.addPart("file", new FileBody(file));
            post.setEntity(entity);
            post.setHeader("User-Agent", "TestAndroidApp/0.1");
            HttpResponse res = httpClient.execute(post);
            trace(res.getEntity().getContent().toString());*/
            Toast.makeText(getApplicationContext(), "Connection " ,Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            throw e;
        }
    }

    private class Takesong extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(Main.this);
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
                url = new URL("http://10.130.1.1/son.php");

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

                // setDoOutput to true as we receive data from json file
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
            try{
            pdLoading.dismiss();
            //if(result.equals("Success! This message is from PHP")) {
            String message;
                //String affichage = "Liste des Messages recu \n";
            message = result.toString();



            //}else{
            // you to understand error returned from doInBackground method
                getsong(message);
            }
            catch(Exception e){
                e.printStackTrace();
                //throw e;
            }
        }

    }
    public void getsong(String message) throws IOException{
        try{
            //try get son
            File f = new File(new File(dataDir, fileName).getAbsolutePath());
            byte[] buffer1 = new byte[(int)f.length()];
            FileInputStream readFile = new FileInputStream (new File(dataDir, fileName).getAbsolutePath());
            readFile.read(buffer1);
            readFile.close();
            String sound = byteArrayToHexString(buffer1);
            String mot = new String(buffer1);
            int t = message.length();
            taille = Integer.toString(t);
            FileOutputStream fichier2 = openFileOutput(nomFichier,getApplicationContext().MODE_PRIVATE);
            fichier2.write(hexStringToByteArray(message));
            fichier2.close();
            fiche = getFileStreamPath(nomFichier);
            String texto = fiche.getAbsolutePath();
            byte[] buffer2 = new byte[(int)f.length()];
            FileInputStream readFile2 = new FileInputStream (new File(texto));
            readFile2.read(buffer2);
            //Toast.makeText(getApplicationContext(), message ,Toast.LENGTH_SHORT).show();
            try{
                MediaPlayer player = new MediaPlayer();
                player.setDataSource(fiche.getAbsolutePath());
                player.prepare();
                player.seekTo(0);
                player.start();
                player.setOnCompletionListener(this);
            }
            catch(Exception e){
                e.printStackTrace();
                error(e.toString());
            }
        }
        catch(Exception e){
            error(e.toString());
        }
    }
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2){
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    public void trace(String message){
        Resources res = getResources();
        Log.v(res.getString(R.string.app_name), message);
    }
    
    public void error(String message){
        Resources res = getResources();
        Log.e(res.getString(R.string.app_name), message);
    }

    public void onCompletion(MediaPlayer mp) {
        mp.stop();
        mp.setOnCompletionListener(null);
        mp.release();
        mp = null;
    }

}
