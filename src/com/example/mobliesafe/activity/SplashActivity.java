package com.example.mobliesafe.activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobliesafe.R;
import com.example.mobliesafe.utils.StreamUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;


public class SplashActivity extends Activity {
	
	protected static final int CODE_UPDATE_DIALOG = 0;
	protected static final int CODE_URL_ERROR = 1;
	protected static final int CODE_NET_ERROR = 2;
	protected static final int CODE_JSON_ERROR = 3;
	protected static final int CODE_ENTER_HOME = 4;// ������ҳ��
	
	private TextView tvVersion;
	private TextView tvProgress;
	
	// ���������ص���Ϣ
		private String mVersionName;// �汾��
		private int mVersionCode;// �汾��
		private String mDesc;// �汾����
		private String mDownloadUrl;// ���ص�ַ
		
		
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg){
			switch(msg.what){
			case CODE_UPDATE_DIALOG:
				showUpdateDailog();
				break;
			case CODE_URL_ERROR:
				Toast.makeText(SplashActivity.this, "url����", Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			case CODE_NET_ERROR:
				Toast.makeText(SplashActivity.this, "�������", Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			case CODE_JSON_ERROR:
				Toast.makeText(SplashActivity.this, "���ݽ�������",
						Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			case CODE_ENTER_HOME:
				enterHome();
				break;
			
			default:
				break;
			}
		};
	};
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tvVersion = (TextView) findViewById(R.id.tv_version);
        tvProgress = (TextView) findViewById(R.id.tv_progress); //Ĭ������
        tvVersion.setText("�汾�ţ�"+getVersionName());
        checkVerson();
    }

    
    
	
	/**
	 * ��ȡ�汾����
	 * 
	 * @return
	 */
	private String getVersionName() {
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			int versionCode = packageInfo.versionCode;
			String versionName = packageInfo.versionName;
			System.out.println("verCode=:"+versionCode+"   verName="+versionName);
			return versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}

	/**
	 * ��ȡ����app�İ汾��
	 * 
	 * @return
	 */
	private int getVersionCode(){
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);//��ȡ������Ϣ
			int versionCode = packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// û���ҵ�������ʱ����ߴ��쳣
			e.printStackTrace();
		}
		
		return -1;
	}
	
	/**
	 * �ӷ�������ȡ�汾��Ϣ����У��
	 */
	private void checkVerson() {
		final long startTime = System.currentTimeMillis();
		//�������߳��첽��������
		
		new Thread(){

			@Override
			public void run() {
				Message msg = Message.obtain();
				HttpURLConnection conn = null;
				try {
					// ������ַ��localhost, ���������ģ�������ر����ĵ�ַʱ,������ip(192.168.1.101)���滻
					URL url = new URL("http://192.168.1.101:8080/update.json");
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(5000);
					conn.connect();
					
					int responseCode = conn.getResponseCode();// ��ȡ��Ӧ��
					if(responseCode == 200){
						InputStream inputStream =conn.getInputStream();
						String result = StreamUtils.readFromStream(inputStream);
						// System.out.println("���緵��:" + result);

						// ����json
						JSONObject jo = new JSONObject(result);
						mVersionName = jo.getString("versionName");
						mVersionCode = jo.getInt("versionCode");
						mDesc = jo.getString("description");
						mDownloadUrl = jo.getString("downloadUrl");
						// System.out.println("�汾����:" + mDesc);
						
						if(mVersionCode>getVersionCode()) {//�ж��Ƿ��и���
							// ��������VersionCode���ڱ��ص�VersionCode
							// ˵���и���, ���������Ի���
							msg.what=CODE_UPDATE_DIALOG;
						}else{
							msg.what=CODE_ENTER_HOME;
						}
					}
				} catch (MalformedURLException e) {
					// url������쳣
					msg.what = CODE_URL_ERROR;
					e.printStackTrace();
				} catch (IOException e1) {
					// ��������쳣
					msg.what = CODE_NET_ERROR;
					e1.printStackTrace();
				} catch (JSONException e2) {
					// json����ʧ��
					msg.what = CODE_JSON_ERROR;
					e2.printStackTrace();
				}finally{
					long endTime = System.currentTimeMillis();
					long timeUsed = endTime-startTime; // �������绨�ѵ�ʱ��
					if(timeUsed<2000){
						// ǿ������һ��ʱ��,��֤����ҳչʾ2����
						try {
							Thread.sleep(2000-timeUsed);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					mHandler.sendMessage(msg);
					if(conn!=null){
						conn.disconnect();
					}
				}
			};
		}.start();
	}
	

	/**
	 * �����Ի���
	 */
	protected void showUpdateDailog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("���°汾"+mVersionName);
		builder.setMessage(mDesc);
		// builder.setCancelable(false);//�����û�ȡ���Ի���, �û�����̫��,������Ҫ��
		builder.setPositiveButton("��������", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				System.out.println("��������");
				download();
			}
		});
		
		builder.setNegativeButton("�Ժ���˵", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				enterHome();
			}
		});
		
		// ����ȡ���ļ���, �û�������ؼ�ʱ�ᴥ��
		builder.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				enterHome();
			}
		});
		
		builder.show();
	}


	/**
	 * ����apk�ļ�
	 */
	protected void download() {
		if(Environment.getExternalStorageDirectory().equals(Environment.MEDIA_MOUNTED)){
			tvProgress.setVisibility(View.VISIBLE);// ��ʾ����
			
			String target = Environment.getExternalStorageDirectory()+"/update.apk";
			
			//XUtils
			HttpUtils utils = new HttpUtils();
			utils.download(mDownloadUrl, target, new RequestCallBack<File>() {
				
				//�����ļ��Ľ���
				@Override
				public void onLoading(long total, long current,
						boolean isUploading) {
					super.onLoading(total, current, isUploading);
					System.out.println("���ؽ���"+current + "/" +total);
					tvProgress.setText("���ؽ���"+current*100/total +"%");
				}
				
				
				// ���سɹ�
				@Override
				public void onSuccess(ResponseInfo<File> arg0) {
					System.out.println("���سɹ�");
					// ��ת��ϵͳ����ҳ��
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.setDataAndType(Uri.fromFile(arg0.result), "application/vnd.android.package-archive");
					
					startActivityForResult(intent, 0);// ����û�ȡ����װ�Ļ�,
					// �᷵�ؽ��,�ص�����onActivityResult
				}
				
				// ����ʧ��
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					Toast.makeText(SplashActivity.this, "����ʧ��!",
							Toast.LENGTH_SHORT).show();
				}
			});
		}else{
			Toast.makeText(SplashActivity.this, "û���ҵ�sdcard!", Toast.LENGTH_SHORT).show();
		}
	}

	// ����û�ȡ����װ�Ļ�,�ص��˷���
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		enterHome();
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * ������ҳ��
	 */
	protected void enterHome() {
		Intent intent = new Intent(this,HomeActivity.class);
		startActivity(intent);
		finish();
	}
    
}
