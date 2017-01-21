package com.funshion.autotest.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenhai on 5/30/16.
 */
public class deviceUtils {
    public static final String TAG = "AutoTest";
    public static final String KEYPAD_DEV_NAME = "MStar Smart TV Keypad";

    private Context mContext = null;
    public static final String DATABASE_NAME_STR = "model";
    public static final String DB_BUILD_INFO_TAB = "build_info";
    public static final String DB_DEVICE_INFO_TAB = "device_info";
    public static final String DB_PROP_INFO_TAB = "prop_info";

    /* device info */
    public static final String DEVICE_CODE_STR  = "device_code";
    public static final String PANEL_MODEL_STR   = "panel_model";
    public static final String PANEL_CLASS_STR  = "panel_class";
    public static final String BRAND_STR       = "brand";

    public static final String HDMI_AMOUNT_STR = "hdmi_amount";
    public static final String HDMI_REVERT_STR = "hdmi_revert";
    public static final String USB_AMOUNT_STR = "usb_amount";
    public static final String AV_AMOUNT_STR = "av_amount";
    public static final String PC_AMOUNT_STR = "pc_amount";

    /* build info */
    public static final String DEVICE_MODEL_STR = "device_model";
    public static final String SUPPER_STR       = "supper";
    public static final String DISP_NAME_STR    = "display_name";
    public static final String BRANCH_STR       = "branch";
    public static final String KEYPAD_STR       = "keypad";
    public static final String IS_NEW_STR       = "is_new";
    public static final String PANEL_TYPE_STR   = "panel_4k";

    public static final String KEYPAD_5KEY_STR = "keys_5";
    public static final String KEYPAD_7KEY_STR = "keys_7";

    public static final int KEYPAD_TYPE_5KEY = 0x100;
    public static final int KEYPAD_TYPE_7KEY = 0x200;


    public final static int KEYPAD_UP_FLAG    = 0x01;
    public final static int KEYPAD_DOWN_FLAG  = 0x02;
    public final static int KEYPAD_LEFT_FLAG  = 0x04;
    public final static int KEYPAD_RIGHT_FLAG = 0x08;
    public final static int KEYPAD_MENU_FLAG  = 0x10;
    public final static int KEYPAD_MTV_FLAG   = 0x20;
    public final static int KEYPAD_POWER_FLAG = 0x40;

    public static String mDispName = null;

    public static boolean isHDPanel = false;

    public static String mCurrentDevice = null;

    private Map<String, deviceInfo> mDeviceInfoList = null;
    private Map<String, buildInfo> mBuildInfoList = null;


    public deviceUtils(Context context){
        mContext = context;

        addDeviceInfoList();
        addBuildInfoList();
    }

    public class deviceInfo{
        public String mDeviceModel;
        public String mPanelMode;
        public String mPanelClass;
        public int mHDMImount;
        public int mUSBmount;
        public int mAVmount;
        public boolean mHDMIRevert;

        public deviceInfo(String deviceModel,
                          String panelMode,
                          String panelClass,
                          int hdmiMount,
                          int usbMount,
                          int avMount,
                          boolean hdmiRevert){
            mDeviceModel = deviceModel;
            mPanelMode = panelMode;
            mPanelClass = panelClass;
            mHDMImount = hdmiMount;
            mUSBmount = usbMount;
            mAVmount = avMount;
            mHDMIRevert = hdmiRevert;
        }
    }

    public class buildInfo{
        public String mBuildDeviceModel;
        public String mBuildBranch;
        public String mBuildDispName;
        public String mBuildSupper;
        public int mBuildKeyPad;
        public int mBuildStorageType;

        public buildInfo(String deviceModel,
                         String supper,
                         String branch,
                         String dispName,
                         int keypad,
                         int storageType){
            mBuildDeviceModel = deviceModel;
            mBuildBranch = branch;
            mBuildDispName = dispName;
            mBuildSupper = supper;
            mBuildKeyPad = keypad;
            mBuildStorageType = storageType;
        }
    }

