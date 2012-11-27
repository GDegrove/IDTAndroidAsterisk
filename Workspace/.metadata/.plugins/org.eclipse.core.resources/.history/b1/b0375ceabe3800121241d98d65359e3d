package com.example.myandroidapp;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		try {

            //URL url = new URL("http://"+params[0]);
			URL url = new URL("http://"+"74.125.132.94");
            System.out.println("url created");

            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
            urlc.setRequestProperty("User-Agent", "Android Application:"+"v1");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(1000 * 30); // mTimeout is in seconds
            System.out.println("trying to connect...");
            urlc.connect();
            if (urlc.getResponseCode() == 200) {
            	//	Main.Log("getResponseCode == 200");
            	System.out.println("got response 200");
            	return;
            }
		} catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
