package com.funshion.autotest;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.funshion.autotest.FunConfig.ModuleConfig;
import com.funshion.autotest.factory.MtcFactory;
import com.funshion.autotest.ui.FunUiEthManager;
import com.funshion.autotest.ui.FunUiKeyPadManager;
import com.funshion.autotest.ui.FunUiManager;
import com.funshion.autotest.ui.FunUiPQManager;
import com.funshion.autotest.ui.FunUiParamManager;
import com.funshion.autotest.ui.FunUiPropertyInfoManager;
import com.funshion.autotest.ui.FunUiTvSourceManager;
import com.funshion.autotest.ui.FunUiUsbManager;
import com.funshion.autotest.ui.FunUiVideoWindowManager;
import com.funshion.autotest.ui.FunUiWifiManager;
import com.funshion.autotest.util.deviceUtils;
import com.funshion.autotest.util.portInfo;

public class MainActivity extends Activity {
	public static Context mContext = null;
	FunUiManager mFunUiManager = null;
	private static final String TAG = "chenhai";
    private static final int FLAG_DISABLE_HOME_KEY = 0x80000000;
	public static int mkeyFlag = 0;

	private MtcFactory mtcFactory = null;

	private int mPreScale = mtcFactory.SCALE_NONE;

	private LinearLayout mMainView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(FLAG_DISABLE_HOME_KEY, FLAG_DISABLE_HOME_KEY);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		Settings.System.putInt(getContentResolver(), "home_hot_key_disable", 0);
		
		mContext = this;
		mFunUiManager = FunUiManager.getInstance();