    private void addBuildInfoList(){
        if (mBuildInfoList != null){
            return;
        }
        mBuildInfoList = new HashMap<String, buildInfo>();

        mBuildInfoList.clear();

        Cursor cursor = getDataBaseCursor(DATABASE_NAME_STR, DB_BUILD_INFO_TAB);
        if (cursor == null){
            Log.d(TAG, "database cant be opened!");
            return;
        }
        int count = cursor.getCount();
        Log.d(TAG, "database count = " + count);
        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                int _panelType = cursor.getInt(cursor.getColumnIndex(PANEL_TYPE_STR));
                if (false == isNotUD()){
                    if (_panelType != 1){
                        cursor.moveToNext();
                        continue;
                    }
                }

                String _deviceModel = cursor.getString(cursor.getColumnIndex(DEVICE_MODEL_STR));
                String _supper = cursor.getString(cursor.getColumnIndex(SUPPER_STR));
                String _branch = cursor.getString(cursor.getColumnIndex(BRANCH_STR));
                String _displayName = cursor.getString(cursor.getColumnIndex(DISP_NAME_STR));
                String _keypad = cursor.getString(cursor.getColumnIndex(KEYPAD_STR));

                int _keypadType = 0;
                if (KEYPAD_5KEY_STR.equals(_keypad)){
                    _keypadType = KEYPAD_TYPE_5KEY;
                }else if (KEYPAD_7KEY_STR.equals(_keypad)){
                    _keypadType = KEYPAD_TYPE_7KEY;
                }

                int _storageType = cursor.getInt(cursor.getColumnIndex(IS_NEW_STR));

                String _keyValue = _deviceModel + "_" + _supper;

                mBuildInfoList.put(_keyValue, new buildInfo(_deviceModel, _supper,
                        _branch, _displayName, _keypadType, _storageType));

                Log.d(TAG, "count =" + count + "_keyValue :"+ _keyValue +"" +
                        "=======    devmodel:" + _deviceModel
                        + "supper:" + _supper + "displayName:" + _displayName);
                cursor.moveToNext();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        cursor.close();
    }

    private void addDeviceInfoList(){
        if (mDeviceInfoList != null){
            return;
        }
        mDeviceInfoList = new HashMap<String, deviceInfo>();

        mDeviceInfoList.clear();
        Cursor cursor = getDataBaseCursor(DATABASE_NAME_STR, DB_DEVICE_INFO_TAB);
        if (cursor == null){
            Log.d(TAG, "database cant be opened!");
            return;
        }

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                String _deviceModel = cursor.getString(cursor.getColumnIndex(DEVICE_MODEL_STR));
                String _branch = cursor.getString(cursor.getColumnIndex(BRAND_STR));
                String _panelModel = cursor.getString(cursor.getColumnIndex(PANEL_MODEL_STR));
                String _panelClass = cursor.getString(cursor.getColumnIndex(PANEL_CLASS_STR));
                int _hdmiMount = cursor.getInt(cursor.getColumnIndex(HDMI_AMOUNT_STR));
                boolean _hdmiRevert =
                        cursor.getInt(cursor.getColumnIndex(HDMI_REVERT_STR))==1?true:false;
                int _usbMount = cursor.getInt(cursor.getColumnIndex(USB_AMOUNT_STR));

                String _keyValue = _deviceModel + "_" + _panelModel + "-" + _panelClass;

                Log.d(TAG, "xxxxx =" + "_keyValue : " + _keyValue +
                        "========   ==devmodel:"+ _deviceModel+"_branch:"+_branch
                        + "panelM:"+_panelModel+"panelC:"+_panelClass+"hdimM:"+_hdmiMount
                        + "hdmiRevert:"+_hdmiRevert+ "usbMount:"+_usbMount);

                cursor.moveToNext();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        cursor.close();
    }

    public Map<String, deviceInfo>getDevInfoList(){
        return mDeviceInfoList;
    }

    public Map<String, buildInfo>getBuildInfoList(){
        return  mBuildInfoList;
    }

    public int getScreenWidthPixels(Context context){
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public int getScreenHeightPixels(Context context){
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    private static portInfo mportInfo = null;

    private Cursor getDataBaseCursor(String dbName, String tabName){
       // String AUTHORITY = "model";
        Uri CONTENT_URI = Uri.parse("content://" + dbName + "/" + tabName);
        Cursor cursor = null;
        try {
            cursor = mContext.getContentResolver().query(CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
        }catch (Exception e ){
            e.printStackTrace();
        }
        return cursor;
    }

    public portInfo getPortInfo(){
        if (mportInfo != null){
            return mportInfo;
        }
        mportInfo = new portInfo();
        mportInfo.setEthPortNum(1);
        mportInfo.setAvPortNum(1);
        mportInfo.setYuvPortNum(1);

        Cursor cursor = getDataBaseCursor(DATABASE_NAME_STR, DB_PROP_INFO_TAB);
        if (cursor == null){
            return mportInfo;
        }

        try {
            if(cursor.moveToFirst()){
                mportInfo.setHdmiPortNum(cursor.getInt(cursor.getColumnIndex(HDMI_AMOUNT_STR)));
                mportInfo.setHdmiRevert(cursor.getInt(cursor.getColumnIndex(HDMI_REVERT_STR)) == 1);
                mportInfo.setUsbPortNum(cursor.getInt(cursor.getColumnIndex(USB_AMOUNT_STR)));
                mportInfo.setAvPortNum(cursor.getInt(cursor.getColumnIndex(AV_AMOUNT_STR)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cursor.close();
        }
        return mportInfo;
    }

    public class DeviceModel{
        private String name;
    }

    public class ScreenSupper{
        private DeviceModel deviceModel;
    }

    private boolean isNotUD(){
        return true;
//        String screenType = SystemProperties.get("ro.screen.type");
//        if (screenType == null){
//            return true;
//        }
//
//        if(screenType.equals("UD")){
//            return false;
//        }
//        return false;
    }
}
