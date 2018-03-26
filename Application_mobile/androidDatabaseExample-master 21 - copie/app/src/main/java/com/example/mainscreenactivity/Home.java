package com.example.mainscreenactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Robert on 24/01/2018.
 */

public class Home extends Activity {
    EditText inputName;
    EditText inputPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        inputName = (EditText) findViewById(R.id.inputName);
        inputPrice = (EditText) findViewById(R.id.inputPrice);

        Button btnCreateProduct = (Button) findViewById(R.id.btnCreateProduct);

        btnCreateProduct.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String name = inputName.getText().toString();
                String price = inputPrice.getText().toString();
                if(name.equals("admin") && price.equals("passer")) {
                    Intent i = new Intent(getApplicationContext(), Design.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Erreur d'authentification", Toast.LENGTH_SHORT).show();
                }


            }


        });
    }
}
