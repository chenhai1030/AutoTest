package com.funshion.autotest.ui;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.funshion.autotest.R;

import java.util.HashMap;
import java.util.Map;

public class FunUiParamManager implements FunUiBase{
    private Context mContext = null;
    private View[] mViewList = null;
    private Map<String, String> mParamNameViewIDList = new HashMap<String, String>();
    private String TAG = "FunUiParamManager";

    public FunUiParamManager(Context context){
        mContext = context;

        mParamNameViewIDList.put("mac", context.getString(R.string.str_mac_address));
        mParamNameViewIDList.put("sn", context.getString(R.string.str_serial_number));
        mParamNameViewIDList.put("hdcp", context.getString(R.string.str_hdcp_key));
//        mParamNameViewIDList.put("pqversion", context.getString(R.string.str_pq_version));
    }

    @Override
    public boolean OnCreate(View view) {
        LinearLayout _LinearLayout = (LinearLayout) view;

        int paramCount = 0;
        paramCount = mParamNameViewIDList.size();

        mViewList = new View[paramCount];

        for(int index = 0; index < paramCount; index ++){
            View _view = View.inflate(mContext, R.layout.info_item_bar, null);
            mViewList[index] = _view;

            _LinearLayout.addView(_view);
        }

        return false;
    }

    @Override
    public boolean OnShow() {
        int index = 0;

        for(Map.Entry<String, String> entry: mParamNameViewIDList.entrySet()){
            TextView nameView = (TextView) mViewList[index].findViewById(R.id.tv_item_bar_name);
            nameView.setText(entry.getValue());

            TextView valueView = (TextView) mViewList[index].findViewById(R.id.tv_item_bar_value);
            String value = null;

            if (null == value || value.isEmpty()) {
                valueView.setText(" ");
            } else {
                valueView.setText(value);
            }

            index ++;
        }
        return true;
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
