package com.example.mainscreenactivity;
/**
 * Created by Robert on 23/01/2018.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Bloc extends PreferenceActivity
{
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    private static String url_create_product = "http://10.130.1.1/blocmonitor.php";

    private static final String TAG_SUCCESS = "success";
    private static final int EDIT_ID = Menu.FIRST+2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.mainbloc);
        //setHasOptionsMenu(true);
        Preference button = findPreference("button");
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //code for what you want it to do
                Bloc.CreateNewProduct task = new CreateNewProduct();
                task.execute();
                return true;
            }
        });
        bindPreferenceSummaryToValue(findPreference("Hopital"));
        bindPreferenceSummaryToValue(findPreference("Service"));
        bindPreferenceSummaryToValue(findPreference("bloc"));
        bindPreferenceSummaryToValue(findPreference("etat"));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, EDIT_ID, Menu.NONE, "Valider")
                .setIcon(R.drawable.pattern)
                .setAlphabeticShortcut('e');

        return(super.onCreateOptionsMenu(menu));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == EDIT_ID) {
            Bloc.CreateNewProduct task = new CreateNewProduct();
            task.execute();
            // startActivity(new Intent(getApplicationContext(), MainScreenActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };
    class CreateNewProduct extends AsyncTask<String,String,String> {



        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pDialog = new ProgressDialog(Bloc.this);
            pDialog.setMessage("tentative d'envoi..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub

            ListPreference listPreference10 = (ListPreference) findPreference("Hopital");
            CharSequence Hopital = listPreference10.getEntry();
            String hopital = (String)Hopital;
            ListPreference listPreference1 = (ListPreference) findPreference("Service");
            CharSequence Service = listPreference1.getEntry();
            String service = (String)Service;
            ListPreference listPreference2 = (ListPreference) findPreference("bloc");
            String salle = listPreference2.getValue();
            ListPreference listPreference3 = (ListPreference) findPreference("etat");
            //CharSequence Lit = listPreference3.getEntry();
            String lit = listPreference3.getValue();
            //String lit = listPreference3.getValue();


            String description = "";
            Date d = new Date();
            SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
            String s = f.format(d);
            String heure = s.substring(9,11);
            heure += ":";
            heure += s.substring(11,13);
            heure += ":";
            heure += s.substring(13,15);
            description += "-"+heure;

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("hop",hopital));
            params.add(new BasicNameValuePair ("serv",service));
            params.add(new BasicNameValuePair("bloc",salle));
            params.add(new BasicNameValuePair("etat",lit));

            JSONObject json = jsonParser.makeHttpRequest(url_create_product,"POST",params);

            //Log.d("Create Response", json.toString());

            //try{
            //	int success = json.getInt(TAG_SUCCESS);

            //	if(success==1){

            Intent i = new Intent(getApplicationContext(),Bloc.class);
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
            Toast.makeText(getApplicationContext(), "Alerte envoyé", Toast.LENGTH_SHORT).show();
        }



    }
}

