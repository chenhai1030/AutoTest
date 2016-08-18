package com.funshion.autotest.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.funshion.autotest.R;

/**
 * Created by chenhai on 5/30/16.
 */
public class FunUiKeyPadManager implements FunUiBase {
    private final String TAG = "AutoTest";
    private Context mContext = null;
    private View mView = null;
    private int panelKeyNum = 7;
    private int mCheckResult = -1;
    private TextView mPanelTestNameView = null;
    private TextView mPanelTestResultView = null;


    public static String FUN_KEYPAD_ACTION = "com.funshion.autotest.KEYPADPRESSED";

    public FunUiKeyPadManager(Context context){
        mContext = context;
    }

    @Override
    public boolean OnCreate(View view) {
        mView = view;

        IntentFilter filter = new IntentFilter();
        filter.addAction(FUN_KEYPAD_ACTION);
        mContext.registerReceiver(mPanelReciver, filter);

        creatView();
        return false;
    }

    private void creatView(){
        LinearLayout _LinearLayout = (LinearLayout) mView;
        View _view = View.inflate(mContext, R.layout.test_item_bar, null);

        mPanelTestNameView = (TextView) _view.findViewById(R.id.tv_item_bar_name);
        mPanelTestResultView = (TextView) _view.findViewById(R.id.tv_item_bar_value);
        _LinearLayout.addView(_view);
    }

    private final BroadcastReceiver mPanelReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            Log.d(TAG,"action = "+ action);

            if (action.equals("android.intent.action.BOOT_COMPLETED")) {
                Log.d(TAG,"*********** isBootcompletedBroadcast");
            }

            if (action.equals(FUN_KEYPAD_ACTION)){
                int date = intent.getIntExtra(action, 0);
                mRefreshHandler.sendEmptyMessage(date);
            }

        }
    };

    private Handler mRefreshHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mCheckResult = msg.what;
            OnShow();
            super.handleMessage(msg);
        }
    };

    @Override
    public boolean OnShow() {
        mPanelTestNameView.setText("KeyPad Test");
        if (mCheckResult > 0){
            String showText = mContext.getString(R.string.str_panel_test_success, mCheckResult);
            mPanelTestResultView.setText(showText);
            if (mCheckResult == panelKeyNum){
                mPanelTestResultView.setBackgroundColor(Color.parseColor("#ADFF2F"));
            }else {
                mPanelTestResultView.setBackgroundColor(Color.parseColor("#A52A2A"));
            }
        }else{
            mPanelTestResultView.setText(R.string.str_panel_test_fail);
            mPanelTestResultView.setBackgroundColor(Color.parseColor("#A52A2A"));
        }
        return false;
    }

    @Override
    public boolean OnRefresh() {
        new Thread(){
            @Override
            public void run() {
                mRefreshHandler.sendEmptyMessage(1);
            }
        }.start();
        return false;
    }

    @Override
    public boolean OnDestroy() {
        mContext.unregisterReceiver(mPanelReciver);
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
