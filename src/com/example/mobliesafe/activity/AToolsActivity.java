package com.example.mobliesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mobliesafe.R;

/**
 * 高级工具
 * @author 桂林
 *
 */
public class AToolsActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools);
	}
	
	/**
	 * 归属地查询
	 * @param view
	 */
	
	public void numberAddressQuery(View view){
		startActivity(new Intent(this,AddressActivity.class));
	}
	
}
