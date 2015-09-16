package com.example.joystickrobotica;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.joystickrobotica.widgets.JoystickMovedListener;
import com.example.joystickrobotica.widgets.JoystickView;


import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class JoystickActivity extends Activity {

	TextView txtX, txtY;
	EditText txt_ip,txt_port;
	JoystickView joystick;
	String url="",port="";
	Boolean flag = true;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.joystick);

		txtX = (TextView)findViewById(R.id.TextViewX);
        txtY = (TextView)findViewById(R.id.TextViewY);
        joystick = (JoystickView)findViewById(R.id.joystickView);
        txt_ip = (EditText)findViewById(R.id.txt_ip);
        txt_port = (EditText)findViewById(R.id.txt_port);
        joystick.setOnJostickMovedListener(_listener);
        
        txt_ip.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				url = String.valueOf(arg0);
			}
        	
        });
        
        txt_port.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				port = String.valueOf(arg0);
			}
        	
        });

	}

    private JoystickMovedListener _listener = new JoystickMovedListener() {

		@Override
		public void OnMoved(int pan, int tilt) {
			
			if(!url.equals("") && !port.equals("")){
			
				int x = pan;
				int y = tilt;
				txtX.setText(Integer.toString(pan));
				txtY.setText(Integer.toString(tilt));
				
				if(x>0 && y==0){
					// move to right
					if(flag){
						flag=false;
						new sendMove().execute("/right");
					}
				}else if(x<0 && y==0){
					// move to left
					if(flag){
						flag=false;
						new sendMove().execute("/left");
					}
					
				}else if(y>0 && x==0){
					// move to down
					if(flag){
						flag=false;
						new sendMove().execute("/down");
					}
					
				}else if(y<0 && x==0){
					// move to up
					if(flag){
						flag=false;
						new sendMove().execute("/up");
					}
					
				}
			}
			
		}

		@Override
		public void OnReleased() {
			txtX.setText("released");
			txtY.setText("released");
		}
		
		public void OnReturnedToCenter() {
			txtX.setText("stopped");
			txtY.setText("stopped");
		};
	};
	
	
	private class sendMove extends AsyncTask<String, String, String> {

		@Override
		protected void onPostExecute(String success) {

			flag = true;

		}
		@Override
		protected String doInBackground(String... args) {
			String status ="";
			try {
				String direction = args[0];
				HttpClient client = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet("http://" + url + ":" + port + direction);
				HttpResponse response = client.execute(httpGet);
				status = Integer.toString(response.getStatusLine().getStatusCode());
				Log.e("robotica", Integer.toString(response.getStatusLine().getStatusCode()));
				
			} catch (Exception e) {
				Log.e("robotica", "Error send move");
			}
			return null;

		}

	}

}
