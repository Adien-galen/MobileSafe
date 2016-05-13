package com.example.mobliesafe.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

/**
 * 获取经纬度坐标的service
 * @author hz15071507
 *
 */
public class LocationService extends Service {

	private SharedPreferences mPref;
	private LocationManager lm;
	private MyLocationListener listener;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		Criteria criteria = new Criteria();
		criteria.setCostAllowed(true);//
		criteria.setAccuracy(Criteria.ACCURACY_FINE);// 是否允许付费,比如使用3g网络定位
		String bestProvider = lm.getBestProvider(criteria, true);// 获取最佳位置提供者
		
		listener = new MyLocationListener();
		lm.requestLocationUpdates(bestProvider, 0, 0, listener);// 参1表示位置提供者,参2表示最短更新时间,参3表示最短更新距离
	}
	
	class MyLocationListener implements LocationListener{
		
		// 位置发生变化
		@Override
		public void onLocationChanged(Location location) {
			System.out.println("get location!");
			
			// 将获取的经纬度保存在sp中
			mPref.edit().putString("location", "j:"+location.getLongitude()+"; w:"+location.getLatitude()).commit();
			stopSelf();//停掉service
		}
		
		// 位置提供者状态发生变化
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			System.out.println("onStatusChanged");
		}
		
		// 用户打开gps
		@Override
		public void onProviderEnabled(String provider) {
			System.out.println("onProviderEnabled");
		}

		// 用户关闭gps
		@Override
		public void onProviderDisabled(String provider) {
			System.out.println("onProviderDisabled");
		}
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		lm.removeUpdates(listener); // 当activity销毁时,停止更新位置, 节省电量
	}
}
