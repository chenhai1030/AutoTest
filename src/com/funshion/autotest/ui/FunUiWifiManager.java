package com.funshion.autotest.ui;

import android.content.Context;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.funshion.autotest.FunConfig;
import com.funshion.autotest.R;

import java.util.List;
import java.util.Timer;

/*
 * Created by chenhai on 5/13/16.
 */
public class FunUiWifiManager implements FunUiBase {
    private String TAG = "FunUiWifiManager";
    private Context mContext = null;
    private TextView mTestNameView = null;
    private TextView mTestStatusView = null;
    private int mWifiTestStatus = 0;
    private static final int WIFI_TEST_SUCCESS = 0x1100;
    private static final int WIFI_TEST_FAIL = 0x1101;
    private static final int WIFI_TEST_LOW = 0x1102;
    private static final int WIFI_FAIL_VALUE = -100;
    private int mWifiLevelValue = -100;
    private boolean mIsWifiRefresh = false;
    private boolean iswifistarted = false;

    private Handler mHandler = new Handler();

    private WifiManager mWifiManager = null;
    private FunConfig.ModuleConfig mModuleConfig = FunConfig.getModuleConfig();
    private final String FunTestSSID = mModuleConfig.FunTestSSID;
    private final int WIFI_STANDORD = mModuleConfig.WIFI_STANDORD;

    private boolean mCheckResult = false;
    private final Timer timer = new Timer();

    public FunUiWifiManager(Context context){
        mContext = context;
    }

    @Override
    public boolean OnCreate(View view) {

        LinearLayout _LinearLayout = (LinearLayout)view;
        View _view = View.inflate(mContext, R.layout.test_item_bar, null);
        mTestNameView = (TextView) _view.findViewById(R.id.tv_item_bar_name);
        mTestStatusView = (TextView) _view.findViewById(R.id.tv_item_bar_value);
        _LinearLayout.addView(_view);

        mHandler.post(task);
        return false;
    }

    private Runnable task = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            mHandler.postDelayed(this, 5000);
            if(!iswifistarted){
                startWifi();
                iswifistarted = true;
                return;
            }

            ScanResult scanResult = getWifiScanResult();
            mWifiLevelValue = getWifiSignalValue(scanResult);
            getWifiStatus(mWifiLevelValue);
            OnShow();
        }
    };


    private void startWifi() {
        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
    }

    public int getWifiSignalValue(ScanResult scanResult) {
        if (scanResult == null) {
            return WIFI_FAIL_VALUE;
        } else {
            return scanResult.level;
        }
    }

    private void printScanResult(ScanResult result) {
        Log.d(TAG, "############sc.level = " + result.level
                + "############  sc.frequency = " + result.frequency
                + "############ sc. SSID = " + result.SSID
                + "############ sc.capabilities = " + result.capabilities);
    }

    private void getWifiStatus(int value) {
        if (value == WIFI_FAIL_VALUE) {
            mWifiTestStatus = WIFI_TEST_FAIL;
        }
        if (value > WIFI_FAIL_VALUE && value < WIFI_STANDORD) {
            mWifiTestStatus = WIFI_TEST_LOW;
        }
        if (value >= WIFI_STANDORD && value <= -1) {
            mWifiTestStatus = WIFI_TEST_SUCCESS;
        }
    }

    private ScanResult getWifiScanResult(){
        ScanResult tempResult = null;

        mWifiManager.startScan();

        final List<ScanResult> mwifiList = mWifiManager.getScanResults();

        Log.d(TAG, "scan wifi size is " + mwifiList.size());
        if (mwifiList.isEmpty() || mwifiList.size() <= 0) {
            return null;
        }

        for (ScanResult tempScan:mwifiList){
            if(tempScan == null){
                continue;
            }

            Log.d(TAG, "SSID = "+ tempScan.SSID);
            if(!tempScan.SSID.equals(FunTestSSID)){
                continue;
            }

            printScanResult(tempScan);

            if (tempResult == null) {
                tempResult = tempScan;
                continue;
            }

            if (tempResult.level < tempScan.level) {
                tempResult = tempScan;
            }
        }

        return tempResult;
    }


    @Override
    public boolean OnShow() {
        mTestNameView.setText(R.string.str_wifi_test_name);
        String wifiShow;

        switch (mWifiTestStatus){
            case WIFI_TEST_SUCCESS:
                wifiShow = mContext.getString(R.string.str_wifi_test_success,
                        mWifiLevelValue);
                mTestStatusView.setText(wifiShow);
                mTestStatusView.setBackgroundColor(Color.parseColor("#ADFF2F"));
                SetTestSuccess();
                break;
            case WIFI_TEST_LOW:
                wifiShow = mContext.getString(R.string.str_wifi_test_success,
                        mWifiLevelValue);
                mTestStatusView.setText(wifiShow);
                mTestStatusView.setBackgroundColor(Color.parseColor("#A52A2A"));
                break;
            case WIFI_TEST_FAIL:
            default:
                mTestStatusView.setText(R.string.str_wifi_test_failure);
                mTestStatusView.setBackgroundColor(Color.parseColor("#A52A2A"));
                break;
        }
        return false;
    }

    @Override
    public boolean OnRefresh() {
        return false;
    }

    @Override
    public boolean OnDestroy() {
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
}
