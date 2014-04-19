package com.orient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;


public class Game_Final extends Activity {
	ImageView shareImageView;
	ImageView returnImageView;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.game_final);
        
        shareImageView = (ImageView)findViewById(R.id.imageView4);
        shareImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(Game_Final.this, HomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
        
        returnImageView = (ImageView)findViewById(R.id.imageView3);
        returnImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(Game_Final.this, HomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
    }
}
