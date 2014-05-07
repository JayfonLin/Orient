package com.test;

import java.util.ArrayList;
import java.util.HashMap;

import com.orient.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;

public class TestTeamActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myteam_listitem);
		GridView gv = (GridView) findViewById(R.id.gridView1);
		
		ArrayList<HashMap<String, Object>> memItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 10; i++){
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("MemImage", android.R.drawable.sym_def_app_icon);
			map.put("Name", "NO."+i);
			memItem.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(this, memItem, R.layout.team_member_item,
				new String[]{"MemImage", "Name"},
				new int[]{R.id.imageView1, R.id.textView1});
		gv.setAdapter(adapter);
		gv.setOnItemClickListener(new ItemClickListener());
	}
	class ItemClickListener implements OnItemClickListener
	{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			HashMap<String, Object> item = (HashMap<String, Object>)arg0.getItemAtPosition(arg2);
			setTitle((String)item.get("Name"));
		}
		
	}
}
