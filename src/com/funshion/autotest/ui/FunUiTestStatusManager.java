package com.funshion.autotest.ui;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.funshion.autotest.R;

/**
 * Created by chenhai on 11/17/15.
 */
public class FunUiTestStatusManager implements FunUiBase{
    private View mView = null;
    private Context mContext = null;

    private TextView mTestNameView = null;
    private TextView mTestResultView = null;
    private TextView mLockView = null;

    private boolean mFlagResult = false;

    public FunUiTestStatusManager(Context context){
        mContext = context;
    }

    @Override
    public boolean OnCreate(View view) {
        mView = view;

        creatview();

        return false;
    }

    private void creatview(){
        LinearLayout _LinearLaout = (LinearLayout)mView;
//        View _view = View.inflate(mContext, R.layout.test_status_bar, null);
//        mTestNameView = (TextView)_view.findViewById(R.id.tv_status_bar_name);
//        mTestResultView = (TextView)_view.findViewById(R.id.tv_status_bar_value);
//        mLockView = (TextView)_view.findViewById(R.id.tv_status_lock);

//        _LinearLaout.addView(_view);
        mTestNameView.setText(R.string.str_test_status_name);
    }


    @Override
    public boolean OnShow() {
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
