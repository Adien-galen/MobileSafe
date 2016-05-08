package com.example.mobliesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.mobliesafe.R;
/**
 * 手机防盗页面
 * @author 桂林
 *
 */
public class LostFindActivity extends Activity {
	private SharedPreferences mPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mPrefs = getSharedPreferences("config", MODE_PRIVATE);
		
		boolean configed = mPrefs.getBoolean("configed", false);// 判断是否进入过设置向导
		if (configed) {
			setContentView(R.layout.activity_lost_find);
		}else{
			// 跳转设置向导页
			startActivity(new Intent(LostFindActivity.this,Setup1Activity.class));
			finish();
		}
	}
	
	/*
	 * 重新进入设置向导
	 * 
	 * @param view
	 */
	
	public void reEnter(View view){
		startActivity(new Intent(LostFindActivity.this,Setup1Activity.class));
		finish();
	}
}
