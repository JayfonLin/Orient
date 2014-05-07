package com.orient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class TeamInfoActivity extends Activity{
	GridView myteam_gv;
	ListView otherteams_lv;
	LinearLayout myteam_ll;
	Team myteam;
	TextView myteam_tv;
	EditText myteam_et;
	List<Team> otherTeams;
	ImageView edit_iv;
	boolean isEditTeamName;
	InputMethodManager imm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		setData();
		updataMyTeam();
		updateOtherTeam();
		//setGridViewHeight();
		//setListViewHeightBasedOnChildren(otherteams_lv);
		setOnClicks();
	}
	
	private void init(){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.teaminfo_layout);
		myteam_gv = (GridView) findViewById(R.id.myteam_gridView);
		myteam_ll = (LinearLayout) findViewById(R.id.myteam_linearLayout);
		myteam_ll.setVisibility(View.VISIBLE);
		otherteams_lv = (ListView) findViewById(R.id.other_teams_listView);
		myteam_tv = (TextView) myteam_ll.findViewById(R.id.myteam_name_tv);
		myteam_et = (EditText) myteam_ll.findViewById(R.id.myteam_name_et);
		edit_iv = (ImageView) findViewById(R.id.edit_teamName);
		otherTeams = new ArrayList<Team>();
		isEditTeamName = false;
		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
	}
	private void setData(){
		myteam = new Team("我的队伍名");
		
		for (int i = 0; i < 10; i++){
			int avatar = R.drawable.pic_1;
			TeamMemberInfo member = new TeamMemberInfo(avatar, "队员"+i);
			myteam.addMember(member);
		}
		for (int i = 0; i < 3; i++){
			Team team = new Team("队伍"+i);
			int avatar2 = R.drawable.pic_2;
			for (int j = 0; j < 5; j++){
				TeamMemberInfo member = new TeamMemberInfo(avatar2, "队员"+j);
				team.addMember(member);
			}
			otherTeams.add(team);
		}
	}
	
	private void updataMyTeam(){
		myteam_et.setText(myteam.getTeamName());
		ArrayList<HashMap<String, Object>> memItem = new ArrayList<HashMap<String, Object>>();
		ArrayList<TeamMemberInfo> myteam_mems = (ArrayList<TeamMemberInfo>) myteam.getTeamMemberList();
		for (int i = 0; i < myteam_mems.size(); i++){
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("MemImage", myteam_mems.get(i).getAvatar());
			map.put("Name", myteam_mems.get(i).getName());
			memItem.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(this, memItem, R.layout.team_member_item,
				new String[]{"MemImage", "Name"},
				new int[]{R.id.imageView1, R.id.textView1});
		myteam_gv.setAdapter(adapter);
	}
	
	private void updateOtherTeam(){
		otherteams_lv.setAdapter(new ListViewAdapter(otherTeams));
	}
	
	private void setOnClicks(){
		edit_iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!isEditTeamName){
					isEditTeamName = !isEditTeamName;
					myteam_tv.setVisibility(View.GONE);
					myteam_et.setVisibility(View.VISIBLE);
					myteam_et.setFocusable(true);
					myteam_et.setFocusableInTouchMode(true);
					myteam_et.requestFocus();
					myteam_et.setSelection(myteam_et.getText().length());
					imm = (InputMethodManager)myteam_et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

			        imm.showSoftInput(myteam_et, 0); //显示软键盘  
			        edit_iv.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_save));
				}
				else {
					isEditTeamName = !isEditTeamName;
					myteam_tv.setVisibility(View.VISIBLE);
					myteam_et.setVisibility(View.GONE);
					myteam_tv.setText(myteam_et.getText());
					imm.hideSoftInputFromWindow(edit_iv.getWindowToken(), 0);//隐藏软键盘 
			        edit_iv.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_edit));
				}
			}
		});
	}
	private void setListViewHeightBasedOnChildren(ListView listView){
		ListAdapter listAdapter = listView.getAdapter();   
        if (listAdapter == null) {  
            // pre-condition  
            return;  
        }  
  
        int totalHeight = 0;  
        for (int i = 0; i < listAdapter.getCount(); i++) {  
            View listItem = listAdapter.getView(i, null, listView);  
            listItem.measure(0, 0);  
            totalHeight += listItem.getMeasuredHeight();  
        }  
  
        ViewGroup.LayoutParams params = listView.getLayoutParams();  
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  
        listView.setLayoutParams(params);  
	}
	
	private class ListViewAdapter extends BaseAdapter{
		View[] itemViews;
		
		public ListViewAdapter(List<Team> teams) {
			super();
			itemViews = new View[teams.size()];
			for (int i = 0; i < itemViews.length; i++){
				itemViews[i] = makeItemView(teams.get(i).getTeamName(), teams.get(i).getTeamMemberList());
			}
		}
		public ListViewAdapter(){}

		@Override
		public int getCount() {
			return itemViews.length;
		}

		@Override
		public Object getItem(int arg0) {
			return itemViews[arg0];
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if (arg1 == null)  
                return itemViews[arg0];  
            return arg1; 
		}
		
		private View makeItemView(String pTeamName, List<TeamMemberInfo> pMembers){
			LayoutInflater inflater = getLayoutInflater();
			View itemView = inflater.inflate(R.layout.otherteam_listitem, null);
			TextView teamName_tv = (TextView) itemView.findViewById(R.id.teamName);
			teamName_tv.setText(pTeamName);
			GridView gv = (GridView) itemView.findViewById(R.id.gridView1);
			ArrayList<HashMap<String, Object>> memItem = new ArrayList<HashMap<String, Object>>();
			for (int i = 0; i < pMembers.size(); i++){
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("MemImage", pMembers.get(i).getAvatar());
				map.put("Name", pMembers.get(i).getName());
				memItem.add(map);
			}
			SimpleAdapter adapter = new SimpleAdapter(TeamInfoActivity.this, memItem, R.layout.team_member_item,
					new String[]{"MemImage", "Name"},
					new int[]{R.id.imageView1, R.id.textView1});
			gv.setAdapter(adapter);
			return itemView;
		}
	}
	
}