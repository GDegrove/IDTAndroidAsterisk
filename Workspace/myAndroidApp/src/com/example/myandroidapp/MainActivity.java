package com.example.myandroidapp;

import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
            	
            	View   text_ip        = findViewById(R.id.text_ip);
            	View   button_connect = findViewById(R.id.button_connect);
            	View   text_connected = findViewById(R.id.text_connected);
            	
            	View   progressCircle = findViewById(R.id.progressCircle);
            	
            	progressCircle.setVisibility(View.INVISIBLE);
            	text_connected.setVisibility(View.INVISIBLE);
            	
            	
            	button_connect.setOnClickListener(clickListener);
            	           	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	OnClickListener clickListener = new OnClickListener() {
	    
	    public void onClick(View v) {
	    	
	        System.out.println("CLICKED");
	        TextView text_ip = (TextView)findViewById(R.id.text_ip);
	        View progressCircle = findViewById(R.id.progressCircle);
	        
	        if(connect(text_ip.getText().toString(), progressCircle)) {
	        
	        	View button_connect = findViewById(R.id.button_connect);
	        	button_connect.setVisibility(View.GONE);
	        	View text_connected = findViewById(R.id.text_connected);
            	text_connected.setVisibility(View.VISIBLE);
            	TextView text_ip_connected = (TextView)findViewById(R.id.text_ip_connected);
            	text_ip_connected.setVisibility(View.VISIBLE);
            	
            	text_ip_connected.setText(text_ip.getText());
	        }
	    }
	};
	
	public boolean connect(String ip, View circle) {
		
		try {

			// Google's IP : 74.125.132.94
            //URL url = new URL("http://"+params[0]);
			URL url = new URL("http://"+ip);
            System.out.println("url created");

            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
            urlc.setRequestProperty("User-Agent", "Android Application:"+"v1");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(1000 * 3); // mTimeout is in seconds
            System.out.println("trying to connect...");
            circle.setVisibility(View.VISIBLE);
            urlc.connect();
            if (urlc.getResponseCode() == 200) {
            	//	Main.Log("getResponseCode == 200");
            	System.out.println("got response 200");
            	circle.setVisibility(View.INVISIBLE);
            	urlc.disconnect();
            	return true;
            }
            
		} catch (Exception e) {
			e.printStackTrace();
			circle.setVisibility(View.INVISIBLE);
			return false;
		}
		
		return false;


	}
}
