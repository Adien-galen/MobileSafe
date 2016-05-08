package com.example.mobliesafe.activity;

import com.example.mobliesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 *  第3个设置向导页
 * @author 桂林
 *
 */
public class Setup3Activity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
	}
	
	//下一页
	public void next(View view){
		startActivity(new Intent(this,Setup4Activity.class));
		finish();
		
		//两个界面切换的动画
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);//进入动画和退出动画
	
	}
	
	//上一页
	public void previous(View view) {
		startActivity(new Intent(this,Setup2Activity.class));
		finish();
		
		//两个界面切换的动画
		overridePendingTransition(R.anim.tran_previous_in, R.anim.tran_previous_out);//进入动画和退出动画
	
	}
}