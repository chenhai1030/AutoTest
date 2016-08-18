package com.funshion.autotest.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.funshion.autotest.R;
import com.funshion.autotest.logic.FunLogicTvSourceManager;
import com.funshion.autotest.util.deviceUtils;
import com.funshion.autotest.util.portInfo;
import com.mstar.android.tv.TvCommonManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenhai on 5/4/16.
 */
public class FunUiTvSourceManager implements FunUiBase{
    private String TAG = "AutoTest";
    private Context mContext = null;
    private View mView = null;
    private View[] mViewList = null;

    /*TV Source ID*/
    private static final int FUN_VIEW_HDMI_ID = 0x100;
    private static final int FUN_VIEW_ATV_ID = 0x200;
    private static final int FUN_VIEW_DTV_ID = 0x300;
    private static final int FUN_VIEW_AV_ID = 0x400;
    private static final int FUN_VIEW_YUV_ID = 0x500;
    private static final int FUN_VIEW_PC_ID = 0x600;

    private portInfo mPortInfo = null;

    private static int mHdmiCount = 3;

    private List<TvSource> mTvSourceViewIDList = new ArrayList<TvSource>();

    private class TvSource{
        public int mResID;
        public int mViewID;

        public TvSource(int resID, int ViewID){
            mResID = resID;
            mViewID = ViewID;
        }
    }

    public FunUiTvSourceManager(Context context){
        mContext = context;
    }

    @Override
    public boolean OnCreate(View view) {
        mView = view;
        addInfoList();
        createView();
        setViewListProperty();
        return false;
    }

    private void addInfoList(){
        mTvSourceViewIDList.clear();

        deviceUtils mDevHandle = new deviceUtils(mContext);
        mPortInfo = mDevHandle.getPortInfo();


        mTvSourceViewIDList.add(new TvSource(R.string.str_HDMI1_port, FUN_VIEW_HDMI_ID));
        mTvSourceViewIDList.add(new TvSource(R.string.str_HDMI2_port, FUN_VIEW_HDMI_ID+1));
        if (mPortInfo.getHdmiPortNum() > 2){
            mTvSourceViewIDList.add(new TvSource(R.string.str_HDMI3_port, FUN_VIEW_HDMI_ID+2));
        }

        mTvSourceViewIDList.add(new TvSource(R.string.str_AV_port, FUN_VIEW_AV_ID));
        mTvSourceViewIDList.add(new TvSource(R.string.str_YUV_port, FUN_VIEW_YUV_ID));
        mTvSourceViewIDList.add(new TvSource(R.string.str_ATV_channel, FUN_VIEW_ATV_ID));
        mTvSourceViewIDList.add(new TvSource(R.string.str_DTV_channel, FUN_VIEW_DTV_ID));

        if (mPortInfo.getPcPortNum() == 1){
            mTvSourceViewIDList.add(new TvSource(R.string.str_PC_port, FUN_VIEW_PC_ID));
        }
    }

    private final View.OnFocusChangeListener mOnFocusChangeHandler =
        new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                TextView textView = (TextView) v;
                if (hasFocus) {
                    textView.setBackgroundColor(Color.GRAY);
                }else{
                    textView.setBackgroundColor(Color.TRANSPARENT);
                }

            }
        };

    private void setViewListProperty() {
        int listSize = mTvSourceViewIDList.size();
        int i = 1;
        String name = null;

        for (int index = 0; index < listSize; index++) {
            TvSource _tvSource = mTvSourceViewIDList.get(index);

            if (i <= mHdmiCount) {
                name = mContext.getString(_tvSource.mResID, i++);
            }else{
                name = mContext.getString(_tvSource.mResID);
            }
            Log.d(TAG, "name = "+ name);
            ((TextView) mViewList[index]).setText(name);
            int id = _tvSource.mViewID;
            mViewList[index].setId(id);
            mViewList[index].setOnFocusChangeListener(mOnFocusChangeHandler);
            mViewList[index].setOnClickListener(mOnClickListener);
        }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int viewID = view.getId();
            TvCommonManager _tvCommonManager = TvCommonManager.getInstance();

            switch (viewID){
                case FUN_VIEW_HDMI_ID:
                    Toast.makeText(mContext, "HDMI1",
                            Toast.LENGTH_SHORT).show();
                    _tvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_HDMI);
                    break;
                case FUN_VIEW_HDMI_ID+1:
                    Toast.makeText(mContext, "HDMI2",
                            Toast.LENGTH_SHORT).show();
                    _tvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_HDMI2);
                    break;
                case FUN_VIEW_HDMI_ID+2:
                    Toast.makeText(mContext, "HDMI3",
                            Toast.LENGTH_SHORT).show();
                    _tvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_HDMI3);
                    break;
                case FUN_VIEW_AV_ID:
                    Toast.makeText(mContext, "AV",
                            Toast.LENGTH_SHORT).show();
                    _tvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_CVBS);
                    break;
                case FUN_VIEW_YUV_ID:
                    Toast.makeText(mContext, "YUV",
                            Toast.LENGTH_SHORT).show();
                    _tvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_YPBPR);
                    break;
                case FUN_VIEW_ATV_ID:
                    Toast.makeText(mContext, "ATV",
                            Toast.LENGTH_SHORT).show();
                    _tvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_ATV);
                    break;
                case FUN_VIEW_DTV_ID:
                    Toast.makeText(mContext, "DTV",
                            Toast.LENGTH_SHORT).show();
                    _tvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_DTV);
                    break;
                case FUN_VIEW_PC_ID:
                    Toast.makeText(mContext, "VGA",
                            Toast.LENGTH_SHORT).show();
                    _tvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_VGA);
                    break;
                default:
                    Log.d(TAG, "error viewID!");
                    break;
            }

            FunLogicTvSourceManager.getInstance().startToPlay();
        }
    };

    private void createView(){
        LinearLayout _LinearLayout = (LinearLayout) mView;
        LinearLayout SourceItemLayout = (LinearLayout)View.inflate(mContext,
                R.layout.source_item_bar, null);
        View _view = (View)View.inflate(mContext,
                R.layout.source_item_bar, null);

        int listsize = mTvSourceViewIDList.size();

        mViewList = new View[listsize];
//        for (int index = 0; index < listsize; index++){
//            TextView _TextView = new TextView(mContext);
//
//            _TextView.setFocusable(true);
//            mViewList[index] = _TextView;
//            SourceItemLayout.addView(mViewList[index]);
//        }
//        _LinearLayout.addView(SourceItemLayout);


        for(int index = 0; index < listsize; index++){
            mViewList[index] = _view.findViewById(R.id.str_HDMI1_port+index);
            mViewList[index].setFocusable(true);
        }
        _LinearLayout.addView(_view);



//        for(int index = 0; index < listsize; index++){
//            TextView _TextView = new TextView(mContext);
//            LinearLayout.LayoutParams textViewParams =
//                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT);
//            textViewParams.leftMargin = 10;
//            textViewParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
//            _TextView.setLayoutParams(textViewParams);
//
//            _TextView.setFocusable(true);
//            if (index == 0){
////                _TextView.requestFocus();
//            }
//            mViewList[index] = _TextView;
//            SourceItemLayout.addView(mViewList[index]);
//        }

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
        FunLogicTvSourceManager.getInstance().stopToPlay();
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
