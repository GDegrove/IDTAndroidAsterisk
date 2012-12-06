package com.example.myandroidapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.database.CharArrayBuffer;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	Socket socket;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
			init();
            	
            	           	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void init() {
		
		
    	(findViewById(R.id.text_connected)).setVisibility(View.GONE);
    	(findViewById(R.id.button_reload)).setVisibility(View.GONE);    	
    	
    	View button_reload = findViewById(R.id.button_reload);
    	View button_disconnect = findViewById(R.id.button_disconnect);
    	View button_show_users = findViewById(R.id.button_show_users);
    	button_disconnect.setVisibility(View.GONE);    	
    	
    	button_reload.setOnClickListener(clickReloadListener);
    	button_disconnect.setOnClickListener(clickDisconnectListener);
    	(findViewById(R.id.button_connect)).setOnClickListener(clickConnectListener);
    	button_show_users.setVisibility(View.GONE);
    	button_show_users.setOnClickListener(clickShowUsersListener);
    	
    	(findViewById(R.id.text_users)).setVisibility(View.GONE);
    			
	}
	
	
	OnClickListener clickConnectListener = new OnClickListener() {
	    
	    public void onClick(View v) {
	        System.out.println("CLICKED");
	        TextView ip = (TextView) findViewById(R.id.TextFieldIP);
	        TextView id = (TextView) findViewById(R.id.TextFieldID);
	        TextView pwd = (TextView) findViewById(R.id.TextFieldPWD);
	        
	        connect(ip.getText().toString(), id.getText().toString(), pwd.getText().toString());
	        
	    }
	};
	
	
	OnClickListener clickDisconnectListener = new OnClickListener() {
		
		public void onClick(View v) {
			System.out.println("CLICKED");
			disconnect();			
		}
	};
	
	OnClickListener clickReloadListener = new OnClickListener() {
		
		public void onClick(View v) {
			System.out.println("CLICKED");
			String reloadCommand = "Action: Command\r\nCommand: Reload\r\n\r\n";
			try {
				(socket.getOutputStream()).write(reloadCommand.getBytes(Charset.forName("UTF-8")));
				//disconnect();
				} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};
	
	OnClickListener clickShowUsersListener = new OnClickListener() {
		public void onClick(View v) {
			
			showUsers();
		}
	};
	
	
	public boolean connect(String ip, String id, String pwd) {
		
		try {
						
			socket = new Socket(ip, 5038);
			OutputStream os = socket.getOutputStream();
			InputStream is = socket.getInputStream();
			
			//String pkt = "GET /index.html HTTP/1.1\r\nHost: 192.168.1.3\r\nConnection: Keep-Alive\r\n\r\n";
			//os.write(pkt.getBytes(Charset.forName("UTF_8")));
			
			String s = "Action: Login\r\nActionID: 1\r\nUsername: "+id+"\r\nSecret: "+pwd+"\r\n\r\n";
			
			//String s = "GET /index.html HTTP/1.1\r\nHost: 192.168.1.3\r\nConnection: Keep-Alive\r\n\r\n";
			
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			os.write(s.getBytes(Charset.forName("UTF-8")));
			
			String serverInfo = br.readLine();
			System.out.println("Server information : "+serverInfo);
			
			for(int i = 1; i < 4; i++)
				if(is.available() < 1) {
					Thread.currentThread().sleep(1000);		//wait until data is available
					if(i == 3) {
						System.out.println("Request timed out.");
						return false;
					}
				}
			
			String result = br.readLine();			
			System.out.println(result);
			
			br.readLine();		//Action id : x
			
			String serverMessage = br.readLine();
			System.out.println(serverMessage);
			
			System.out.println("...");			
			
			if(result.equals("Response: Error")) {
				System.out.println("ERROR : incorrect id/password.");
				socket.close();
				System.out.println("socket closed.");
				return false;
			}
			else if(result.equals("Response: Success")) {			//successfully connected
				
				System.out.println("Successfully logged in...");
				
				(findViewById(R.id.textMainMessage)).setVisibility(View.GONE);
				((TextView)findViewById(R.id.TextFieldIP)).setVisibility(View.GONE);
				((TextView)findViewById(R.id.TextFieldID)).setVisibility(View.GONE);
				((TextView)findViewById(R.id.TextFieldPWD)).setVisibility(View.GONE);
				(findViewById(R.id.button_connect)).setVisibility(View.GONE);
				
				(findViewById(R.id.button_disconnect)).setVisibility(View.VISIBLE);
				(findViewById(R.id.button_reload)).setVisibility(View.VISIBLE);
				
				TextView text_connected = (TextView) findViewById(R.id.text_connected);
				text_connected.setText(getString(R.string.connected)+" "+ip);
				text_connected.setVisibility(View.VISIBLE);
				
				(findViewById(R.id.button_show_users)).setVisibility(View.VISIBLE);
								
				return true;
			}		
			else {
				System.out.println("Error while receiving the response from the server.");
				return false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			}
		return false;
	}
	
	public void disconnect() {
		
		try {
			String logoff = "Action: Logoff\r\n\r\n";
			socket.getOutputStream().write(logoff.getBytes(Charset.forName("UTF-8")));
			
			socket.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error while trying to log off.");
			}
		
		(findViewById(R.id.text_users)).setVisibility(View.GONE);
		
		(findViewById(R.id.textMainMessage)).setVisibility(View.VISIBLE);
		(findViewById(R.id.TextFieldID)).setVisibility(View.VISIBLE);
		(findViewById(R.id.TextFieldIP)).setVisibility(View.VISIBLE);
		(findViewById(R.id.TextFieldPWD)).setVisibility(View.VISIBLE);
		(findViewById(R.id.button_connect)).setVisibility(View.VISIBLE);
		((TextView)findViewById(R.id.TextFieldID)).setText("");
		((TextView)findViewById(R.id.TextFieldPWD)).setText("");
		
		init();
	}
	
	public void showUsers() {
		
		String commandShowUsers = "Action: Command\r\nCommand: sip show peers\r\n\r\n";
		try {
			(socket.getOutputStream()).write(commandShowUsers.getBytes(Charset.forName("UTF-8")));
			Thread.currentThread().sleep(1000);
			
			String global = "";
			
			InputStream is = socket.getInputStream();
			while(is.available() > 0) {
				System.out.println("Data vailable");
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				global = global+br.readLine();
				global = global+br.readLine();
				global = global+br.readLine();
				global = global+br.readLine();
				global = global+br.readLine();
				global = global+br.readLine();
				global = global+br.readLine();
				global = global+br.readLine();
				System.out.println(global);
				TextView textZone = (TextView)findViewById(R.id.text_users);
				textZone.setVisibility(View.VISIBLE);
				textZone.setMovementMethod(new ScrollingMovementMethod());
				textZone.setText(global);
			}
			

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
}
