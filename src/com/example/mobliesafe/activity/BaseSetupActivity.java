package com.example.mobliesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

import com.example.mobliesafe.utils.ToastUtils;

/**
 * 设置引导页的基类, 不需要在清单文件中注册, 因为不需要界面展示
 * @author 桂林
 *
 */
public abstract class BaseSetupActivity extends Activity {
	private GestureDetector mDetector;
	public SharedPreferences mpref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mpref = getSharedPreferences("config", MODE_PRIVATE);
		
		// 手势识别器
		mDetector = new GestureDetector(this, new SimpleOnGestureListener(){
			
			/**
			 * 监听手势滑动事件 e1表示滑动的起点,e2表示滑动终点 velocityX表示水平速度 velocityY表示垂直速度
			 */
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				
				// 判断纵向滑动幅度是否过大, 过大的话不允许切换界面
				if(Math.abs(e2.getRawY()-e1.getRawY())>100){
					ToastUtils.showToast(BaseSetupActivity.this, "不能这样划哦!");
					return true;
				}
				
				// 判断滑动是否过慢
				if(Math.abs(velocityX)<100){
					ToastUtils.showToast(BaseSetupActivity.this, "滑动的太慢了!");
					return true;
				}
				
				//向右划，显示上一页
				if(e2.getRawX()-e1.getRawX()>200){
					showPreviousPage();
					return true;
				}
				
				//向左划，显示下一页
				if(e1.getRawX()-e2.getRawX()>200){
					showNextPage();
					return true;
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		});
	}
	
	/**
	 * 展示下一页, 子类必须实现
	 */
	public abstract void showNextPage() ;

	/**
	 * 展示上一页, 子类必须实现
	 */
	public abstract void showPreviousPage() ;

	// 点击下一页按钮
	public void next(View view){
		showNextPage();
	}	
	
	// 点击上一页按钮
	public void previous(View view){
		showPreviousPage();
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mDetector.onTouchEvent(event);// 委托手势识别器处理触摸事件
		return super.onTouchEvent(event);
	}
}