		mtcFactory = new MtcFactory(mContext);
		uiOperator();
	}

	private void deviceInfo(){
		portInfo _portInfo = null;
		deviceUtils _devInfo = new deviceUtils(mContext);

	}

	private void uiOperator() {
		deviceInfo();
		// get module config for ui custom
		ModuleConfig moduleConfig = FunConfig.getModuleConfig();
		// get module config for ui custom

		mMainView = (LinearLayout) findViewById(R.id.ll_main_layout);

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
		if (moduleConfig.mModuleTestLocalVideo) {
			mFunUiManager.addUiModule(FunUiVideoWindowManager.class.getSimpleName(),
					new FunUiVideoWindowManager(mContext));
			mFunUiManager.onCreateUiModuleByName(
					FunUiVideoWindowManager.class.getSimpleName(), (View) leftLayout);
		}
		/* signal test*/
		if (moduleConfig.mModuleTestInputSignal) {
			mFunUiManager.addUiModule(FunUiTvSourceManager.class.getSimpleName(),
					new FunUiTvSourceManager(mContext));
			mFunUiManager.onCreateUiModuleByName(
					FunUiTvSourceManager.class.getSimpleName(), (View) rightLayout);
		}
		/* info item*/
		if (moduleConfig.mModuleInfoParams) {
			mFunUiManager.addUiModule(FunUiParamManager.class.getSimpleName(),
					new FunUiParamManager(mContext));
			mFunUiManager.onCreateUiModuleByName(
					FunUiParamManager.class.getSimpleName(), (View) rightLayout);
		}

		if (moduleConfig.mModuleInfoProperty) {
			mFunUiManager.addUiModule(FunUiPropertyInfoManager.class.getSimpleName(),
					new FunUiPropertyInfoManager(mContext));
			mFunUiManager.onCreateUiModuleByName(
					FunUiPropertyInfoManager.class.getSimpleName(), (View) rightLayout);
		}


		if(moduleConfig.mModuleInfoPQSpinner){
			mFunUiManager.addUiModule(FunUiPQManager.class.getSimpleName(),
					new FunUiPQManager(mContext));
			mFunUiManager.onCreateUiModuleByName(
					FunUiPQManager.class.getSimpleName(), rightLayout);
		}

		/* eth test */
		if(moduleConfig.mModuleTestWireLink){
			mFunUiManager.addUiModule(FunUiEthManager.class.getSimpleName(),
					new FunUiEthManager(mContext));
			mFunUiManager.onCreateUiModuleByName(
					FunUiEthManager.class.getSimpleName(), leftLayout);
		}

		if (moduleConfig.mModuleTestWifiLink){
			mFunUiManager.addUiModule(FunUiWifiManager.class.getSimpleName(),
					new FunUiWifiManager(mContext));
			mFunUiManager.onCreateUiModuleByName(
					FunUiWifiManager.class.getSimpleName(), leftLayout);
		}

 		if (moduleConfig.mModuleTestUsbDevice){
			mFunUiManager.addUiModule(FunUiUsbManager.class.getSimpleName(),
					new FunUiUsbManager(mContext));
			mFunUiManager.onCreateUiModuleByName(
					FunUiUsbManager.class.getSimpleName(), leftLayout);
		}

		if (moduleConfig.mModuleTestKeyPad){
			mFunUiManager.addUiModule(FunUiKeyPadManager.class.getSimpleName(),
					new FunUiKeyPadManager(mContext));
			mFunUiManager.onCreateUiModuleByName(
					FunUiKeyPadManager.class.getSimpleName(), leftLayout);
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
//		mFunUiManager.onResumeModuleByName(FunUiVideoWindowManager.class.getSimpleName());
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "chenhai test!!!!!!!!!!!");
		mFunUiManager.onDestroyAllUiModule();
		super.onDestroy();
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		String KEYPAD_IN_DEVICE = deviceUtils.KEYPAD_DEV_NAME;
		boolean isKeypad = false;

		if(KEYPAD_IN_DEVICE.equalsIgnoreCase(event.getDevice().getName())){
			isKeypad = true;
		}
		else{
			isKeypad = false;
		}
		Log.d(TAG, "chenhai test  keypad keycode = "+ keyCode + "isKeypad = " + isKeypad);

		if (isKeypad){
			switch (keyCode){
				case KeyEvent.KEYCODE_VOLUME_DOWN:
					mkeyFlag |= deviceUtils.KEYPAD_DOWN_FLAG;
					break;
				case KeyEvent.KEYCODE_VOLUME_UP:
					mkeyFlag |= deviceUtils.KEYPAD_UP_FLAG;
					break;
				case KeyEvent.KEYCODE_CHANNEL_DOWN:
					mkeyFlag |= deviceUtils.KEYPAD_LEFT_FLAG;
					break;
				case KeyEvent.KEYCODE_CHANNEL_UP:
					mkeyFlag |= deviceUtils.KEYPAD_RIGHT_FLAG;
					break;
				case 255://KeyEvent.KEYCODE_POWER:
					mkeyFlag |= deviceUtils.KEYPAD_POWER_FLAG;
					break;
				case KeyEvent.KEYCODE_MENU:
					mkeyFlag |= deviceUtils.KEYPAD_MENU_FLAG;
					break;
				case KeyEvent.KEYCODE_SOFT_RIGHT:
					mkeyFlag |= deviceUtils.KEYPAD_MTV_FLAG;
					break;
				default:
					break;
			}
			Intent intent = new Intent(FunUiKeyPadManager.FUN_KEYPAD_ACTION);
			intent.putExtra(FunUiKeyPadManager.FUN_KEYPAD_ACTION, mkeyFlag);
			sendBroadcast(intent);
		}

		switch (keyCode){
			case KeyEvent.KEYCODE_BACK:
				String factoryMode = SystemProperties.get("sys.autotest.flag",
						"false");
				if ("true".equals(factoryMode)) {
					return true;
				}
				mtcFactory.fullScale = mtcFactory.SCALE_SMALL;
				break;
			case KeyEvent.KEYCODE_HOME:
				break;
			case KeyEvent.KEYCODE_MENU:
				mFunUiManager.onRefreshAllUiModule();
				break;

			default:
				break;
		}

		mtcFactory.factoryTestKey(keyCode);

		if (mPreScale != mtcFactory.fullScale){
			Message tmpMsg = new Message();
			tmpMsg.what = mtcFactory.fullScale;
			scaleWinHandler.sendMessageDelayed(tmpMsg, 200);
			mPreScale = mtcFactory.fullScale;
		}

		return super.onKeyDown(keyCode, event);
//		return true;
	}

	private Handler scaleWinHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			FunUiVideoWindowManager _FunUiVideoWindowManager =
					(FunUiVideoWindowManager) mFunUiManager.getLogicModuleByModule(FunUiVideoWindowManager.class.getSimpleName());
			switch (msg.what) {
				case 1://mtcFactory.SCALE_FULL:
//					mMainView.setVisibility(View.INVISIBLE);
					_FunUiVideoWindowManager.setFullscale();
					break;
				case 2://mtcFactory.SCALE_SMALL:

//					handlertv.postDelayed(pip_thread, 500); // delay to show tv
					// window, wait
					// launcher UI.
//					mMainView.setBackgroundColor(Color.TRANSPARENT);
//					mMainView.setVisibility(View.VISIBLE);
					_FunUiVideoWindowManager.setVideoRectangle();
					break;
			}
		}


	};


}

