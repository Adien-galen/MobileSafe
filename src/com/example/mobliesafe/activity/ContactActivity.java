package com.example.mobliesafe.activity;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mobliesafe.R;

public class ContactActivity extends Activity {
	ListView contactsView;
	ArrayAdapter<String> adapter;
	List<String> contactsList = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		contactsView = (ListView) findViewById(R.id.contacts_view);
		adapter =new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contactsList);
		contactsView.setAdapter(adapter);
		readContacts();
	}
	private void readContacts() {
		Cursor cursor = null;
		try {
			cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
			while(cursor.moveToNext()){
				//获取联系人姓名
				String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
				//获取联系人手机号
				String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				
				contactsList.add(displayName+"\n"+number);
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(cursor!=null){
				cursor.close();
			}
		}
	}
}
