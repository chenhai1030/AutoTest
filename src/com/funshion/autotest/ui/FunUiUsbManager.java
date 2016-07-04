package com.funshion.autotest.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.funshion.autotest.R;
import com.funshion.autotest.logic.FunLogicStorageManager;

public class FunUiUsbManager implements FunUiBase {
	private Context mContext = null;
	private View mView = null;
	private int mCheckResult = -1;
	private StorageManager mStorageManager = null;
	private FunLogicStorageManager mLogicStorageManager = null;

	private TextView mUsbTestNameView = null;
	private TextView mUsbTestResultView = null;

	private static final int FUNTV_USB_MOUNTED = 1;

    private static final String TAG = "FunUiUsbManager";

	public FunUiUsbManager(Context context) {
		mContext = context;
	}

	@Override
	public boolean OnCreate(View view) {
		mView = view;
		
		mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
		
		// TODO Auto-generated method stub
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_EJECT);
        filter.addAction(Intent.ACTION_MEDIA_REMOVED);
        filter.addDataScheme("file");
        mContext.registerReceiver(mUsbReceive,filter);
        
        mLogicStorageManager = new FunLogicStorageManager(mContext);
        mCheckResult = mLogicStorageManager.getUsbMountedNum();

		createView();
		return false;
	}

	public void createView() {
		LinearLayout _LinearLayout = (LinearLayout) mView;
		View _view = View.inflate(mContext, R.layout.test_item_bar, null);

		mUsbTestNameView = (TextView) _view.findViewById(R.id.tv_item_bar_name);
		mUsbTestResultView = (TextView) _view.findViewById(R.id.tv_item_bar_value);

		_LinearLayout.addView(_view);
	}

	private final BroadcastReceiver mUsbReceive = new BroadcastReceiver() {
		//private static final String INTERAL_STORAGE = Environment.getExternalStorageDirectory().getAbsolutePath();
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (action.equals("android.intent.action.BOOT_COMPLETED")) {
				Log.d(TAG,"*********** isBootcompletedBroadcast");
			}

			if (action.equals(Intent.ACTION_MEDIA_MOUNTED)
					||action.equals(Intent.ACTION_MEDIA_EJECT)
					||action.equals(Intent.ACTION_MEDIA_UNMOUNTED)) {
				mRefreshHandler.sendEmptyMessage(1);
				mRefreshHandler.sendEmptyMessageDelayed(2, 1000);
			//	startUsbCommand(context, intent);
			}
			return;
		}
	};

    private Handler mRefreshHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
			case 1:
				mCheckResult = 0;
				OnShow();
				break;
			case 2:
				mCheckResult = mLogicStorageManager.getUsbMountedNum();
				OnShow();
			default:
				break;
			}

            super.handleMessage(msg);
        }
    };

	@Override
	public boolean OnShow() {
		// TODO Auto-generated method stub
		mUsbTestNameView.setText(R.string.str_usb_test_name);

//		Log.i("chenhai test", "mCheckResult: " + mCheckResult);
		if (mCheckResult > 0) {
			String showText = mContext.getString(R.string.str_usb_test_success, mCheckResult);
			mUsbTestResultView.setText(showText);
			mUsbTestResultView.setBackgroundColor(Color.parseColor("#ADFF2F"));

		} else {
			mUsbTestResultView.setText(R.string.str_usb_test_fail);
			mUsbTestResultView.setBackgroundColor(Color.parseColor("#A52A2A"));
		}

		return false;
	}


	@Override
	public boolean OnRefresh() {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				mRefreshHandler.sendEmptyMessage(1);
				mRefreshHandler.sendEmptyMessageDelayed(2, 1000);
			}
		}.start();
		return true;
	}

	@Override
	public boolean OnDestroy() {
		// TODO Auto-generated method stub
		mContext.unregisterReceiver(mUsbReceive);
		return false;
	}

	@Override
	public void OnPause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnResume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void SetTestSuccess() {
		// TODO Auto-generated method stub
	}
}
