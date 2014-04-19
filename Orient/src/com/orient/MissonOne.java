package com.orient;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class MissonOne extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.misson);
	}
}
