package com.funshion.autotest.ui;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.funshion.autotest.R;
import com.funshion.autotest.util.deviceUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by chenhai on 6/6/16.
 */
public class FunUiPQManager implements FunUiBase {
    private final String TAG = "FunUiPQManager";
    private Context mContext = null;
    private View mView = null;
    private TextView mPQVersionView = null;
    private Spinner mPQSpinnerView = null;
    private ArrayAdapter<String> adapter;

    private TextView mDeviceModelView = null;
    private Spinner mDevSpinnerView = null;

    private Map<String, ?> mBuildInfoList = null;


    public FunUiPQManager(Context context){
        mContext = context;
    }

    @Override
    public boolean OnCreate(View view) {
        mView = view;

        creatView();
        return false;
    }

    private void creatView(){
        LinearLayout _linearLayout = (LinearLayout) mView;
        View _view = View.inflate(mContext, R.layout.pq_spinner_bar, null);

        mPQVersionView = (TextView) _view.findViewById(R.id.str_pq_version);
        mPQSpinnerView = (Spinner) _view.findViewById(R.id.pq_spinner);

//        mDeviceModelView = (TextView) _view.findViewById(R.id.str_device_model);
//        mDevSpinnerView = (Spinner) _view.findViewById(R.id.device_model_spinner);

        _linearLayout.addView(_view);
    }

    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String currentDevice = (String) adapterView.getItemAtPosition(i);
            deviceUtils.mCurrentDevice = currentDevice;
            Log.d(TAG, "device :"+ currentDevice);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    @Override
    public boolean OnShow() {
//        String showDevText = mContext.getString(R.string.str_device_model, "NULL");
//        mDeviceModelView.setText(showDevText);
//        mDevSpinnerView.setAdapter(new ArrayAdapter<String>(mContext,
//                android.R.layout.simple_spinner_item, getDevData()));
//        mDevSpinnerView.setOnItemSelectedListener(new SpinnerSelectedListener());
//        mDevSpinnerView.setVisibility(View.VISIBLE);

        String showText = mContext.getString(R.string.str_pq_version, "NULL");
        mPQVersionView.setText(showText);
        adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item,
                               getPQData());
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPQSpinnerView.setAdapter(adapter);
        mPQSpinnerView.setOnItemSelectedListener(new SpinnerSelectedListener());
        mPQSpinnerView.setVisibility(View.VISIBLE);

        return false;
    }

    private List<String> getPQData() {
        // 数据源
        List<String> dataList = new ArrayList<String>();

        deviceUtils mDevHandle = new deviceUtils(mContext);

        mBuildInfoList = mDevHandle.getBuildInfoList();
        int listSize = mBuildInfoList.size();
        if (listSize == 0) {
            return null;
        }

        Iterator devItem = mBuildInfoList.entrySet().iterator();

        while (devItem.hasNext()) {
            @SuppressWarnings("rawtypes")
            Map.Entry mapEntry = (Map.Entry) devItem.next();
            String name = (String) mapEntry.getKey();

            dataList.add(name);
        }

        return dataList;
    }

    private List<String> getDevData() {
        // 数据源
        List<String> dataList = new ArrayList<String>();
        dataList.add("aaa");
        dataList.add("bbb");
        return dataList;
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
