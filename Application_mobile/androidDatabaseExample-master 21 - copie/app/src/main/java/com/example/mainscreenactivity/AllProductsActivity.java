package com.example.mainscreenactivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class AllProductsActivity extends ListActivity {
	
	private ProgressDialog pDialog;
	
	JSONParser jParser = new JSONParser();
	
	ArrayList<HashMap<String,String>> productsList;
	
	private static String url_all_products = "http://10.130.1.1/message.php";
	
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCTS = "id";
	private static final String TAG_PID = "lat";
	private static final String TAG_NAME = "long";
	
	JSONArray products = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_products);
		
		productsList = new ArrayList<HashMap<String,String>>();
		
		new LoadAllProducts().execute();
		
		ListView lv = getListView();
		
		lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
					String pid = ((TextView)view.findViewById(R.id.pid)).getText().toString();
					
					Intent in = new Intent(getApplicationContext(),EditProductActivity.class);
					
					in.putExtra(TAG_PID,pid);
					
					startActivityForResult(in, 100);
					
			}
			
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	
		if(resultCode==100){
			
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}
	}
		


class LoadAllProducts extends AsyncTask<String,String,String>{

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		
		pDialog = new ProgressDialog(AllProductsActivity.this);
		pDialog.setMessage("Loading products. Please wait...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		pDialog.show();
	}

	@Override
	protected String doInBackground(String... args) {
		// TODO Auto-generated method stub
		List<NameValuePair> params  = new ArrayList<NameValuePair>();
		
		JSONObject json = jParser.makeHttpRequest(url_all_products,"GET",params);
		
		//Log.d("All Products: ", json.toString());
		
		try {
		//	int success = json.getInt(TAG_SUCCESS);
			
		//	if(success == 1){
				products = json.getJSONArray(TAG_PRODUCTS);
				
				for(int i = 0; i<products.length();i++){
					JSONObject c = products.getJSONObject(i);
					
					String id = c.getString(TAG_PID);
					String name = c.getString(TAG_NAME);
					
					HashMap<String, String> map = new HashMap<String, String>();
					
					map.put(TAG_PID, id);
					map.put(TAG_NAME, name);
					
					productsList.add(map);
				}
				
			//} else {
				//Intent i = new Intent(getApplicationContext(),
                 //       NewProductActivity.class);
				
				//i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			//	startActivity(i);
		//	}
		} catch (JSONException e){
		e.printStackTrace();
		}
		
		return null;
	}
	
	protected void onPostExecute(String file_url){
		
		pDialog.dismiss();
		
		runOnUiThread(new Runnable (){
			public void run(){
				ListAdapter adapter = new SimpleAdapter (
						AllProductsActivity.this,productsList,
						R.layout.list_item,new String[]{TAG_PID,TAG_NAME},
						new int [] {R.id.pid, R.id.name});
				
				setListAdapter(adapter);
						
			}
		});
	}
	
}
}