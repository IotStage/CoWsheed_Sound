package com.example.mainscreenactivity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditProductActivity extends Activity{
	
	EditText txtName;
	EditText txtPrice;
	EditText txtDesc;
	EditText txtCreatedAt;
	Button btnSave;
	Button btnDelete;
	
	String pid;
	
	private ProgressDialog pDialog;
	
	JSONParser jsonParser = new JSONParser();
	
	private static final String url_product_details = "http://androidprojectmahimna.comoj.com/get_product_details.php";
	private static final String url_update_product = "http://androidprojectmahimna.comoj.com/update_product.php";
	private static final String url_delete_product = "http://androidprojectmahimna.comoj.com/delete_product.php";
	
	
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCT = "product";
	private static final String TAG_PID = "pid";
	private static final String TAG_NAME = "name";
	private static final String TAG_PRICE = "price";
	private static final String TAG_DESCRIPTION = "description";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.edit_product);
		
		btnSave = (Button) findViewById(R.id.btnSave);
		btnDelete = (Button) findViewById(R.id.btnDelete);
		
		Intent i = getIntent();
		
		pid = i.getStringExtra(TAG_PID);
		
		
		GetProductDetails task1 = new GetProductDetails();
		task1.execute();
		
		btnSave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			SaveProductDetails task2 = new SaveProductDetails();
			task2.execute();
			}
		});
		
		btnDelete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			DeleteProduct task3 = new DeleteProduct();
			task3.execute();
			}
		});
		
	}
	
class GetProductDetails extends AsyncTask<String,String,String>{		
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(EditProductActivity.this);
			pDialog.setMessage("Loading product details. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			
			runOnUiThread(new Runnable (){
				public void run(){
					
					int success;
					
					try{
						
						StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
						StrictMode.setThreadPolicy(policy);
						
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("pid",pid));
						
						JSONObject json = jsonParser.makeHttpRequest(
								url_product_details,"GET",params);
						
						Log.d("Single Product Details", json.toString());
						
						success = json.getInt(TAG_SUCCESS);
						
						if(success==1){
							
							JSONArray productObj = json.getJSONArray(TAG_PRODUCT);
							
							JSONObject product = productObj.getJSONObject(0);
							
							txtName = (EditText)findViewById(R.id.inputName);
							txtPrice = (EditText)findViewById(R.id.inputPrice);
							txtDesc = (EditText)findViewById(R.id.inputDesc);
							
							txtName.setText(product.getString(TAG_NAME));
							txtPrice.setText(product.getString(TAG_PRICE));
							txtDesc.setText(product.getString(TAG_DESCRIPTION));
							
							
							
						} else {
							
						}
					} catch (JSONException e){
						e.printStackTrace();
					}
				}
			});
			
			
			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {
			// TODO Auto-generated method stub
			pDialog.dismiss();
		}
		
		
		
	
}
	
class SaveProductDetails extends AsyncTask <String,String,String>{		
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(EditProductActivity.this);
            pDialog.setMessage("Saving product ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			String name = txtName.getText().toString();
            String price = txtPrice.getText().toString();
            String description = txtDesc.getText().toString();
 
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_PID, pid));
            params.add(new BasicNameValuePair(TAG_NAME, name));
            params.add(new BasicNameValuePair(TAG_PRICE, price));
            params.add(new BasicNameValuePair(TAG_DESCRIPTION, description));
 
            // sending modified data through http request
            // Notice that update product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_product,
                    "POST", params);
            
            Log.d("Update response", json.toString());
 
            try {
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // successfully updated
                    Intent i = getIntent();
                    // send result code 100 to notify about product update
                    setResult(100, i);
                    finish();
                } else {
                    // failed to update product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
			
			return null;
		}
		
		
		@Override
		 protected void onPostExecute(String file_url) {
	            // dismiss the dialog once product updated
	            pDialog.dismiss();
	     }
}
	
class DeleteProduct extends AsyncTask <String,String,String>{
					
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(EditProductActivity.this);
            pDialog.setMessage("Deleting Product...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			
			int success;
			
			try{
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("pid",pid));
				
				JSONObject json = jsonParser.makeHttpRequest(
						url_delete_product,"POST",params);
				
				Log.d("Delete Product", json.toString());
				
				success = json.getInt(TAG_SUCCESS);
				
				if(success==1){
					
					Intent i  = getIntent();
					
					setResult(100,i);
					finish();
				}
				
			}catch(JSONException e){
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
 
        }		
}
}

