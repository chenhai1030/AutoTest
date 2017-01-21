package com.funshion.autotest.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.funshion.autotest.R;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.List;

/**
 * Created by chenhai on 11/17/15.
 */
public class FunUiEthManager implements FunUiBase{
    private Context mContext = null;

    private View mView = null;
    private TextView mTestEthNameView = null;
    private TextView mTestEthResultView = null;
    private String mEthernetIpAddress = null;
    private Method mMethodGetActiveLinkProperties = null;
    private boolean mCheckResult = false;

    private final static int FUNTV_ETHERNET_CHANGER = 1;
    private final static int FUNTV_WIFI_CHANGER = 1;

    public FunUiEthManager(Context context){
        mContext = context;
    }

    @Override
    public boolean OnCreate(View view) {
    	mView = view;
    	
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        mContext.registerReceiver(mConnectivyReceiver, filter);

        createView();
        return true;
    }

    private void createView() {
        LinearLayout _LinearLayout = (LinearLayout) mView;
        View _view = View.inflate(mContext, R.layout.test_item_bar, null);
        mTestEthNameView = (TextView) _view.findViewById(R.id.tv_item_bar_name);
        mTestEthResultView = (TextView) _view.findViewById(R.id.tv_item_bar_value);
        _LinearLayout.addView(_view);

    }

    private BroadcastReceiver mConnectivyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)){
                int type = intent.getIntExtra(ConnectivityManager.EXTRA_NETWORK_TYPE, 0);

                if (type == ConnectivityManager.TYPE_ETHERNET){
                    mHandler.sendEmptyMessage(FUNTV_ETHERNET_CHANGER);
                }
            }
        }
    };

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == FUNTV_ETHERNET_CHANGER){
                boolean bIsCheck = runCheck();
                if (bIsCheck != mCheckResult)
                {
                    mCheckResult = bIsCheck;
                    OnShow();
                }
            }

            super.handleMessage(msg);
        }
    };

    private boolean runCheck(){
        boolean checkResult = false;

        ConnectivityManager connMgr = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);

        if (info.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
            mEthernetIpAddress = getLocalIpAddress();
            checkResult = true;
        }

        return checkResult;
    }

    private String getLocalIpAddress() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        LinkProperties prop = null;
        try {
            mMethodGetActiveLinkProperties = cm.getClass().getMethod("getActiveLinkProperties",null);
//        LinkProperties prop = cm.getActiveLinkProperties();
            prop = (LinkProperties)mMethodGetActiveLinkProperties.invoke(cm,null);
        }catch (Exception e){

        }

        return formatIpaddr(prop);
    }

    private static String formatIpaddr(LinkProperties prop){
        if(prop == null){
            return null;
        }
        Iterator<InetAddress> iter = null;
        try {
            Method methodGetAllAddress = prop.getClass().getMethod("getAllAddresses", null);
            List<InetAddress> addr = (List)methodGetAllAddress.invoke(prop, null);
            iter = addr.iterator();
        }catch (Exception e){

        }

        // If there are no entries, return null
        if (!iter.hasNext()) return null;
        // Concatenate all available addresses, comma separated
        String addresses = "";
        while (iter.hasNext()) {
            addresses += iter.next().getHostAddress();
            if (iter.hasNext()) addresses += "\n";
        }
        return addresses;
    }

    @Override
    public boolean OnShow() {
        mTestEthNameView.setText(R.string.action_settings);

        if (mCheckResult){
            String showText = mContext.getString(R.string.str_wire_test_success, mEthernetIpAddress);
            mTestEthResultView.setText(showText);
            mTestEthResultView.setBackgroundColor(Color.parseColor("#ADFF2F"));
        }else{
            mTestEthResultView.setText(R.string.str_wire_test_failure);
            mTestEthResultView.setBackgroundColor(Color.parseColor("#A52A2A"));
        }

        return true;
    }

    private Handler mRefreshHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    mCheckResult = runCheck();
                    break;
                case 1:
                default:
                    mCheckResult = false;
                    break;
            }
            OnShow();
            super.handleMessage(msg);
        }
    };

    @Override
    public boolean OnRefresh() {
        new Thread(){
            public void run(){
                mRefreshHandler.sendEmptyMessage(1);
                mRefreshHandler.sendEmptyMessageDelayed(2,1000);
            }
        }.start();
        return true;
    }

    @Override
    public boolean OnDestroy() {
    	mContext.unregisterReceiver(mConnectivyReceiver);
        return true;
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
