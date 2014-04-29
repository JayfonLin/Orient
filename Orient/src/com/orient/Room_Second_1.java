package com.orient;

import java.util.Calendar;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.TimePicker;

public class Room_Second_1 extends Activity implements View.OnTouchListener {
	private ImageButton next;
	private ImageButton backHomeImageButton;
	private EditText etStartTime;
    private ImageAdapter imgAdapter = null;			// 声明图片资源对象
	private Gallery gallery = null;
	private EditText roomNameEditText;
	private String roomNameString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SysApplication.getInstance().addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.room_second_1);

		 
        etStartTime = (EditText) this.findViewById(R.id.room_create_2_date);       
        etStartTime.setOnTouchListener(this);    
        
        gallery = (Gallery) findViewById(R.id.gallery);
		imgAdapter = new ImageAdapter(this);
		gallery.setAdapter(imgAdapter); 					// 设置图片资源
		gallery.setGravity(Gravity.CENTER_HORIZONTAL);		// 设置水平居中显示
		gallery.setSelection(imgAdapter.imgs.length * 100);		// 设置起始图片显示位置（可以用来制作gallery循环显示效果）
		
		gallery.setOnItemClickListener(clickListener); 			// 设置点击图片的监听事件（需要用手点击才触发，滑动时不触发）
		gallery.setOnItemSelectedListener(selectedListener);		// 设置选中图片的监听事件（当图片滑到屏幕正中，则视为自动选中）
		gallery.setUnselectedAlpha(0.3f);					// 设置未选中图片的透明度
		gallery.setSpacing(40);					
		
		next = (ImageButton) findViewById(R.id.nextButton);
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {			
				Intent intent = new Intent();
				//把输入的房间名传递到下一个界面
				roomNameEditText = (EditText)findViewById(R.id.roomName_editText);
				roomNameString = roomNameEditText.getText().toString();
				String numString=((EditText)findViewById(R.id.numpergroup)).getText().toString();
				String date = ((EditText)findViewById(R.id.room_create_2_date)).getText().toString();
				if(roomNameString.equals("")||numString.equals("")||date.equals("")){
					new AlertDialog.Builder(Room_Second_1.this).setMessage("请填写完整").setPositiveButton("确定", null).create().show();
					return;
				}
				int numpergroup = Integer.valueOf(numString).intValue();
				intent.putExtra("roomName", roomNameString);
				intent.putExtra("numpergroup", numpergroup);
				intent.putExtra("date", date);
		        intent.setClass(Room_Second_1.this, Room_Second_1_2.class);
		        startActivity(intent);
		        //finish();
		        //下面代码是用Tab时候的代码，已经无作用
//		        View decorView = CreateGroup.createGroup.getLocalActivityManager().startActivity("Room_Second_1_2", intent).getDecorView();
//		        CreateGroup.createGroup.replaceContentView(decorView);
			}
		});
		
		backHomeImageButton = (ImageButton)findViewById(R.id.back_home);
		backHomeImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(Room_Second_1.this, HomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	
	//设置返回键返回主页
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
			Intent intent = new Intent();
			intent.setClass(Room_Second_1.this, HomeActivity.class);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
		
	@Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = View.inflate(this, R.layout.date_choose, null);
            final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
            final TimePicker timePicker = (android.widget.TimePicker) view.findViewById(R.id.time_picker);
            builder.setView(view);

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);

            timePicker.setIs24HourView(true);
            timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(Calendar.MINUTE);

            if (v.getId() == R.id.room_create_2_date) {
                final int inType = etStartTime.getInputType();
                etStartTime.setInputType(InputType.TYPE_NULL);
                etStartTime.onTouchEvent(event);
                etStartTime.setInputType(inType);
                etStartTime.setSelection(etStartTime.getText().length());
                
                builder.setTitle("选取起始时间");
                builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        StringBuffer sb = new StringBuffer();
                        sb.append(String.format("%d年%02d月%02d日", 
                                datePicker.getYear(), 
                                datePicker.getMonth() + 1,
                                datePicker.getDayOfMonth()));
                        sb.append("  ");
                        sb.append(timePicker.getCurrentHour())
                        .append(":").append(timePicker.getCurrentMinute());

                        etStartTime.setText(sb);
                        
                        dialog.cancel();
                    }
                });
                
            } 
            
            Dialog dialog = builder.create();
            dialog.show();
        }

        return true;
    }
    
    // 点击图片的监听事件
 	AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
 		@Override
 		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
 			//Toast.makeText(GalleryActivity.this, "点击图片 " + (position + 1), 100).show();
 		}
 	};
 	
 	// 选中图片的监听事件
 	AdapterView.OnItemSelectedListener selectedListener = new AdapterView.OnItemSelectedListener() {
 		@Override
 		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
 			//Toast.makeText(GalleryActivity.this, "选中图片 " + (position + 1), 20).show();
 		}

 		@Override
 		public void onNothingSelected(AdapterView<?> arg0) {
 			
 		}
 	};
	
}
