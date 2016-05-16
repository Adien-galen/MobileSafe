package com.example.mobliesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mobliesafe.R;

/**
 * 修改归属地显示位置
 * @author hz15071507
 *
 */
public class DragViewActivity extends Activity {
	private TextView tvTop;
	private SharedPreferences mPref;
	private TextView tvButtom;
	private ImageView ivDrag;
	
	private int startX;
	private int startY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_drag_view);
		
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		
		tvTop = (TextView) findViewById(R.id.tv_top);
		tvButtom = (TextView) findViewById(R.id.tv_bottom);
		ivDrag = (ImageView) findViewById(R.id.iv_drag);
		
		int lastX = mPref.getInt("lastX", 0);
		int lastY = mPref.getInt("lastY", 0);
		
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivDrag.getLayoutParams();//获取布局对象
		
		layoutParams.leftMargin=lastX; // 设置左边距
		layoutParams.topMargin = lastY ; // 设置top边距
		
		ivDrag.setLayoutParams(layoutParams); // 重新设置位置
		
		// 设置触摸监听
		ivDrag.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:
					// 初始化起点坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();
					
					// 计算移动偏移量
					int dx = endX -startX;
					int dy = endY -startY	;
					
					// 更新左上右下距离
					int l = ivDrag.getLeft()+dx;
					int r = ivDrag.getRight()+dx;
					
					int t = ivDrag.getTop()+dy;
					int b = ivDrag.getBottom()+dy;
					
					// 更新界面
					ivDrag.layout(l, t, r, b);
					
					// 重新初始化起点坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					//记录坐标点
					Editor edit = mPref.edit();
					edit.putInt("lastX",ivDrag.getLeft());
					edit.putInt("lastY",ivDrag.getTop());
					edit.commit();
					break;
				
				default:
					break;
				}
				return true;
			}
		});
	}
}
