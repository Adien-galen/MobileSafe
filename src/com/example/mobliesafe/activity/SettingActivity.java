package com.example.mobliesafe.activity;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.mobliesafe.R;
import com.example.mobliesafe.view.SettingItemView;

/**
 * 
 * @author 桂林
 *
 */
public class SettingActivity extends Activity {
	private SharedPreferences mPref;
	private SettingItemView sivUpdate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		mPref=getSharedPreferences("config", MODE_PRIVATE);
		
		sivUpdate = (SettingItemView)findViewById(R.id.siv_update);
		sivUpdate.setTitle("自动更新设置");
		boolean autoUpdate = mPref.getBoolean("auto_update", true);
		
		if(autoUpdate){
			sivUpdate.setChecked(true);
		}else{
			sivUpdate.setChecked(false);
		}
		
		sivUpdate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//判断当前的勾选状态
				if(sivUpdate.isChecked()){
					sivUpdate.setChecked(false);
					// sivUpdate.setDesc("自动更新已关闭");
					// 更新sp
					mPref.edit().putBoolean("auto_update", false).commit();
				}else{
					sivUpdate.setChecked(true);
					// sivUpdate.setDesc("自动更新已开启");
					// 更新sp
					mPref.edit().putBoolean("auto_update", true).commit();
				}
			}
		});
	}
}
