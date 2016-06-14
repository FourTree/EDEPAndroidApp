package com.hengbao.tsm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.simalliance.openmobileapi.SEService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity implements OnClickListener {

	//byte[] appletaid = new byte[] {(byte)0xA0,0x00,0x00,0x00,0x03,(byte)0x86,(byte)0x98,0x07,0x01 };
	//byte[] appletaid = new byte[] {(byte)0xA0,(byte)0x00,(byte)0x00,(byte)0x01,(byte)0x51,(byte)0x00,(byte)0x00 };
	//byte[] appletaid = new byte[] {(byte)0xD1,(byte)0x56,(byte)0x00,(byte)0x00,(byte)0x27,(byte)0x55,(byte)0x49,(byte)0x43,(byte)0x43};
	byte[] appletaid = new byte[] {0x31,0x50,0x41,0x59,0x2E,0x53,0x59,0x53,0x2E,0x44,0x44,0x46,0x30,0x31};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ExitApplication.getInstance().addActivity(this);
		System.out.println("ACC开始检验……");
		//计算app hash值
		PackageManager packageManager = getPackageManager();
	    AccessController accessController = new AccessController(packageManager);
	    String apphash = accessController.countAppHash(packageManager, getPackageName(), this);
	    System.out.println("ACC通过");
		//建立与卡交互的service
		GetSimInfo callback = new GetSimInfo();
		SEService seService = new SEService(this, callback);
		
/*		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}*/
	    DisplayMetrics displayMetrics = new DisplayMetrics();
	    ((Activity) this).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
	    int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
      	
		LinearLayout linearlayout1 = (LinearLayout) findViewById(R.id.LinearLayout1);
		LinearLayout linearlayout2 = (LinearLayout) findViewById(R.id.LinearLayout2);
		LinearLayout.LayoutParams params = new LinearLayout
				.LayoutParams((int) (displayWidth * 0.5f + 0.5f),(int) (displayWidth * 1.0f + 0.5f));
		linearlayout1.setLayoutParams(params);
		linearlayout2.setLayoutParams(params);
		
		Button tradeBtn = (Button) findViewById(R.id.trade);
		Button balanceBtn = (Button) findViewById(R.id.balance);
		Button recordBtn = (Button) findViewById(R.id.record);
		Button appinfoBtn = (Button) findViewById(R.id.appletinfo);
		tradeBtn.setOnClickListener(this);//充值交易
		tradeBtn.setTag(1);
		balanceBtn.setOnClickListener(this);//账户余额查询
		balanceBtn.setTag(2);
		recordBtn.setOnClickListener(this); //交易记录查询
		recordBtn.setTag(3);
		appinfoBtn.setOnClickListener(this); //账户管理
		appinfoBtn.setTag(4);
	}
	
	
	final int MENU_EXIT = Menu.FIRST;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		menu.clear();
		menu.add(0, MENU_EXIT, 0, "退出");
		return true;
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch(item.getItemId()){
			case MENU_EXIT:
				if((GetSimInfo.seService != null)&&(GetSimInfo.seService.isConnected()))
				{
					GetSimInfo.seService.shutdown();
				}
				ExitApplication.getInstance().exit();
				break;
			default:
				break;
		}
/*		if (id == R.id.action_settings) {
			return true;
		}*/
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 * fun: select menu
	 */
	public void onClick(View v){  
		byte[] msg ;
		CommandList.clearList();
		int tag = (Integer) v.getTag();  
        switch(tag){ 
        	case 1:  
        	Intent intent = new Intent();
	    	intent.putExtra("aid", appletaid);
    		intent.setClass(MainActivity.this, RechargeActivity.class);
    		MainActivity.this.startActivity(intent);
			break;
        	case 2:  
        		System.out.println("点击查询余额");
        		//查询账户余额
        		ShowDialog.creatDialog(0, "请等待", "正在查询账户信息...",null);
        		System.out.println("getbalacne dialog已显示");
        		Balance balance = new Balance();
        		System.out.println("对象balance已实例化");
        		balance.showbalance(appletaid);
        		System.out.println("balance已显示");
        		break;  
        	case 3:  
        		//查询交易记录
        		ShowDialog.creatDialog(0, "请等待", "正在查询账户信息...",null);
        		TradeRecord tradeRecord = new TradeRecord();
        		tradeRecord.getTradeRecord(appletaid);
				break;   
        	case 4:  
        		String version = null;
        		try { 
	        			PackageManager manager = this.getPackageManager(); 
	        			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0); 
	        			version = info.versionName;  
        			} catch (Exception e) { 
        				e.printStackTrace(); 
        			}		
        		ShowDialog.creatDialog(0, "应用信息", "适用于一般钱包的客户端测试，版本" + version +"。",null);
        		break; 
        	default :  
        		break;  
        }  
  
    } 
	 @Override  
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("提示");
			builder.setMessage("是否退出此程序？");
			builder.setPositiveButton("是",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int arg0) {
							finish();
						}
					}).setNegativeButton("否",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int arg0) {
						}
					});
			builder.show();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
	 @Override  
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ExitApplication.getInstance().addActivity(this);

	}				
}
