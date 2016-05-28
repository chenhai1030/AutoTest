package com.funshion.autotest;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemProperties;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.funshion.autotest.ui.FunUiEthManager;
import com.funshion.autotest.ui.FunUiManager;
import com.funshion.autotest.ui.FunUiParamManager;
import com.funshion.autotest.ui.FunUiPropertyInfoManager;
import com.funshion.autotest.ui.FunUiTvSourceManager;
import com.funshion.autotest.ui.FunUiUsbManager;
import com.funshion.autotest.ui.FunUiVideoWindowManager;
import com.funshion.autotest.ui.FunUiWifiManager;

public class MainActivity extends Activity {
	public static Context mContext = null;
	FunUiManager mFunUiManager = null;
	private static final String TAG = "FunUiEthManager";
    private static final int FLAG_DISABLE_HOME_KEY = 0x80000000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(FLAG_DISABLE_HOME_KEY, FLAG_DISABLE_HOME_KEY);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mContext = this;
		mFunUiManager = FunUiManager.getInstance();
		
		uiOperator();
	}
	
	private void uiOperator() {
		// get module config for ui custom
		// ModuleConfig moduleConfig = FunConfig.getModuleConfig();
		
		// ui operator
//		TextView _textView = (TextView) findViewById(R.id.tv_sorftware_version);
//		_textView.setText("00.00.01");
		
		// get right/left layout
		LinearLayout rightLayout = (LinearLayout)findViewById(R.id.rigth_layout); 
		LinearLayout leftLayout = (LinearLayout)findViewById(R.id.left_layout);
		
		// ***********set left layout widge
		/*
		if (moduleConfig.mModuleTestWifiLink) {
			mSkyUiManager.addUiModule(SkyUiWiFiManager.class.getSimpleName(),
					new SkyUiWiFiManager(mContext));
			mSkyUiManager.onCreateUiModuleByName(
					SkyUiWiFiManager.class.getSimpleName(), (View) leftLayout);
		}*/

		/* video */
		mFunUiManager.addUiModule(FunUiVideoWindowManager.class.getSimpleName(),
				new FunUiVideoWindowManager(mContext));
		mFunUiManager.onCreateUiModuleByName(
				FunUiVideoWindowManager.class.getSimpleName(), (View)leftLayout);

		/* signal test*/
		mFunUiManager.addUiModule(FunUiTvSourceManager.class.getSimpleName(),
				new FunUiTvSourceManager(mContext));
		mFunUiManager.onCreateUiModuleByName(
				FunUiTvSourceManager.class.getSimpleName(),(View)rightLayout);


		mFunUiManager.addUiModule(FunUiParamManager.class.getSimpleName(),
				new FunUiParamManager(mContext));
		mFunUiManager.onCreateUiModuleByName(
				FunUiParamManager.class.getSimpleName(), (View)rightLayout);

		/* info item*/
		mFunUiManager.addUiModule(FunUiPropertyInfoManager.class.getSimpleName(),
				new FunUiPropertyInfoManager(mContext));
		mFunUiManager.onCreateUiModuleByName(
				FunUiPropertyInfoManager.class.getSimpleName(), (View)rightLayout);

		if(true){
			mFunUiManager.addUiModule(FunUiEthManager.class.getSimpleName(),
					new FunUiEthManager(mContext));
//			Log.d("chenhai test", "name :"+FunUiEthManager.class.getSimpleName());
			mFunUiManager.onCreateUiModuleByName(
					FunUiEthManager.class.getSimpleName(), leftLayout);
		}
		if (true){
			mFunUiManager.addUiModule(FunUiWifiManager.class.getSimpleName(),
					new FunUiWifiManager(mContext));
			mFunUiManager.onCreateUiModuleByName(
					FunUiWifiManager.class.getSimpleName(), leftLayout);
		}
 		if (true){
			mFunUiManager.addUiModule(FunUiUsbManager.class.getSimpleName(),
					new FunUiUsbManager(mContext));
			mFunUiManager.onCreateUiModuleByName(
					FunUiUsbManager.class.getSimpleName(), leftLayout);
		}

		// show all UI
		mFunUiManager.onShowAllUiModule();
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}

//	@Override
	protected void onResume() {
		mFunUiManager.onResumeModuleByName(FunUiVideoWindowManager.class.getSimpleName());
		super.onResume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode){
			case KeyEvent.KEYCODE_BACK:
				String factoryMode = SystemProperties.get("sys.autotest.flag",
						"false");
				if (factoryMode.equals("true")) {
					return true;
				}
				break;
			case KeyEvent.KEYCODE_HOME:
				break;
			case KeyEvent.KEYCODE_MENU:
				mFunUiManager.onRefreshAllUiModule();
				break;

			default:
				break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
