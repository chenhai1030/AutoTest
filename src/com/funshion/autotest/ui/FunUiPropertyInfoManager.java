package com.funshion.autotest.ui;

import android.content.Context;
import android.os.SystemProperties;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.funshion.autotest.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FunUiPropertyInfoManager implements FunUiBase{
    private String TAG = "AutoTest";
    private Context mContext = null;
    private View mView = null;
    private String PROPERTY_SOFTWARE_ID = "ro.build.version.incremental";
    private String PROPERTY_BUILD_DATE = "ro.build.date";
    private String PROPERTY_DISP_ID = "ro.build.display.id";

    private String mSoftwareVersion = null;
    private String mPublishTime = null;
    private String mDisplayId = null;
    private List<PropertyInfo> mPropertyNameViewIDList = new ArrayList<FunUiPropertyInfoManager.PropertyInfo>();
    private View[] mViewList = null;

    private class PropertyInfo {
        int mResID;
        String mPropertyValue;

        PropertyInfo(int resID, String propName) {
            mResID = resID;
            mPropertyValue = propName;
        }
    }
    public FunUiPropertyInfoManager(Context context){
        mContext = context;
        getPropertyValues();
        Log.i(TAG, "mSoftwareVersion  = "+mSoftwareVersion);
        addInfoList();
    }

    @Override
    public boolean OnCreate(View view) {
        mView = view;

        createView();
        return false;
    }

    private void createView() {
        LinearLayout _LinearLayout = (LinearLayout) mView;
        int listSize = mPropertyNameViewIDList.size();

        Log.i(TAG, "listsize = " + listSize);
//        Toast.makeText(mContext, "text view 1", Toast.LENGTH_SHORT).show();
        mViewList = new View[listSize];
        for (int index = 0; index < listSize; index++) {
            View _view = View.inflate(mContext, R.layout.info_item_bar, null);
            mViewList[index] = _view;
            _LinearLayout.addView(mViewList[index]);
        }
    }

    private void getPropertyValues(){
        mSoftwareVersion = SystemProperties.get(PROPERTY_SOFTWARE_ID);
//        mPublishTime ＝ SystemProperties.get(PROPERTY_BUILD_DATE);
//        mDisplayId = SystemProperties.get(mDisplayId);

        long time = android.os.Build.TIME;
        Date date = new Date(time);
        mPublishTime = (1900 + date.getYear()) + " "
                + mContext.getString(R.string.str_year) + (1 + date.getMonth())
                + mContext.getString(R.string.str_month) + date.getDate()
                + mContext.getString(R.string.str_day) + date.getHours()
                + mContext.getString(R.string.str_hour) + date.getMinutes()
                + mContext.getString(R.string.str_minute) + date.getSeconds()
                + mContext.getString(R.string.str_second);
    }

    private void addInfoList(){
        mPropertyNameViewIDList.clear();

        mPropertyNameViewIDList.add(new PropertyInfo(R.string.str_software_version_name, mSoftwareVersion));
        mPropertyNameViewIDList.add(new PropertyInfo(R.string.str_publishtime_name, mPublishTime));
    }

    @Override
    public boolean OnShow() {
        int listsize = mPropertyNameViewIDList.size();

        for(int index = 0; index < listsize; index ++){
            PropertyInfo propertyInfo = mPropertyNameViewIDList.get(index);

            TextView nameView = (TextView) mViewList[index]
                    .findViewById(R.id.tv_item_bar_name);
            String name = mContext.getString(propertyInfo.mResID);
            nameView.setText(name);

            TextView valueView = (TextView) mViewList[index]
                    .findViewById(R.id.tv_item_bar_value);
            String value = propertyInfo.mPropertyValue;
            valueView.setText(value);
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
