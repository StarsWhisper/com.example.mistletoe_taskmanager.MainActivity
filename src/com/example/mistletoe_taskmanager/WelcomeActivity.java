package com.example.mistletoe_taskmanager;

import android.app.Activity;  
import android.content.Intent;  
import android.os.Bundle;  
import android.os.Handler;  


public class WelcomeActivity extends Activity{

	 private final int SPLASH_DISPLAY_LENGHT = 4000; // —”≥Ÿ¡˘√Î  
	 
	 protected void onCreate(Bundle savedInstanceState) {
	    	 super.onCreate(savedInstanceState);  
	         setContentView(R.layout.welcome_layout);  
	         new Handler().postDelayed(new Runnable() {  
	            public void run() {  
	                 Intent mainIntent = new Intent(WelcomeActivity.this,MainActivity.class);
	                 WelcomeActivity.this.startActivity(mainIntent);  
	                 WelcomeActivity.this.finish();  
	             }  
	   
	        }, SPLASH_DISPLAY_LENGHT);  
	   
	    }  


}
