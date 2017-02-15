package com.funshion.autotest;

/*
 * Created by chenhai on 7/4/16.
 */
public class FunConfig {
    /**
     * Funtv Test System current module config
     */
    private static ModuleConfig mModuleConfig = null;

    public static ModuleConfig getModuleConfig(){
        if (null != mModuleConfig) {
            return mModuleConfig;
        }

        mModuleConfig = new ModuleConfig();

        return mModuleConfig;
    }

    public static class ModuleConfig {
        /**
         * config test item
         */
        boolean mModuleTestWifiLink = true;
        boolean mModuleTestBTLink = true;
        boolean mModuleTestUsbDevice = true;
        boolean mModuleTestWireLink = true;
        boolean mModuleTestLocalVideo = true;
        boolean mModuleTestInputSignal = true;
        boolean mModuleTestStatus = true;
        boolean mModuleTestKeyPad = true;

		/*
		 * config show info
		 */

        boolean mModuleInfoProperty = true;
        boolean mModuleInfoParams = true;
        boolean mModuleInfoPQSpinner = false;

        /*
         * config WIFI test SSID
         */
        public final String FunTestSSID = "funshion-wh";
        public final int WIFI_STANDORD = -60;

        /*
         * config BT test BSSID
         */
        public final String FunTestName = "小米蓝牙音箱";
        public final int BT_STANDORD = -60;

        ModuleConfig() {

        }
    }
}
