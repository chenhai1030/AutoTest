package com.funshion.autotest.ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.funshion.autotest.FunConfig;
import com.funshion.autotest.R;

import static com.android.internal.os.SamplingProfilerIntegration.start;

/*
 * Created by chenhai on 2/10/17.
 */

public class FunUiBTManager implements FunUiBase {
    private String TAG = "FunUiBTManager";
    private Context mContext = null;
    private TextView mTestNameView = null;
    private TextView mTestStatusView = null;
    private int mBTTestStatus = 0;
    private boolean isBTstarted = false;

    private static final int BT_TEST_SUCCESS = 0x1100;
    private static final int BT_TEST_FAIL = 0x1101;
    private static final int BT_TEST_LOW = 0x1102;
    private static final int BT_FAIL_VALUE = -100;
    private int mBTLevelValue = -100;

    private BluetoothAdapter mBluetoothAdapter;

    private Handler mHandler = new Handler();

    private FunConfig.ModuleConfig mModuleConfig = FunConfig.getModuleConfig();
    private final String FunTestName = mModuleConfig.FunTestName;
    private final int BT_STANDORD = mModuleConfig.BT_STANDORD;

    public FunUiBTManager(Context context){
        mContext = context;
    }
    @Override
    public boolean OnCreate(View view) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        mContext.registerReceiver(mReceiver, filter);


        LinearLayout _LinearLayout = (LinearLayout)view;
        View _view = View.inflate(mContext, R.layout.test_item_bar, null);
        mTestNameView = (TextView) _view.findViewById(R.id.tv_item_bar_name);
        mTestStatusView = (TextView) _view.findViewById(R.id.tv_item_bar_value);
        _LinearLayout.addView(_view);

        startBT();
        mHandler.post(task);
        return false;
    }

    private Runnable task = new Runnable() {
        @Override
        public void run() {
        mHandler.postDelayed(this, 5000);
        if(!isBTstarted){
            startBT();
            isBTstarted = true;
            return;
        }
        OnShow();
        }
    };

    private void startBT() {
        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.startDiscovery();
        } else {
            mBluetoothAdapter.enable();
        }
    }

    @Override
    public boolean OnRefresh() {
        return false;
    }

    @Override
    public boolean OnShow() {
        mTestNameView.setText(R.string.str_bt_test_name);
        String btShow;

        switch (mBTTestStatus){
            case BT_TEST_SUCCESS:
                btShow = mContext.getString(R.string.str_wifi_test_success,
                        mBTLevelValue);
                mTestStatusView.setText(btShow);
                mTestStatusView.setBackgroundColor(Color.parseColor("#ADFF2F"));
                SetTestSuccess();
                break;
            case BT_TEST_LOW:
                btShow = mContext.getString(R.string.str_wifi_test_success,
                        mBTLevelValue);
                mTestStatusView.setText(btShow);
                mTestStatusView.setBackgroundColor(Color.parseColor("#A52A2A"));
                break;
            case BT_TEST_FAIL:
            default:
                mTestStatusView.setText(R.string.str_bt_test_failure);
                mTestStatusView.setBackgroundColor(Color.parseColor("#A52A2A"));
                break;
        }
        return false;
    }

    @Override
    public boolean OnDestroy() {
        mContext.unregisterReceiver(mReceiver);
        return false;
    }

    @Override
    public void OnPause() {

    }

    @Override
    public void OnResume() {

    }

    @Override
    public void SetTestSuccess() {

    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null)
                return;
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                String stateStr = "???";
                switch (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothDevice.ERROR)) {
                    case BluetoothAdapter.STATE_OFF:
                        stateStr = "off";
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        stateStr = "turning on";
                        break;
                    case BluetoothAdapter.STATE_ON:
                        stateStr = "on";
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        stateStr = "turning off";
                        break;
                }
                Log.d(TAG, "BT state is " + stateStr);
            } else if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
                Log.d(TAG, "Scan name = "+ name  + "is eq=" + ((FunTestName.equals(name))?1:0));
                if (FunTestName.equals(name)) {
                    mBTLevelValue =  intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
                    Log.d(TAG, "Scan rssi. = "+ mBTLevelValue);
                    getBTStatus(mBTLevelValue);
                }
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
                Log.d(TAG, "Scan started...");
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                Log.d(TAG, "Scan ended");
                isBTstarted = false;
            }
        }
    };

    private void getBTStatus(int value) {
        if (value == BT_FAIL_VALUE) {
            mBTTestStatus = BT_TEST_FAIL;
        }
        if (value > BT_FAIL_VALUE && value < BT_STANDORD) {
            mBTTestStatus = BT_TEST_LOW;
        }
        if (value >= BT_STANDORD && value <= -1) {
            mBTTestStatus = BT_TEST_SUCCESS;
        }
    }
}
