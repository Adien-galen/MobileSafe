package com.example.mobliesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.mobliesafe.R;
import com.example.mobliesafe.utils.ToastUtils;
import com.example.mobliesafe.view.SettingItemView;

/**
 *  第2个设置向导页
 * @author 桂林
 *
 */
public class Setup2Activity extends BaseSetupActivity {
	private SettingItemView sivSim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		
		sivSim = (SettingItemView) findViewById(R.id.siv_sim);
		
		String sim = mpref.getString("sim", null);
		if(!TextUtils.isEmpty(sim)){
			sivSim.setChecked(true);
		}else{
			sivSim.setChecked(false);
		}
		
		sivSim.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(sivSim.isChecked()){
					sivSim.setChecked(false);
					mpref.edit().remove("sim").commit();// 删除已绑定的sim卡
				}else{
					TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					String simSerialNumber = tm.getSimSerialNumber();// 获取sim卡序列号
					System.out.println("sim卡序列号:" + simSerialNumber);
					
					mpref.edit().putString("sim", simSerialNumber);
				}
			}
		});
	}
	
	@Override
	public void showNextPage() {
		// 如果sim卡没有绑定,就不允许进入下一个页面
		String sim = mpref.getString("sim", null);
		if(TextUtils.isEmpty(sim)){
			ToastUtils.showToast(this, "必须绑定sim卡!");
			return ;
		}
		
		startActivity(new Intent(this,Setup3Activity.class));
		finish();
		
		//两个界面切换的动画
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);//进入动画和退出动画
	
	}

	@Override
	public void showPreviousPage() {
		startActivity(new Intent(this,Setup1Activity.class));
		finish();
		
		//两个界面切换的动画
		overridePendingTransition(R.anim.tran_previous_in, R.anim.tran_previous_out);//进入动画和退出动画
		
	}
}
