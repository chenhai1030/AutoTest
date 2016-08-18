package com.funshion.autotest;

/**
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
        public boolean mModuleTestWifiLink = true;
        public boolean mModuleTestUsbDevice = true;
        public boolean mModuleTestWireLink = true;
        public boolean mModuleTestSdCard = false;
        public boolean mModuleTestLocalVideo = true;
        public boolean mModuleTestInputSignal = true;
        public boolean mModuleTestStatus = true;
        public boolean mModuleTestKeyPad = true;

		/*
		 * config show info
		 */

        public boolean mModuleInfoProperty = true;
        public boolean mModuleInfoParams = true;
        public boolean mModuleInfoPQSpinner = true;

        public ModuleConfig() {

        }
    }
}
